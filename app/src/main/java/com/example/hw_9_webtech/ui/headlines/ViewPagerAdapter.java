package com.example.hw_9_webtech.ui.headlines;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private String[] title = {"World", "Politics", "Business", "Technology", "Sports"};

    ViewPagerAdapter(FragmentManager manager){
        super(manager);
    }

    @Override public Fragment getItem(int position){
        return HeadlineTabFragment.getInstance(position);
    }

    @Override
    public int getCount(){
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return title[position];
    }
}
