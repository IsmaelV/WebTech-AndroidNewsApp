package com.example.hw_9_webtech.ui.trending;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface TrendingServerCallback {
    void onSuccessTrending(JSONObject result);
    void onErrorTrending(VolleyError err);
}
