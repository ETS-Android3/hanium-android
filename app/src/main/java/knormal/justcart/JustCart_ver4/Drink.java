package knormal.justcart.JustCart_ver4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class Drink extends AppCompatActivity {

    private ImageButton btn_shop, btn_home, btn_mypage;
    private String stringData;

    private static String TAG = "phpquerytest";

    private static final String TAG_JSON="webnautes";
    private static final String TAG_NAME = "Name";
    private static final String TAG_PRICE = "Price";
    private static final String TAG_DESC ="Desc";
    private static final String TAG_LOCATION ="Location";
    private static final String TAG_IMAGE ="Image";


    private TextView mTextViewResult;
    private ArrayList<PersonalData> mArrayList;
    private CategoryAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        mTextViewResult = (TextView) findViewById(R.id.textView7);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView_main_list2);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        btn_shop = findViewById(R.id.btn_shop10);
        btn_home = findViewById(R.id.btn_home10);
        btn_mypage = findViewById(R.id.btn_mypage10);

        //메인화면에서 검색
        Intent intent = getIntent();
        stringData = intent.getStringExtra("Category");
        showResult2(stringData);

        mArrayList = new ArrayList<>();     //Personal객체를 담을 array리스트(어댑터 쪽으로 날릴거임)

        //장바구니 버튼을 클릭 시 수행
        btn_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Drink.this, ShopActivity2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        //홈 버튼을 클릭 시 수행
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Drink.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        //마이페이지 버튼을 클릭 시 수행
        btn_mypage.setOnClickListener(new View.OnClickListener() {
            Intent intent =getIntent();
            @Override
            public void onClick(View v) {
                if(SharedPreference.getUserName(Drink.this).length() == 0) {
                    Intent intent = new Intent(Drink.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Drink.this, MypageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("STD_NUM", SharedPreference.getUserName(Drink.this).toString());
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    void showResult2(String stringData) {       //메인화면에서 검색 결과 수행
        GetData task2 = new GetData();
        task2.execute(stringData);
    }

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Drink.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {       //에러있는 경우 에러메세지 보여줌/ 아니면 JSON파싱해서 화면에 보여줌
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
        protected String doInBackground(String... params) {     //php파일 실행

            String searchKeyword = params[0];

            String serverURL = "http://3.37.3.112/Category.php";
            String postParameters = "&Category=" + searchKeyword;


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

    private void showResult() {     //DB에서 상세정보 다 가져옴

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);    //json파일 받아옴
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);    //json파일의 result array 가져옴

            for (int i = 0; i < jsonArray.length(); i++) {

                //한 딕셔너리를 가져옴-> 하나의 오브젝트에 상품하나의 상세정보 저장
                JSONObject item = jsonArray.getJSONObject(i);

                String Name = item.getString(TAG_NAME);
                String Price = item.getString(TAG_PRICE);
                String Desc = item.getString(TAG_DESC);
                String Location = item.getString(TAG_LOCATION);
                String Image = item.getString(TAG_IMAGE);

                //만들어뒀던 Personal객체에 데이터를 담는다
                PersonalData personalData = new PersonalData();

                personalData.setName(Name);
                personalData.setPrice(Price);
                personalData.setDesc(Desc);
                personalData.setLocation(Location);
                personalData.setImage(Image);

                mArrayList.add(personalData); //담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
            }
            mAdapter = new CategoryAdapter(this, mArrayList);
            mRecyclerView.setAdapter(mAdapter);

        } catch (JSONException /*| MalformedURLException*/ e) {

            Log.d(TAG, "showResult : ", e);
        }

    }
}