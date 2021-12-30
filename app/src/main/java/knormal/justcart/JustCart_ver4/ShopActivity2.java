package knormal.justcart.JustCart_ver4;

import android.content.Intent;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.JustCart_ver4.R;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
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
import java.util.Collection;
import java.util.List;

import static java.lang.Thread.sleep;

public class ShopActivity2 extends AppCompatActivity {

    private ImageButton btn_shop, btn_home, btn_mypage;
    private String TAG2 = "ShopActivity";
    public static Activity shopActivity;
    private List<Beacon> beaconList = new ArrayList<>();
    private String USER_ID, ORDER_ID;
    int value = 0;

    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    protected static final String TAG_beacon = "RangingActivity";

    private static String TAG = "phpquerytest";

    private static final String TAG_JSON="webnautes";
    private static final String TAG_productNAME = "productName";
    private static final String TAG_productPrice = "productPrice";
    private static final String TAG_classNum ="classNum";
    private static final String TAG_ENAME ="errordata";


    private TextView mTextViewResult, text_totalprice;
    private ArrayList<PersonalData> mArrayList;
    private ArrayList<RecommData> eArrayList;
    EditText mEditTextSearchKeyword;
    private ShopAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private String mJsonString;
    private String totalprice;

    //자동업데이트
    private CustomRunnable mCustomRunnable;
    boolean isThread = false;
    int noDialog=0;
    Handler handler = null;
    ProgressRunnable runnable1;
    
