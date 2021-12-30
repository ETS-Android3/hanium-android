package knormal.justcart.JustCart_ver4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;


import androidx.viewpager.widget.ViewPager;

import com.example.JustCart_ver4.R;

public class MainActivity extends Activity {

    private ImageButton btn_shop, btn_home, btn_event, btn_mypage, imageButton, btn_qrcode;
    private ImageButton btn_fruit, btn_snack, btn_noodle, btn_pizza, btn_milk, btn_meat;
    private Button btn_check;

    //ListView 참조변수
    ListView listview;

    EventAdapter eventAdapter;
    ViewPager viewPager;
    EditText editText_main_search;

    public static Activity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = MainActivity.this;

        viewPager = (ViewPager) findViewById(R.id.view);
        eventAdapter = new EventAdapter(this);
        viewPager.setAdapter(eventAdapter);

        btn_shop = findViewById(R.id.btn_shop);
        btn_home = findViewById(R.id.btn_home);
        btn_event = findViewById(R.id.btn_event);
        btn_mypage = findViewById(R.id.btn_mypage);
        imageButton = findViewById(R.id.imageButton);
        btn_check = findViewById(R.id.btn_check);
        btn_qrcode = findViewById(R.id.btn_qrcode);
        btn_snack = findViewById(R.id.btn_snack);
        btn_noodle = findViewById(R.id.btn_noodle);
        btn_fruit = findViewById(R.id.btn_fruit);
        btn_meat = findViewById(R.id.btn_meat);
        btn_pizza = findViewById(R.id.btn_pizza);
        btn_milk = findViewById(R.id.btn_milk);
        editText_main_search = (EditText) findViewById(R.id.editText_main_search);

        //QR 버튼을 클릭 시 수행
        btn_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SharedPreference.getUserName(MainActivity.this).length() == 0) {

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);

                } else {

                    Intent intent = new Intent(MainActivity.this, CreateQR.class);
                    intent.putExtra("STD_NUM", SharedPreference.getUserName(MainActivity.this).toString());
                    startActivity(intent);

                }
            }
        });

        //장바구니 버튼을 클릭 시 수행
        btn_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SharedPreference.getUserName(MainActivity.this).length() == 0) {

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);

                } else {

                    Intent intent = new Intent(MainActivity.this, ShopActivity2.class);
                    intent.putExtra("STD_NUM", SharedPreference.getUserName(MainActivity.this).toString());
                    startActivity(intent);

                }
            }
        });

        //홈 버튼을 클릭 시 수행
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



        //이벤트 버튼을 클릭 시 수행
        btn_event.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(MainActivity.this, EventActivity.class);
                 startActivity(intent);
             }
        });

        //마이페이지 버튼을 클릭 시 수행
        btn_mypage.setOnClickListener(new View.OnClickListener() {
            Intent intent =getIntent();
            @Override
            public void onClick(View v) {
                if(SharedPreference.getUserName(MainActivity.this).length() == 0) {

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);

                } else {

                    Intent intent = new Intent(MainActivity.this, MypageActivity.class);
                    intent.putExtra("STD_NUM", SharedPreference.getUserName(MainActivity.this).toString());
                    startActivity(intent);

                }
            }
        });

        //체크리스트 버튼을 클릭 시 수행
        btn_check.setOnClickListener(new View.OnClickListener() {
            Intent intent =getIntent();
            @Override
            public void onClick(View v) {
                if(SharedPreference.getUserName(MainActivity.this).length() == 0) {

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);

                } else {

                    Intent intent = new Intent(MainActivity.this, CheckActivity.class);
                    intent.putExtra("STD_NUM", SharedPreference.getUserName(MainActivity.this).toString());
                    String checkdata = "checklist";
                    intent.putExtra("checklist_data", checkdata);
                    startActivity(intent);

                }

            }
        });

        //검색 버튼을 클릭 시 수행
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String data = editText_main_search.getText().toString();
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("search_data", data);
                startActivity(intent);

            }
        });

        //스낵 버튼을 클릭 시 수행
        btn_snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "snack";
                Intent intent = new Intent(MainActivity.this, Snack.class);
                intent.putExtra("Category", data);
                startActivity(intent);
            }
        });

        //즉석식품 버튼을 클릭 시 수행
        btn_noodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "noodle";
                Intent intent = new Intent(MainActivity.this, NoodleActivity.class);
                intent.putExtra("Category", data);
                startActivity(intent);
            }
        });

        //냉동식품 버튼을 클릭 시 수행
        btn_pizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "frozenfood";
                Intent intent = new Intent(MainActivity.this, FrozenFood.class);
                intent.putExtra("Category", data);
                startActivity(intent);
            }
        });

        //과일 버튼을 클릭 시 수행
        btn_fruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "fruit";
                Intent intent = new Intent(MainActivity.this, Fruit.class);
                intent.putExtra("Category", data);
                startActivity(intent);
            }
        });

        //고기 버튼을 클릭 시 수행
        btn_meat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "meat";
                Intent intent = new Intent(MainActivity.this, Meat.class);
                intent.putExtra("Category", data);
                startActivity(intent);
            }
        });

        //음료 버튼을 클릭 시 수행
        btn_milk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "drink";
                Intent intent = new Intent(MainActivity.this, Drink.class);
                intent.putExtra("Category", data);
                startActivity(intent);
            }
        });

    }


}


