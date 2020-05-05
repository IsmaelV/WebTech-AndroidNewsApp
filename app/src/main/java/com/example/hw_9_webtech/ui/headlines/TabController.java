package com.example.hw_9_webtech.ui.headlines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.hw_9_webtech.R;
import com.google.android.material.tabs.TabLayout;


public class TabController extends Fragment {

    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragments_headlines, container, false);
        tabLayout = root.findViewById(R.id.all_tabs);
        viewPager = root.findViewById(R.id.view_pager);

        adapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        addTabs();
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                viewPager.setCurrentItem(tab.getPosition());
                getChildFragmentManager().beginTransaction().addToBackStack(null).commit();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab){
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab){
            }
        });

        return root;
    }

    private void addTabs(){
        tabLayout.addTab(tabLayout.newTab().setText(R.string.world));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.politics));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.business));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.sports));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.technology));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.science));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
    }
}
