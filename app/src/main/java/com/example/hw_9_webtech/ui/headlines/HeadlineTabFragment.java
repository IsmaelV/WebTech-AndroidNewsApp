package com.example.hw_9_webtech.ui.headlines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

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
import java.util.List;
import java.util.Objects;

public class HeadlineTabFragment extends Fragment {

    private String urlCall;
    private RequestQueue mqueue;
    private ArrayAdapter myArrayAdapter;
    private JSONArray results;
    private List<NewsArticle> all_news;
    private List<String> tmp_list;  // Only temporarily used to display text in a list

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
        mqueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        tmp_list = new ArrayList<>();
        all_news = new ArrayList<>();
        jsonParse();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_tab, container, false);

        ListView listView = root.findViewById(R.id.headline_list);
        myArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                android.R.layout.simple_list_item_1,
                tmp_list);
        listView.setAdapter(myArrayAdapter);

        return root;
    }

    private void jsonParse() {
        tmp_list.clear();
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
                                NewsArticle singleArticle = new NewsArticle(results.getJSONObject(i), "home");
                                tmp_list.add(singleArticle.getTitle() + ", " + singleArticle.getSection() + ", " + singleArticle.getDate());
                                all_news.add(singleArticle);
                            }
                            myArrayAdapter.notifyDataSetChanged();
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
