package com.example.hw_9_webtech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw_9_webtech.ui.RVCardAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SwipeRefreshLayout r;
    private RequestQueue myQueue;
    private JSONArray results;
    private List<NewsArticle> all_news;
    private RVCardAdapter myRVAdapter;
    private RelativeLayout myProgressBar;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        query = getIntent().getExtras().getString("query", null);
        getSupportActionBar().setTitle("Search Results for " + query);

        myProgressBar = findViewById(R.id.progress_container);

        myQueue = Volley.newRequestQueue(this);
        all_news = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.search_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        myRVAdapter = new RVCardAdapter(all_news);
        recyclerView.setAdapter(myRVAdapter);
        recyclerView.setLayoutManager(llm);

        jsonParse();

        r = findViewById(R.id.search_refresher);

        r.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jsonParse();
                r.setRefreshing(false);
            }
        });
    }

    public void jsonParse(){
        all_news.clear();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                "https://ivillega-nytimes-guardian.wl.r.appspot.com/guardian/search/" + query,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            results = response.getJSONObject("response")
                                    .getJSONArray("results");

                            for(int i = 0; i < results.length(); i++){
                                all_news.add(new NewsArticle(results.getJSONObject(i)));
                            }
                            myProgressBar.setVisibility(View.GONE);
                            myRVAdapter.notifyDataSetChanged();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        myQueue.add(request);
    }
}
