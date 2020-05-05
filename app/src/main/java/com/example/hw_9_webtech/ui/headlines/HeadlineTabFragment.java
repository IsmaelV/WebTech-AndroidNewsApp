package com.example.hw_9_webtech.ui.headlines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.hw_9_webtech.R;

public class HeadlineTabFragment extends Fragment {

    private String urlCall;
    private String section;

    static Fragment getInstance(int position, String s){
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        String urlToSave = "https://ivillega-nytimes-guardian.wl.r.appspot.com/guardian/" + s;
        bundle.putString("sectionToShow", s);
        bundle.putString("urlToShow", urlToSave);
        HeadlineTabFragment headlineTabFragment = new HeadlineTabFragment();
        headlineTabFragment.setArguments(bundle);
        return headlineTabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        section = getArguments().getString("sectionToShow");
        urlCall = getArguments().getString("urlToShow");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_tab, container, false);

        TextView textView = root.findViewById(R.id.tab_text_view);

        String text_to_set = section + "\n\n" + urlCall;
        textView.setText(text_to_set);

        return root;
    }

    private static String getSection(int pos){
        String result = "error";

        if (pos == 0){
            result = "world";
        }
        else if(pos == 1){
            result = "politics";
        }
        else if(pos == 2){
            result = "business";
        }
        else if(pos == 3){
            result = "technology";
        }
        else if(pos == 4){
            result = "sports";
        }
        return result;
    }
}
