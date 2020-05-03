package com.example.hw_9_webtech.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hw_9_webtech.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {

    ListView listView;
    List<String> dataPoints;
    SwipeRefreshLayout r;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        r = root.findViewById(R.id.refresher);
        listView = (ListView) root.findViewById(R.id.yoink_list);
        dataPoints = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.yoink)));
        listView.setAdapter(
                new ArrayAdapter(
                        getActivity(),
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

    public void shuffle(){
        Collections.shuffle(dataPoints, new Random(System.currentTimeMillis()));
        listView.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, dataPoints));
    }
}
