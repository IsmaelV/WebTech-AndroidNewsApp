package com.example.hw_9_webtech.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw_9_webtech.NewsArticle;
import com.example.hw_9_webtech.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class HomeFragment extends Fragment {

    private ListView listView;
    private SwipeRefreshLayout r;
    private RequestQueue mqueue;
    private JSONArray results;
    private List<NewsArticle> all_news;
    private List<String> tmp_list;  // Only temporarily used to display text in a list

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mqueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        tmp_list = new ArrayList<>();
        all_news = new ArrayList<>();
        jsonParse();

        r = root.findViewById(R.id.refresher);
        listView = root.findViewById(R.id.home_list);
        listView.setAdapter(
                new ArrayAdapter<>(
                        Objects.requireNonNull(getActivity()),
                        android.R.layout.simple_list_item_1,
                        tmp_list
                )
        );

        r.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                jsonParse();
                shuffle();
//                listView.setAdapter(
//                        new ArrayAdapter<>(
//                                Objects.requireNonNull(getActivity()),
//                                android.R.layout.simple_list_item_1,
//                                tmp_list
//                        )
//                );
                r.setRefreshing(false);
            }
        });

        return root;
    }

    private void shuffle(){
        Collections.shuffle(tmp_list, new Random(System.currentTimeMillis()));
        listView.setAdapter(
                new ArrayAdapter<>(
                        Objects.requireNonNull(getActivity()),
                        android.R.layout.simple_list_item_1,
                        tmp_list
                )
        );
    }

    private void jsonParse() {
        String apiURL = "https://ivillega-nytimes-guardian.wl.r.appspot.com/guardian/home";
        tmp_list.clear();
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
                                NewsArticle singleArticle = new NewsArticle(results.getJSONObject(i), "home");
                                tmp_list.add(singleArticle.getTitle() + ", " + singleArticle.getSection() + ", " + singleArticle.getDate());
                                all_news.add(singleArticle);
                            }
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
