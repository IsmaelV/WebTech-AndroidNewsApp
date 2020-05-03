package com.example.hw_9_webtech.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

@Dao
public interface BookmarkDao {

    @Insert
    void insert(Bookmark bookmark);

    @Delete
    void delete(Bookmark bookmark);
}
