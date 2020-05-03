package com.example.hw_9_webtech.db;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

public class BookmarkViewModel extends AndroidViewModel {

    private String TAG = this.getClass().getSimpleName();
    private BookmarkDao bookmarkDao;
    private BookmarkRoomDatabase bookmarkDB;

    public BookmarkViewModel(Application application){
        super(application);

        bookmarkDB = BookmarkRoomDatabase.getDatabase(application);
        bookmarkDao = bookmarkDB.bookmarkDao();
    }

    public void insert(Bookmark bookmark){
        new InsertAsyncTask(bookmarkDao).execute(bookmark);
    }

    public void delete(Bookmark bookmark){
        new DeleteAsyncTask(bookmarkDao).execute(bookmark);
    }

    @Override
    protected void onCleared(){
        super.onCleared();
        Log.i(TAG, "ViewModel Destroyed");
    }

    private class InsertAsyncTask extends AsyncTask<Bookmark, Void, Void> {

        BookmarkDao mBookmarkDao;

        public InsertAsyncTask(BookmarkDao mBookmarkDao){
            this.mBookmarkDao = mBookmarkDao;
        }

        @Override
        protected Void doInBackground(Bookmark... bookmarks){
            mBookmarkDao.insert(bookmarks[0]);
            return null;
        }
    }

    private class DeleteAsyncTask extends AsyncTask<Bookmark, Void, Void> {

        BookmarkDao mBookmarkDao;

        public DeleteAsyncTask(BookmarkDao mBookmarkDao){
            this.mBookmarkDao = mBookmarkDao;
        }

        @Override
        protected Void doInBackground(Bookmark... bookmarks){
            mBookmarkDao.delete(bookmarks[0]);
            return null;
        }
    }
}
