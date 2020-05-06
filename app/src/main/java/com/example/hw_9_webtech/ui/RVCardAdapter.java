package com.example.hw_9_webtech.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw_9_webtech.NewsArticle;
import com.example.hw_9_webtech.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Great GREAT resource for RecyclerView:
// https://code.tutsplus.com/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465

public class RVCardAdapter extends RecyclerView.Adapter<RVCardAdapter.ArticleViewHolder>{
    private List<NewsArticle> myNews;
    private Context parentContext;
    private SharedPreferences pref;
    private boolean gridView = false;

    static class ArticleViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView t, ds;
        ImageView im, bm;

        ArticleViewHolder(View itemView){
            super(itemView);
            cv = itemView.findViewById(R.id.home_card_wrapper);
            t = itemView.findViewById(R.id.cardTitleText);
            im = itemView.findViewById(R.id.cardImgURL);
            bm = itemView.findViewById(R.id.bookmark_icon);
            ds = itemView.findViewById(R.id.cardDateSection);
        }
    }

    public RVCardAdapter(List<NewsArticle> myNews){
        this.myNews = myNews;
    }
    public RVCardAdapter(List<NewsArticle> myNews, boolean grid){
        this.myNews = myNews;
        this.gridView = grid;
    }

    @Override
    public int getItemCount(){
        return myNews.size();
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View v;
        if(gridView){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_card_bookmarked, viewGroup, false);
        }
        else {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_card, viewGroup, false);
        }
        parentContext = viewGroup.getContext();
        pref = parentContext.getSharedPreferences(parentContext.getResources().getString(R.string.bookmark_pref), 0);
        return new ArticleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ArticleViewHolder articleViewHolder, final int i){
        articleViewHolder.t.setText(myNews.get(i).getTitle());
        Picasso.with(articleViewHolder.im.getContext())
                .load(myNews.get(i).getImgURL())
                .fit()
                .into(articleViewHolder.im);
        if(pref.contains(myNews.get(i).getArticleID())){
            articleViewHolder.bm.setImageResource(R.drawable.ic_bookmarked);
            articleViewHolder.bm.setContentDescription("Bookmarked");
        }
        else{
            articleViewHolder.bm.setImageResource(R.drawable.ic_not_bookmarked);
            articleViewHolder.bm.setContentDescription("Not Bookmarked");
        }
        articleViewHolder.bm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = pref.edit();
                Toast toast;
                String toToast;
                if(pref.contains(myNews.get(i).getArticleID())){
                    editor.remove(myNews.get(i).getArticleID());
                    Set<String> allArticles = pref.getStringSet(parentContext.getResources().getString(R.string.all_bookmarked), new HashSet<String>());
                    allArticles.remove(myNews.get(i).getArticleID());
                    editor.putStringSet(parentContext.getResources().getString(R.string.all_bookmarked), allArticles);
                    articleViewHolder.bm.setImageResource(R.drawable.ic_not_bookmarked);
                    toToast = "\"" + myNews.get(i).getTitle() + "\" was removed from Bookmarks";
                    if(gridView){
                        myNews.remove(i);
                        notifyItemRemoved(i);
                        notifyItemRangeChanged(i, myNews.size());
                    }
                }
                else{
                    editor.putString(myNews.get(i).getArticleID(), myNews.get(i).toString());
                    Set<String> allArticles = pref.getStringSet(parentContext.getResources().getString(R.string.all_bookmarked), new HashSet<String>());
                    allArticles.add(myNews.get(i).getArticleID());
                    editor.putStringSet(parentContext.getResources().getString(R.string.all_bookmarked), allArticles);
                    articleViewHolder.bm.setImageResource(R.drawable.ic_bookmarked);
                    toToast = "\"" + myNews.get(i).getTitle() + "\" was added to Bookmarks";
                }
                editor.apply();
                toast = Toast.makeText(parentContext, toToast, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        articleViewHolder.im.setContentDescription(articleViewHolder.t.toString());
        SimpleDateFormat sdfENG = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        sdfENG.setTimeZone(TimeZone.GMT_ZONE);
        Date myDateENG = new Date();
        try {
            myDateENG = sdfENG.parse(myNews.get(i).getDate());
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        CharSequence myStringDate = DateUtils.getRelativeTimeSpanString(myDateENG.getTime());
        String myDateSection = myStringDate.toString() + " | " + myNews.get(i).getSection();
        articleViewHolder.ds.setText(myDateSection);

        articleViewHolder.cv.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                final Dialog myDialog = new Dialog(parentContext);
                myDialog.setContentView(R.layout.fragment_popup_dialog);
                myDialog.setCancelable(true);
                ImageView dialogImage = myDialog.findViewById(R.id.dialog_image);
                Picasso.with(dialogImage.getContext())
                        .load(myNews.get(i).getImgURL())
                        .fit()
                        .into(dialogImage);
                TextView dialog_title = myDialog.findViewById(R.id.dialog_title);
                dialog_title.setText(myNews.get(i).getTitle());
                ImageButton twitterButton = myDialog.findViewById(R.id.dialog_twitter_icon);
                twitterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://www.twitter.com/intent/tweet?url=" + myNews.get(i).getWebURL()+"&hashtags=CSCI_571_NewsApp"));
                        parentContext.startActivity(intent);
                    }
                });
                final ImageButton bookmarkButton = myDialog.findViewById(R.id.dialog_bookmark_icon);
                if (pref.contains(myNews.get(i).getArticleID())){
                    bookmarkButton.setImageResource(R.drawable.ic_bookmarked);
                }
                else{
                    bookmarkButton.setImageResource(R.drawable.ic_not_bookmarked);
                }
                bookmarkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = pref.edit();
                        Toast toast;
                        String toToast;
                        if(pref.contains(myNews.get(i).getArticleID())){
                            editor.remove(myNews.get(i).getArticleID());
                            Set<String> allArticles = pref.getStringSet(parentContext.getResources().getString(R.string.all_bookmarked), new HashSet<String>());
                            allArticles.remove(myNews.get(i).getArticleID());
                            editor.putStringSet(parentContext.getResources().getString(R.string.all_bookmarked), allArticles);
                            articleViewHolder.bm.setImageResource(R.drawable.ic_not_bookmarked);
                            bookmarkButton.setImageResource(R.drawable.ic_not_bookmarked);
                            toToast = "\"" + myNews.get(i).getTitle() + "\" was removed from Bookmarks";
                            if(gridView){
                                myNews.remove(i);
                                notifyItemRemoved(i);
                                notifyItemRangeChanged(i, myNews.size());
                                myDialog.dismiss();
                            }
                        }
                        else{
                            editor.putString(myNews.get(i).getArticleID(), myNews.get(i).toString());
                            Set<String> allArticles = pref.getStringSet(parentContext.getResources().getString(R.string.all_bookmarked), new HashSet<String>());
                            allArticles.add(myNews.get(i).getArticleID());
                            editor.putStringSet(parentContext.getResources().getString(R.string.all_bookmarked), allArticles);
                            articleViewHolder.bm.setImageResource(R.drawable.ic_bookmarked);
                            bookmarkButton.setImageResource(R.drawable.ic_bookmarked);
                            toToast = "\"" + myNews.get(i).getTitle() + "\" was added to Bookmarks";
                        }
                        editor.apply();
                        toast = Toast.makeText(parentContext, toToast, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                myDialog.show();
                return true;
            }
        });
    }
}
