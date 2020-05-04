package com.example.hw_9_webtech.ui.bookmark;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hw_9_webtech.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BookmarkFragment extends Fragment {

    private GridView gridView;
    private List<String> all_bookmarked;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookmark, container, false);

        String[] t_list = new String[] {"one", "two", "three", "four", "I am just trying to test one really long text vs some really short text", "six"};
        all_bookmarked = new ArrayList<>(Arrays.asList(t_list));

        gridView = root.findViewById(R.id.bookmark_list);
        gridView.setAdapter(
                new ArrayAdapter<>(
                        Objects.requireNonNull(getActivity()),
                        android.R.layout.simple_list_item_1,
                        all_bookmarked
                )
        );

        return root;
    }
}
