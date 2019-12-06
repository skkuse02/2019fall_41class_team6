package com.example.shopick;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SignupRequest extends StringRequest{
    final static private String URL = "http://khsung0.dothome.co.kr/test/Signup.php";
    private Map<String, String> parameters;

    public SignupRequest(String userName, String userID, String userPW, String instaID, String instaPW, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("name",userName);
        parameters.put("id", userID);
        parameters.put("pw", userPW);
        parameters.put("instaID", instaID);
        parameters.put("instaPW", instaPW);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
