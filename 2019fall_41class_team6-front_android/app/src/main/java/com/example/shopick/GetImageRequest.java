package com.example.shopick;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetImageRequest extends StringRequest {
    final static private String URL = "http://115.145.170.186/GetImage.php";
    private Map<String, String> parameters;

    public GetImageRequest(String tag, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("tag",tag);
    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
