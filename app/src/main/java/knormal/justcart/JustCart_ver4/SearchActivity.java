package knormal.justcart.JustCart_ver4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
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

public class SearchActivity extends AppCompatActivity{

    private ImageButton btn_shop, btn_home, btn_mypage, btn_add3;

    private static String TAG = "phpquerytest";

    private static final String TAG_JSON="webnautes";
    private static final String TAG_NAME = "Name";
    private static final String TAG_RNAME = "recommendProduct";
    private static final String TAG_PRICE = "Price";
    private static final String TAG_DESC ="Desc";
    private static final String TAG_LOCATION ="Location";
    private static final String TAG_IMAGE ="Image";

    private String USER_ID, ORDER_ID;


    private TextView mTextViewResult;
    private ArrayList<PersonalData> mArrayList;
    private ArrayList<RecommData> reArrayList;
    EditText mEditTextSearchKeyword;

    //상세정보용 어댑터
    private UsersAdapter mAdapter;
    private RecyclerView mRecyclerView;

    //추천용 어댑터
    private RecommAdapter reAdapter;
    private RecyclerView reRecyclerView;


    private String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mTextViewResult = (TextView) findViewById(R.id.textView_main_result);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView_main_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mEditTextSearchKeyword = (EditText) findViewById(R.id.editText_main_searchKeyword);
        ImageButton button_search = (ImageButton) findViewById(R.id.button_main_search);

        reRecyclerView = (RecyclerView) findViewById(R.id.recyclerView14);
        reRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));         //리사이클러뷰 가로


        btn_shop = findViewById(R.id.btn_shop);
        btn_home = findViewById(R.id.btn_home);
        btn_mypage = findViewById(R.id.btn_mypage);
        btn_add3 = findViewById(R.id.btn_add3);

        USER_ID = SharedPreference.getUserID(SearchActivity.this);
        ORDER_ID = SharedPreference.getOrderID(SearchActivity.this);

        //메인화면에서 검색
        Intent intent = getIntent();
        String stringData = intent.getStringExtra("search_data");
        showResult2(stringData);
        showResult3(stringData);

        //장바구니 버튼을 클릭 시 수행
        btn_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, ShopActivity2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        //홈 버튼을 클릭 시 수행
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
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
                if(SharedPreference.getUserName(SearchActivity.this).length() == 0) {

                    Intent intent = new Intent(SearchActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                } else {

                    Intent intent = new Intent(SearchActivity.this, MypageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("STD_NUM", SharedPreference.getUserName(SearchActivity.this).toString());
                    startActivity(intent);
                    finish();

                }
            }
        });

        //검색 버튼을 클릭 시 수행
        button_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mArrayList.clear();
                GetData task = new GetData();
                task.execute(mEditTextSearchKeyword.getText().toString());
                GetData2 task2 = new GetData2();
                task2.execute(mEditTextSearchKeyword.getText().toString());

            }
        });

        mArrayList = new ArrayList<>();         //Personal객체를 담을 array리스트(어댑터 쪽으로 날릴거임)
        reArrayList = new ArrayList<>();

    }

    void showResult2(String stringData) {           //메인화면에서 검색 결과 수행
        GetData task2 = new GetData();
        task2.execute(stringData);
    }

    void showResult3(String stringData2) {          //메인화면에서 검색 결과 수행
        GetData2 task2 = new GetData2();
        task2.execute(stringData2);
    }

    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SearchActivity.this,
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
        protected String doInBackground(String... params) {         //php파일 실행

            String searchKeyword = params[0];

            String serverURL = "http://3.37.3.112/Search.php";
            String postParameters = "&Name=" + searchKeyword;


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

    private class GetData2 extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SearchActivity.this,
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
                showResult4();

            }
        }


        @Override
        protected String doInBackground(String... params) {         //php파일 실행

            String searchKeyword = params[0];

            String serverURL = "http://3.37.3.112/Recommend.php";
            String postParameters = "&Name=" + searchKeyword;


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

    private void showResult() {         //DB에서 상세정보 다 가져옴

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);        //json파일 받아옴
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);        //json파일의 result array 가져옴

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
                mArrayList.clear();

                mArrayList.add(personalData);       //담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비

                btn_add3.setOnClickListener(new View.OnClickListener() {            //상세정보페이지에서 찜-> 상품이름정보 체크리스트 액티비티로 보냄 DB의 CHECKLIST에 넣음
                    @Override
                    public void onClick(View v) {

                        String productName = Name;
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                //response: 체크리스트 요청을 한 뒤 결과값(Success)을 jsonObject로 받음
                                try {

                                    JSONObject jsonObject = new JSONObject(response);

                                } catch (JSONException e) {

                                    e.printStackTrace(); //예외처리들을 가져옴

                                }
                            }
                        };

                        //서버로 Volley를 이용해서 요청을 함
                        CheckRequest checkRequest = new CheckRequest(USER_ID, ORDER_ID, Name, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(SearchActivity.this);
                        queue.add(checkRequest);

                        Intent intent = new Intent(SearchActivity.this, CheckActivity.class);
                        startActivity(intent);

                    }
                });
            }
            mAdapter = new UsersAdapter(this, mArrayList);
            mRecyclerView.setAdapter(mAdapter);

        } catch (JSONException /*| MalformedURLException*/ e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    private void showResult4() {        //DB에서 상세정보 다 가져옴

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);        //json파일 받아옴
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);        //json파일의 result array 가져옴
            reArrayList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {

                //한 딕셔너리를 가져옴-> 하나의 오브젝트에 상품하나의 상세정보 저장
                JSONObject item = jsonArray.getJSONObject(i);

                String Name = item.getString(TAG_RNAME);
                String Image = item.getString(TAG_IMAGE);

                //만들어뒀던 Personal객체에 데이터를 담는다
                RecommData recommData = new RecommData();

                recommData.setName(Name);
                recommData.setImage(Image);

                reArrayList.add(recommData);        //담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
            }
            reAdapter = new RecommAdapter(this, reArrayList);
            reRecyclerView.setAdapter(reAdapter);

        } catch (JSONException /*| MalformedURLException*/ e) {

            Log.d(TAG, "showResult : ", e);
        }


    }
}
