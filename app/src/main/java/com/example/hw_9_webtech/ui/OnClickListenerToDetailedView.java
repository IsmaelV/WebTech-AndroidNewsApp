package com.example.hw_9_webtech.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.hw_9_webtech.DetailedArticleActivity;

public class OnClickListenerToDetailedView implements View.OnClickListener {
    private String id;
    private Context parent;

    public OnClickListenerToDetailedView(Context parentContext, String article_id){
        id = article_id;
        parent = parentContext;
    }

    @Override
    public void onClick(View view){
        Intent intent = new Intent(parent, DetailedArticleActivity.class);
        intent.putExtra("articleID", id);
        parent.startActivity(intent);
    }
}
