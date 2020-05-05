package com.example.hw_9_webtech.ui.headlines;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private String[] title = {"World", "Politics", "Business", "Sports", "Technology", "Science"};

    ViewPagerAdapter(FragmentManager manager){
        super(manager);
    }

    @Override
    public Fragment getItem(int position){
        switch(position){
            case 0:
                return HeadlineTabFragment.getInstance(position, "world");
            case 1:
                return HeadlineTabFragment.getInstance(position, "politics");
            case 2:
                return HeadlineTabFragment.getInstance(position, "business");
            case 3:
                return HeadlineTabFragment.getInstance(position, "sports");
            case 4:
                return HeadlineTabFragment.getInstance(position, "technology");
            case 5:
                return HeadlineTabFragment.getInstance(position, "science");
            default:
                return HeadlineTabFragment.getInstance(position, "ERROR");
        }
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
