package knormal.justcart.JustCart_ver4;

//서버와 통신하는 request파일

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CheckRequest extends StringRequest{
    //서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://3.37.3.112/Check.php";
    private Map<String, String> map; //해쉬맵


    public CheckRequest(String userID, String order_id, String productName, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null); //POST방식: 서버전송방식 중 하나

        map=new HashMap<>();
        map.put("userID",userID);
        map.put("order_id",order_id);
        map.put("productName",productName);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