    //에러 자동업데이트
    @Override
    protected void onCreate(Bundle savedInstanceState) {        //액티비티 생성할 때
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        mCustomRunnable = new CustomRunnable();
        handler = new Handler();
        runnable1 = new ProgressRunnable();
        Thread thread = new Thread(mCustomRunnable);
        thread.setDaemon(true);
        thread.start();

        shopActivity = ShopActivity2.this;
        View view =findViewById(R.id.view1);
        view.setClickable(true);

        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")); //iBeacon의 layout
        Log.i("foxy", "ScanBT - i am in onCreateeeeeeeee");


        USER_ID = SharedPreference.getUserID(ShopActivity2.this);
        ORDER_ID = SharedPreference.getOrderID(ShopActivity2.this);


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
                    Log.d("viewTest", "newXvalue : " + pressedX);       // View 내부에서 터치한 지점의 상대 좌표값.
                }
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    pressedX2 = event.getX();
                    distance = pressedX - pressedX2;        // View 내부에서 터치한 지점의 상대 좌표값.
                    Log.d("viewTest", "Distance : "+ distance);
                }


                // 해당 거리가 100이 되지 않으면 이벤트 처리 하지 않는다.
                if (Math.abs(distance) < 200) {
                    return false;
                }

                if (distance > 0) {
                    // 손가락을 왼쪽으로 움직였으면 오른쪽 화면이 나타나야 한다.
                    Intent intent = new Intent(ShopActivity2.this, CheckActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                } else {
                    // 손가락을 오른쪽으로 움직였으면 왼쪽 화면이 나타나야 한다.
                    Intent intent = new Intent(ShopActivity2.this, CheckActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                }
                finish(); // finish 해주지 않으면 activity가 계속 쌓인다.

                return true;
            }
        });

        btn_shop = findViewById(R.id.btn_shop2);
        btn_home = findViewById(R.id.btn_home2);
        btn_mypage = findViewById(R.id.btn_mypage2);

        //장바구니 버튼을 클릭 시 수행
        btn_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity2.this, ShopActivity2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        //홈 버튼을 클릭 시 수행
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isThread = false;
                Intent intent = new Intent(ShopActivity2.this, MainActivity.class);
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
                if(SharedPreference.getUserName(ShopActivity2.this).length() == 0) {

                    Intent intent = new Intent(ShopActivity2.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                } else {

                    Intent intent = new Intent(ShopActivity2.this, MypageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("STD_NUM", SharedPreference.getUserName(ShopActivity2.this).toString());
                    startActivity(intent);
                    finish();

                }
            }
        });

        mTextViewResult = (TextView) findViewById(R.id.textView13);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView13);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        text_totalprice = (TextView) findViewById(R.id.text_totalprice);

        showResult2(USER_ID, ORDER_ID);
        showResult3(ORDER_ID);

        mArrayList = new ArrayList<>();         //Personal객체를 담을 array리스트(어댑터 쪽으로 날릴거임)
        eArrayList = new ArrayList<>();
    }


    //비콘인식
    @Override
    protected void onResume() {
        super.onResume();

        beaconManager.addRangeNotifier (new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    for (Beacon beacon : beacons) {
                        int minor = beacon.getId3().toInt();            // beacon minor
                        if(value == 0) {
                            if (((Beacon) beacons.iterator().next()).getDistance() < 0.2 && minor == 55155) {           // minor가 55177인 비콘 인식
                                value = 1;
                                Log.i(TAG_beacon, "The first beacon I see is about " + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                Log.i(TAG_beacon, "The first beacon I see is about " + ((Beacon) beacons.iterator().next()).getDistance() + " meters away." + minor);
                                Intent intent = new Intent(ShopActivity2.this, PayActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.i(TAG_beacon, "The no beacon I see is about@@@@@@@@@@@@@ " + ((Beacon) beacons.iterator().next()).getDistance() + " meters away." + minor);
                            }
                        }
                    }
                }
            }

        });
        beaconManager.startRangingBeacons(new Region("CardBeacon", Identifier.parse("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"), Identifier.parse("40010"), Identifier.parse("55155")));
    }

    void showResult3(String stringData) {           //메인화면에서 검색 결과 수행
        GetData2 task3 = new GetData2();
        task3.execute(stringData);
    }

    @Override
    //액티비티 옮겨갈 때 비콘인식 중지
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy(){
        handler.removeCallbacks(mCustomRunnable);
        super.onDestroy();
    }

    class CustomRunnable implements Runnable{
        @Override
        public void run() {
            while(true){
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> info = manager.getRunningTasks(1);
                if(info.get(0).topActivity.getClassName().equals("com.example.JustCart_ver4.ShopActivity2")){
                    handler.post(runnable1);
                }
            }
        }
    }

    public class ProgressRunnable implements Runnable{
        @Override
        public void run() {
            noDialog =1;
            mArrayList.clear();
            GetData task2 = new GetData();
            task2.execute(USER_ID, ORDER_ID);
            GetData2 task3 = new GetData2();
            task3.execute(ORDER_ID);

        }

    }



    void showResult2(String Data1, String Data2) {          //메인화면에서 검색 결과 수행
        noDialog = 0;
        GetData task2 = new GetData();
        task2.execute(Data1, Data2);
    }

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(noDialog ==0){
                progressDialog = ProgressDialog.show(ShopActivity2.this,
                        "", null, false, true);
            }
        }


        @Override
        protected void onPostExecute(String result) {       //에러있는 경우 에러메세지 보여줌/ 아니면 JSON파싱해서 화면에 보여줌
            super.onPostExecute(result);


            if(noDialog==0){
                progressDialog.dismiss();
            }

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

            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];

            String serverURL = "http://3.37.3.112/Load_userbasket.php";
            String postParameters = "&userID=" + searchKeyword1 +"&order_id=" + searchKeyword2;

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

            if(noDialog ==0){

                progressDialog = ProgressDialog.show(ShopActivity2.this,
                        "", null, false, true);

            }
        }


        @Override
        protected void onPostExecute(String result) {       //에러있는 경우 에러메세지 보여줌/ 아니면 JSON파싱해서 화면에 보여줌
            super.onPostExecute(result);

            if(noDialog==0){
                progressDialog.dismiss();
            }

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

            String serverURL = "http://3.37.3.112/Error.php";
            String postParameters = "&order_id=" + searchKeyword;


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
            Integer a=0;
            JSONObject jsonObject = new JSONObject(mJsonString);        //json파일 받아옴
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);        //json파일의 result array 가져옴

            for (int i = 0; i < jsonArray.length(); i++) {

                //한 딕셔너리를 가져옴-> 하나의 오브젝트에 상품하나의 상세정보 저장
                JSONObject item = jsonArray.getJSONObject(i);

                String productName = item.getString(TAG_productNAME);
                String productPrice = item.getString(TAG_productPrice);
                String classNum = item.getString(TAG_classNum);

                a += Integer.parseInt(productPrice,10)*Integer.parseInt(classNum,10);

                //만들어뒀던 Personal객체에 데이터를 담는다
                PersonalData personalData = new PersonalData();

                personalData.setproductName(productName);
                personalData.setproductPrice(productPrice);
                personalData.setclassNum(classNum);

                mArrayList.add(personalData);       //담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
            }
            mAdapter = new ShopAdapter(this, mArrayList);
            mRecyclerView.setAdapter(mAdapter);
            totalprice=String.valueOf(a);
            SharedPreference.setTotalPrice(ShopActivity2.this, totalprice);
            text_totalprice.setText(totalprice);

            //추가
            RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
            if(animator instanceof SimpleItemAnimator){
                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
            }

        } catch (JSONException /*| MalformedURLException*/ e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    private void showResult4() {        //DB에서 상세정보 다 가져옴

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);        //json파일 받아옴
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);        //json파일의 result array 가져옴

            for (int i = 0; i < jsonArray.length(); i++) {

                //한 딕셔너리를 가져옴-> 하나의 오브젝트에 상품하나의 상세정보 저장
                JSONObject item = jsonArray.getJSONObject(i);
                String EName = item.getString(TAG_ENAME);

                if(Integer.parseInt(EName,10) == 1){

                    Toast.makeText(getApplicationContext(),"물건을 다시 인식해주세요", Toast.LENGTH_SHORT).show();

                }

            }
        } catch (JSONException /*| MalformedURLException*/ e) {

            Log.d(TAG, "showResult : ", e);
        }


    }

}
