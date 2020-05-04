package com.example.hw_9_webtech.ui.trending;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TrendingFragment extends Fragment {

    private List<Entry> entryList;
    private LineData myLineData;
    private LineChart lineChart;
    private RequestQueue my_queue;
    private JSONArray all_weeks;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trending, container, false);
        my_queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        lineChart = root.findViewById(R.id.trending_chart);
//        Legend leg = lineChart.getLegend();  // Set Legend Later
//        leg.setDrawInside(true);
//        leg.setFormSize(10f);
//        leg.setForm(Legend.LegendForm.SQUARE);
//        leg.setXEntrySpace(5f);
//        leg.setYEntrySpace(5f);
        entryList = new ArrayList<>();

        final EditText editText = root.findViewById(R.id.trending_query);
        editText.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    getChartInfo(editText.getText().toString());
                    return true;
                }
                return false;
            }
        });

        String init_query = editText.getText().toString();
        if(!init_query.equals("")){
            getChartInfo(init_query);
        }
        else{
            getChartInfo("Coronavirus");
        }

        return root;
    }

    private LineData retrieveTrendingData(String q){
        LineDataSet lineDataSet = new LineDataSet(entryList, q);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        List<ILineDataSet> finalDataSet = new ArrayList<>();
        finalDataSet.add(lineDataSet);
        return new LineData(finalDataSet);
    }

    private void getChartInfo(final String q) {
        entryList.clear();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                "https://ivillega-nytimes-guardian.wl.r.appspot.com/google/interest_over_time/" + q,
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
                            myLineData = retrieveTrendingData(q);
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
