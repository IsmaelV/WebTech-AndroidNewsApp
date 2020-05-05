package com.example.hw_9_webtech.ui.trending;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw_9_webtech.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TrendingFragment extends Fragment {

    private List<Entry> entryList;
    private LineData myLineData;
    private LineChart lineChart;
    private RequestQueue my_queue;
    private JSONArray all_weeks;
    private String query;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trending, container, false);
        my_queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        lineChart = root.findViewById(R.id.trending_chart);
        Legend leg = lineChart.getLegend();
        leg.setDrawInside(true);
        leg.setFormSize(15f);
        leg.setTextSize(15f);
        leg.setForm(Legend.LegendForm.SQUARE);
        entryList = new ArrayList<>();

        final EditText editText = root.findViewById(R.id.trending_query);
        editText.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    query = editText.getText().toString();
                    if(query.equals("")){ query = "Coronavirus"; }
                    getChartInfo();
                    return true;
                }
                return false;
            }
        });

        query = editText.getText().toString();
        if(query.equals("")){ query = "Coronavirus"; }
        getChartInfo();

        return root;
    }

    private LineData retrieveTrendingData(){
        LineDataSet lineDataSet = new LineDataSet(entryList, "Trending Chart for " + query);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        int color = ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimary);
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);

        List<ILineDataSet> finalDataSet = new ArrayList<>();
        finalDataSet.add(lineDataSet);
        return new LineData(finalDataSet);
    }

    private void getChartInfo() {
        entryList.clear();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                "https://ivillega-nytimes-guardian.wl.r.appspot.com/google/interest_over_time/" + query,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            all_weeks = response.getJSONObject("default")
                                    .getJSONArray("timelineData");
                            for(int i = 0; i < all_weeks.length(); i++){
                                entryList.add(
                                        new Entry(
                                                i,
                                                all_weeks.getJSONObject(i).getJSONArray("value").getInt(0)
                                        )
                                );
                            }
                            Collections.sort(entryList, new EntryXComparator());
                            myLineData = retrieveTrendingData();
                            lineChart.setData(myLineData);
                            lineChart.invalidate();  // refresh
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
        my_queue.add(request);
    }
}
