package com.example.hw_9_webtech.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
    private Context parentContext;

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

    @Override
    public int getItemCount(){
        return myNews.size();
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_card, viewGroup, false);
        parentContext = viewGroup.getContext();
        return new ArticleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ArticleViewHolder articleViewHolder, final int i){
        articleViewHolder.t.setText(myNews.get(i).getTitle());
        Picasso.with(articleViewHolder.im.getContext())
                .load(myNews.get(i).getImgURL())
                .fit()
                .into(articleViewHolder.im);
        articleViewHolder.bm.setImageResource(R.drawable.ic_not_bookmarked); // Haven't checked if bookmarked or not
        articleViewHolder.bm.setContentDescription("Not Bookmarked");
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
                ((ImageView) myDialog.findViewById(R.id.dialog_twitter_icon))
                        .setImageResource(R.drawable.ic_twitter);
                ((ImageView) myDialog.findViewById(R.id.dialog_bookmark_icon))
                        .setImageResource(R.drawable.ic_not_bookmarked);
                myDialog.show();
                return true;
            }
        });
    }
}
