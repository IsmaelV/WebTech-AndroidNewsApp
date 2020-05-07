package com.example.hw_9_webtech.ui.bookmark;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw_9_webtech.NewsArticle;
import com.example.hw_9_webtech.R;
import com.example.hw_9_webtech.ui.RVCardAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class BookmarkFragment extends Fragment {

    private List<NewsArticle> all_bookmarked;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookmark, container, false);

        all_bookmarked = new ArrayList<>();
        populateBookmarks(root);
        TextView noBookmarks = root.findViewById(R.id.no_bookmarks);

        RecyclerView recyclerView = root.findViewById(R.id.bookmark_list);
        GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
        RVCardAdapter myRVAdapter = new RVCardAdapter(all_bookmarked, true, root);
        recyclerView.setAdapter(myRVAdapter);
        recyclerView.setLayoutManager(glm);

        return root;
    }

    private void populateBookmarks(View root){
        SharedPreferences pref = getContext().getSharedPreferences(root.getResources().getString(R.string.bookmark_pref), 0);
        try {
            if (pref.contains(getResources().getString(R.string.all_bookmarked))) {
                Set<String> myBookmarks = pref.getStringSet(getResources().getString(R.string.all_bookmarked), null);
                if (myBookmarks.size() >= 1) {
                    for(String key : myBookmarks){
                        NewsArticle tmpArticle = new NewsArticle(Objects.requireNonNull(pref.getString(key, null)));
                        all_bookmarked.add(tmpArticle);
                    }
                }
            }
        }
        catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }
}
