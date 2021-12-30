package knormal.justcart.JustCart_ver4;

//서버와 통신하는 request파일

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    //서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://3.37.3.112/Register.php";
    private Map<String, String> map;        //해쉬맵


    public RegisterRequest(String userID, String userPassword, String userName, String userEmail, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);        //POST방식: 서버전송방식 중 하나

        map=new HashMap<>();
        map.put("userID",userID);
        map.put("userPassword",userPassword);
        map.put("userName",userName);
        map.put("userEmail",userEmail);         //string으로 눈속임
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
