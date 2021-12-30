package knormal.justcart.JustCart_ver4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.JustCart_ver4.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText et_id, et_pass;
    private ImageButton btn_login, btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        //회원가입 버튼을 클릭 시 수행
        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        //로그인 버튼을 클릭 시 수행
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //EditText에 현재 입력되어있는 값을 get해옴(문자열 형태로)
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");         //success가 진짜 success냐, 서버통신 성공여부 알려줌
                            if(success){        //로그인에 성공한 경우

                                String userID = jsonObject.getString("userID");
                                String userPass = jsonObject.getString("userPassword");
                                String userName = jsonObject.getString( "userName" );
                                String userEmail = jsonObject.getString( "userEmail" );

                                Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다!", Toast.LENGTH_SHORT).show();         //서버통신 성공하면 짧은 시간동안 문장 보여줌
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);         //출발Activity->로그인Activity로 이동
                                intent.putExtra("userID",userID);
                                intent.putExtra("userPass",userPass);
                                intent.putExtra( "userName", userName );
                                intent.putExtra( "userEmail", userEmail );
                                SharedPreference.setUserID(LoginActivity.this, intent.getStringExtra("userID"));
                                SharedPreference.setUserPass(LoginActivity.this, intent.getStringExtra("userPass"));
                                SharedPreference.setUserName(LoginActivity.this, intent.getStringExtra("userName"));
                                SharedPreference.setUserEmail(LoginActivity.this, intent.getStringExtra("userEmail"));
                                startActivity(intent);


                            } else{         //로그인에 실패한 경우
                                Toast.makeText(getApplicationContext(),"로그인에 실패하였습니다!", Toast.LENGTH_SHORT).show();
                                return;


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();        //예외처리
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID, userPass, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);

            }
        });
    }
}