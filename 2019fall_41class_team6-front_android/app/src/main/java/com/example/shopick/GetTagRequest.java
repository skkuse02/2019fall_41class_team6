package com.example.shopick;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetTagRequest extends StringRequest {
    final static private String URL = "http://115.145.170.186/GetTag.php";
    private Map<String, String> parameters;

    public GetTagRequest(String userID, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener,null);
        parameters = new HashMap<>();
        parameters.put("id", userID);
        Log.d("GET TAG REQUEST","DOING");
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
