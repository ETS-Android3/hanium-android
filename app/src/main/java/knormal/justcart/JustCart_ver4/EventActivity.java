package knormal.justcart.JustCart_ver4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.JustCart_ver4.R;

public class EventActivity extends AppCompatActivity {

    private ImageButton btn_shop, btn_home, btn_mypage;

    private ImageButton btn_event1, btn_event2, btn_event3;
    public static Activity eventActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        eventActivity = EventActivity.this;

        btn_shop = findViewById(R.id.btn_shop3);
        btn_home = findViewById(R.id.btn_home3);
        btn_mypage = findViewById(R.id.btn_mypage3);

        btn_event1 = findViewById(R.id.btn_event1);
        btn_event2 = findViewById(R.id.btn_event2);
        btn_event3 = findViewById(R.id.btn_event3);

        btn_event1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, Frag_event1Activity.class);
                startActivity(intent);
            }
        });

        btn_event2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, Frag_event2Activity.class);
                startActivity(intent);
            }
        });

        btn_event3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, Frag_event3Activity.class);
                startActivity(intent);
            }
        });

        //장바구니 버튼을 클릭 시 수행
        btn_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, ShopActivity2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        //홈 버튼을 클릭 시 수행
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, MainActivity.class);
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
                if(SharedPreference.getUserName(EventActivity.this).length() == 0) {
                    Intent intent = new Intent(EventActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(EventActivity.this, MypageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("STD_NUM", SharedPreference.getUserName(EventActivity.this).toString());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}