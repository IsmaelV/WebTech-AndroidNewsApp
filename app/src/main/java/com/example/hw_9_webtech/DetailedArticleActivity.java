package com.example.hw_9_webtech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

public class DetailedArticleActivity extends AppCompatActivity {

    private RelativeLayout myProgressBar;
    private NewsArticle detailedArticle;
    private JSONObject results;
    private RequestQueue myQueue;
    private SharedPreferences pref;

    private ImageView detailedImg;
    private ScrollView myCard;
    private MenuItem bookmark;
    private MenuItem appbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_article);
        invalidateOptionsMenu();  // Works alongside onCreateOptionsMenu to make custom menu

        pref = getSharedPreferences(getResources().getString(R.string.bookmark_pref), 0);

        myCard = findViewById(R.id.card_wrapper);
        myCard.setVisibility(View.GONE);

        Intent intent = getIntent();
        String articleID = intent.getExtras().getString("articleID", null);

        myProgressBar = findViewById(R.id.progress_container);
        myQueue = Volley.newRequestQueue(this);
        detailedImg = findViewById(R.id.detailedImg);
        jsonParse(articleID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.top_detailed_article_menu, menu);
        bookmark = menu.getItem(2);
        appbarTitle = menu.getItem(1);
        return true;
    }

    public void finishArticle(MenuItem item) {
        this.finish();
    }

    public void tweet(MenuItem item) {
        Intent tweetIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com/intent/tweet?url=" + detailedArticle.getWebURL()+"&hashtags=CSCI_571_NewsApp"));
        startActivity(tweetIntent);
    }

    public void jsonParse(String id){
        String encodedURL = "";
        try {
            encodedURL = URLEncoder.encode(id, "utf-8");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                "https://ivillega-nytimes-guardian.wl.r.appspot.com/guardian/article_view/" + encodedURL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            results = response.getJSONObject("response").getJSONObject("content");
                            detailedArticle = new NewsArticle(results);
                            myProgressBar.setVisibility(View.GONE);

                            Picasso.with(detailedImg.getContext())
                                    .load(detailedArticle.getImgURL())
                                    .fit()
                                    .into(detailedImg);
                            detailedImg.setContentDescription(detailedArticle.getTitle());
                            ((TextView) findViewById(R.id.cardDetailedTitleText)).setText(detailedArticle.getTitle());
                            ((TextView) findViewById(R.id.detailedSection)).setText(detailedArticle.getSection());

                            ZonedDateTime myDate = ZonedDateTime.parse(detailedArticle.getDate()).withZoneSameInstant(ZoneId.of("America/Los_Angeles"));
                            String myFinalDate = myDate.getDayOfMonth() + " " + myDate.getMonth().toString() + " " + myDate.getYear();
                            ((TextView) findViewById(R.id.detailedDate)).setText(myFinalDate);

                            Spanned content = HtmlCompat.fromHtml(detailedArticle.getContent(), HtmlCompat.FROM_HTML_MODE_LEGACY);
                            ((TextView) findViewById(R.id.detailedContent)).setText(content);

                            SpannableString fullArticleLink = new SpannableString("View Full Article");
                            fullArticleLink.setSpan(new UnderlineSpan(), 0, fullArticleLink.length(), 0);
                            ((TextView) findViewById(R.id.detailedLink)).setText(fullArticleLink);
                            findViewById(R.id.detailedLink).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent fullArticle = new Intent(Intent.ACTION_VIEW, Uri.parse(detailedArticle.getWebURL()));
                                    startActivity(fullArticle);
                                }
                            });

                            if(pref.contains(detailedArticle.getArticleID())){
                                bookmark.setIcon(R.drawable.ic_bookmarked);
                            }

                            if (detailedArticle.getTitle().length() > 27){
                                appbarTitle.setTitle(detailedArticle.getTitle().subSequence(0, 27).toString().concat("..."));
                            }
                            else {
                                appbarTitle.setTitle(detailedArticle.getTitle());
                            }

                            myCard.setVisibility(View.VISIBLE);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        myQueue.add(request);
    }

    public void toggle_bookmark(MenuItem item) {
        SharedPreferences.Editor editor = pref.edit();
        Toast toast;
        String toToast;
        if(pref.contains(detailedArticle.getArticleID())){
            editor.remove(detailedArticle.getArticleID());
            Set<String> allArticles = pref.getStringSet(getResources().getString(R.string.all_bookmarked), new HashSet<String>());
            allArticles.remove(detailedArticle.getArticleID());
            editor.putStringSet(getResources().getString(R.string.all_bookmarked), allArticles);
            item.setIcon(R.drawable.ic_not_bookmarked);
            toToast = "\"" + detailedArticle.getTitle() + "\" was removed from Bookmarks";
        }
        else{
            editor.putString(detailedArticle.getArticleID(), detailedArticle.toString());
            Set<String> allArticles = pref.getStringSet(getResources().getString(R.string.all_bookmarked), new HashSet<String>());
            allArticles.add(detailedArticle.getArticleID());
            editor.putStringSet(getResources().getString(R.string.all_bookmarked), allArticles);
            item.setIcon(R.drawable.ic_bookmarked);
            toToast = "\"" + detailedArticle.getTitle() + "\" was added to Bookmarks";
        }
        editor.apply();
        toast = Toast.makeText(this, toToast, Toast.LENGTH_SHORT);
        toast.show();
    }

}
