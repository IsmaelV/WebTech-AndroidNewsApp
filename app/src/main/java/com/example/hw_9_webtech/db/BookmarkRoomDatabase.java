package com.example.hw_9_webtech.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Bookmark.class, version = 1)
public abstract class BookmarkRoomDatabase extends RoomDatabase {

    public abstract BookmarkDao bookmarkDao();

    private static volatile BookmarkRoomDatabase bookmarkRoomInstance;

    static BookmarkRoomDatabase getDatabase(final Context context){
        if(bookmarkRoomInstance == null){
            synchronized (BookmarkRoomDatabase.class){
                if (bookmarkRoomInstance == null){
                    bookmarkRoomInstance = Room.databaseBuilder(context.getApplicationContext(),
                            BookmarkRoomDatabase.class, "bookmark_database")
                            .build();
                }
            }
        }
        return bookmarkRoomInstance;
    }
}
