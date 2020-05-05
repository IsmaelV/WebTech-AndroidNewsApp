package com.example.hw_9_webtech.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw_9_webtech.NewsArticle;
import com.example.hw_9_webtech.R;

import java.util.List;

public class RVCardAdapter extends RecyclerView.Adapter<RVCardAdapter.ArticleViewHolder>{
    private List<NewsArticle> myNews;

    static class ArticleViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView t, im, ds;

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
        articleViewHolder.im.setText(myNews.get(i).getImgURL());
        String myDateSection = myNews.get(i).getDate() + " | " + myNews.get(i).getSection();
        articleViewHolder.ds.setText(myDateSection);
    }
}
