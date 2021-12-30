package knormal.justcart.JustCart_ver4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.JustCart_ver4.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CheckActivity extends AppCompatActivity {//DB의 체크리스트 목록 가져옴

    //DB연동
    //데이터 받아올 php주소
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="webnautes";
    private static final String TAG_NAME = "productName";

    private TextView mTextViewResult;
    private ArrayList<CheckData> mArrayList;
    private CheckAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ImageButton btn_shop, btn_home, btn_mypage, btn_fresh;
    private String USER_ID, ORDER_ID;


    private String mJsonString;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        View view =findViewById(R.id.view2);
        view.setClickable(true);


        //드래그로 화면전환(체크리스트 화면)
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float distance = 0;
                float pressedX = 0;
                float pressedX2 = 0;

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    // 손가락을 touch 했을 떄 x 좌표값 저장
                    pressedX = event.getX();
                    Log.d("viewTest", "newXvalue : " + pressedX);    // View 내부에서 터치한 지점의 상대 좌표값.
                }

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    pressedX2 = event.getX();
                    distance = pressedX - pressedX2;
                    Log.d("viewTest", "oldXvalue : "+ pressedX2);    // View 내부에서 터치한 지점의 상대 좌표값.
                    Log.d("viewTest", "Distance : "+ distance);
                }

                // 해당 거리가 100이 되지 않으면 이벤트 처리 하지 않는다.
                if (Math.abs(distance) < 200) {
                    return false;
                }

                if (distance > 0) {
                    // 손가락을 왼쪽으로 움직였으면 오른쪽 화면이 나타나야 한다.
                    Intent intent = new Intent(CheckActivity.this, ShopActivity2.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                } else {
                    // 손가락을 오른쪽으로 움직였으면 왼쪽 화면이 나타나야 한다.
                    Intent intent = new Intent(CheckActivity.this, ShopActivity2.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                }
                finish(); // finish 해주지 않으면 activity가 계속 쌓인다.

                return true;
            }
        });

        btn_shop = findViewById(R.id.btn_shop9);
        btn_home = findViewById(R.id.btn_home9);
        btn_mypage = findViewById(R.id.btn_mypage9);
        btn_fresh = findViewById(R.id.btn_fresh);

        mTextViewResult = (TextView) findViewById(R.id.textView_main_result2);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView_check_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //메인화면에서 검색
        Intent intent = getIntent();
        USER_ID = SharedPreference.getUserID(CheckActivity.this);
        ORDER_ID = SharedPreference.getOrderID(CheckActivity.this);
        showResult2(USER_ID, ORDER_ID);

        //신선도 버튼을 클릭 시 수행
        btn_fresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckActivity.this, FreshBeaconActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        //장바구니 버튼을 클릭 시 수행
        btn_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckActivity.this, ShopActivity2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        //홈 버튼을 클릭 시 수행
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        //마이페이지 버튼을 클릭 시 수행
        btn_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckActivity.this, MypageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        mArrayList = new ArrayList<>(); //Personal객체를 담을 array리스트(어댑터 쪽으로 날릴거임)
    }

    //메인화면에서 검색 결과 수행
    void showResult2(String stringData1, String stringData2) {
        GetData task2 = new GetData();
        task2.execute(stringData1, stringData2);
    }

    //데이터베이스 통신
    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(CheckActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) { //에러있는 경우 에러메세지 보여줌/ 아니면 JSON파싱해서 화면에 보여줌
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result == null){

                mTextViewResult.setText(errorString);
            }
            else {
                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) { //php파일 실행

            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];

            String serverURL = "http://3.37.3.112/Load_checklist.php";
            String postParameters = "&userID=" + searchKeyword1 + "&order_id=" + searchKeyword2;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    private void showResult() { //DB에서 상세정보 다 가져옴

        try {
            JSONObject jsonObject = new JSONObject(mJsonString); //json파일 받아옴
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON); //json파일의 result array 가져옴

            for (int i = 0; i < jsonArray.length(); i++) {

                //한 딕셔너리를 가져옴-> 하나의 오브젝트에 상품하나의 상세정보 저장
                JSONObject item = jsonArray.getJSONObject(i);
                String Name = item.getString(TAG_NAME);
                //만들어뒀던 Personal객체에 데이터를 담는다
                CheckData checkData = new CheckData();
                checkData.setName(Name);

                mArrayList.add(checkData); //담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비

            }
            mAdapter = new CheckAdapter(this, mArrayList);
            mRecyclerView.setAdapter(mAdapter);

            //여기서 btn_add를 클릭하면
            //체크리스트 페이지로 Name을 보냄 + 체크리스트 DB에 들어가야함





        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

}
