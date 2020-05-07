package com.example.hw_9_webtech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailedArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_article);
        invalidateOptionsMenu();  // Works alongside onCreateOptionsMenu to make custom menu

        Intent intent = getIntent();
        String articleID = intent.getExtras().getString("articleID", null);

        ((TextView) findViewById(R.id.articleID)).setText(articleID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.top_detailed_article_menu, menu);
        return true;
    }

    public void finishArticle(MenuItem item) {
        this.finish();
    }
}
