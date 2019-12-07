package com.example.shopick;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetNameRequest extends StringRequest {
    final static private String URL = "http://115.145.170.186/GetName.php";
    private Map<String, String> parameters;

    public GetNameRequest(String id, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("id",id);
    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
