package com.example.hw_9_webtech.ui.headlines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class HeadlineTabFragment extends Fragment {

    private String urlCall;
    private RequestQueue myQueue;
    private RVCardAdapter myRVAdapter;
    private JSONArray results;
    private List<NewsArticle> all_news;
    private SwipeRefreshLayout r;

    static Fragment getInstance(int position, String s){
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        String urlToSave = "https://ivillega-nytimes-guardian.wl.r.appspot.com/guardian/" + s;
        bundle.putString("urlToShow", urlToSave);
        HeadlineTabFragment headlineTabFragment = new HeadlineTabFragment();
        headlineTabFragment.setArguments(bundle);
        return headlineTabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        urlCall = Objects.requireNonNull(getArguments()).getString("urlToShow");
        myQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        all_news = new ArrayList<>();
        jsonParse();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_tab, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.headline_list);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        myRVAdapter = new RVCardAdapter(all_news);
        recyclerView.setAdapter(myRVAdapter);
        recyclerView.setLayoutManager(llm);

        r = root.findViewById(R.id.tab_refresher);
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
        all_news.clear();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                urlCall,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            results = response.getJSONObject("response")
                                    .getJSONArray("results");

                            for(int i = 0; i < results.length(); i++){
                                NewsArticle singleArticle = new NewsArticle(results.getJSONObject(i), "normal");
                                all_news.add(singleArticle);
                            }
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
