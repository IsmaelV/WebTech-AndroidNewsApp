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

import com.example.hw_9_webtech.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class HomeFragment extends Fragment {

    private ListView listView;
    private List<String> dataPoints;
    private SwipeRefreshLayout r;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        r = root.findViewById(R.id.refresher);
        listView = root.findViewById(R.id.yoink_list);
        dataPoints = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.yoink)));
        listView.setAdapter(
                new ArrayAdapter<>(
                        Objects.requireNonNull(getActivity()),
                        android.R.layout.simple_list_item_1,
                        dataPoints
                )
        );

        r.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shuffle();
                r.setRefreshing(false);
            }
        });

        return root;
    }

    private void shuffle(){
        Collections.shuffle(dataPoints, new Random(System.currentTimeMillis()));
        listView.setAdapter(
                new ArrayAdapter<>(
                        Objects.requireNonNull(getActivity()),
                        android.R.layout.simple_list_item_1,
                        dataPoints
                )
        );
    }
}
