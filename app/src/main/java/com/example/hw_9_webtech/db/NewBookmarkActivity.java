package com.example.hw_9_webtech.db;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NewBookmarkActivity extends AppCompatActivity {

    public static final String BOOKMARK_ADDED = "new_bookmark";
    public static final String BOOKMARK_REMOVED = "old_bookmark";

    private EditText etNewArticle;  // Change this into a news article or news article container

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new);
//        etNewArticle = findViewById(R.id.etNewArticle);  // Grab by news article container
//
//        Button button = findViewById(R.id.bookmarkAdd);  // This would be the bookmark icon I believe
//        button.setOnClickListener(new View.OnClickListener(){  // setOnClickListener to the bookmark icon
//
//            public void onClick(View view){
//                Intent resultIntent = new Intent();
//
//                if (TextUtils.isEmpty(etNewArticle.getText())){  // Not sure if you have to check if empty
//                    setResult(RESULT_CANCELED, resultIntent);
//                }
//                else{
//                    NewsArticle newsArticle = etNewArticle.getText().toString();  // Init NewsArticle (or just grab it if possible)
//                    resultIntent.putExtra(BOOKMARK_ADDED, newsArticle);  // Bookmark the news article
//                    setResult(RESULT_OK, resultIntent);
//                }
//
//                finish();
//            }
//        });
//    }
}
