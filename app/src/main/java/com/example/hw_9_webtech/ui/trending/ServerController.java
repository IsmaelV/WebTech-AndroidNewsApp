package com.example.hw_9_webtech.ui.trending;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerController {

    public void parseDataForVolleyRequest(RequestQueue myQueue, String q, final TrendingServerCallback callback){
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                "https://ivillega-nytimes-guardian.wl.r.appspot.com/google/interest_over_time/" + q,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccessTrending(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onErrorTrending(error);
                    }
                });
        myQueue.add(request);
    }
}
