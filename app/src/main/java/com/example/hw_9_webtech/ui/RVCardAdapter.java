package com.example.hw_9_webtech.ui;

import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw_9_webtech.NewsArticle;
import com.example.hw_9_webtech.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

// Great GREAT resource for RecyclerView:
// https://code.tutsplus.com/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465

public class RVCardAdapter extends RecyclerView.Adapter<RVCardAdapter.ArticleViewHolder>{
    private List<NewsArticle> myNews;

    static class ArticleViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView t, ds;
        ImageView im;

        ArticleViewHolder(View itemView){
            super(itemView);
            cv = itemView.findViewById(R.id.home_card_wrapper);
            t = itemView.findViewById(R.id.cardTitleText);
            im = itemView.findViewById(R.id.cardImgURL);
            ds = itemView.findViewById(R.id.cardDateSection);
        }
    }

    public RVCardAdapter(List<NewsArticle> myNews){
        this.myNews = myNews;
    }

    @Override
    public int getItemCount(){
        return myNews.size();
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_card, viewGroup, false);
        return new ArticleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder articleViewHolder, int i){
        articleViewHolder.t.setText(myNews.get(i).getTitle());
        Picasso.with(articleViewHolder.im.getContext()).load(myNews.get(i).getImgURL()).fit().into(articleViewHolder.im);
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
    }
}
