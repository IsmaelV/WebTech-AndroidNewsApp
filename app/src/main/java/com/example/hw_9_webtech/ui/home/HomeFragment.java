package com.example.hw_9_webtech.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw_9_webtech.NewsArticle;
import com.example.hw_9_webtech.R;
import com.example.hw_9_webtech.ui.RVCardAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private SwipeRefreshLayout r;
    private RequestQueue mqueue;
    private JSONArray results;
    private List<NewsArticle> all_news;
    private RVCardAdapter myRVAdapter;
    private RelativeLayout myProgressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        myProgressBar = root.findViewById(R.id.progress_container);

        mqueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        all_news = new ArrayList<>();

        RecyclerView recyclerView = root.findViewById(R.id.home_list);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        myRVAdapter = new RVCardAdapter(all_news);
        recyclerView.setAdapter(myRVAdapter);
        recyclerView.setLayoutManager(llm);

        jsonParse();

        r = root.findViewById(R.id.home_refresher);

        r.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jsonParse();
                r.setRefreshing(false);
            }
        });

        return root;
    }

    private void jsonParse() {
        String apiURL = "https://ivillega-nytimes-guardian.wl.r.appspot.com/guardian/home";
        all_news.clear();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                apiURL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            results = response.getJSONObject("response")
                                    .getJSONArray("results");

                            for(int i = 0; i < results.length(); i++){
                                all_news.add(new NewsArticle(results.getJSONObject(i), "normal"));
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
        mqueue.add(request);
    }
}
