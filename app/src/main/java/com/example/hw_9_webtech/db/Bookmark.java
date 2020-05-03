package com.example.hw_9_webtech.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.hw_9_webtech.NewsArticle;

@Entity(tableName="bookmarks")
public class Bookmark {

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public NewsArticle getArticle() {
        return this.article;
    }

    @PrimaryKey
    @NonNull
    private String id;

    @NonNull
    @ColumnInfo(name = "bookmark")
    private NewsArticle article;

    public Bookmark(String id, NewsArticle article){
        this.id = id;
        this.article = article;
    }

}
