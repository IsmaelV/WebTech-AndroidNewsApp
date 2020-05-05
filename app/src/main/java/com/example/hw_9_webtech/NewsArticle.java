package com.example.hw_9_webtech;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsArticle {
    private String title;
    private String imgURL;
    private String section;
    private String content;
    private String articleID;
    private String webURL;
    private String date;

    public NewsArticle(JSONObject article, String type) {
        if (type.equals("normal")){
            extractHome(article);
        }
        else if(type.equals("detailed")){
            System.out.println("Parse for detailed");
        }
    }

    public NewsArticle(String allInfo){
        String[] sepInfo = allInfo.split("#####");
        title = sepInfo[0];
        imgURL = sepInfo[1];
        section = sepInfo[2];
        content = sepInfo[3];
        articleID = sepInfo[4];
        webURL = sepInfo[5];
        date = sepInfo[6];
    }

    public String toString(){
        return title +
                "#####" + imgURL +
                "#####" + section +
                "#####" + content +
                "#####" + articleID +
                "#####" + webURL +
                "#####" + date;
    }

    private void extractHome(JSONObject article){
        try {
            // Set all values
            title = article.getString("webTitle");
            articleID = article.getString("id");
            webURL = article.getString("webUrl");
            section = article.getString("sectionName");
            date = article.getString("webPublicationDate");
            content = article.getJSONObject("blocks")
                    .getJSONArray("body")
                    .getJSONObject(0)
                    .getString("bodyTextSummary");

            // Search for appropriate image
            boolean imgFound = false;
            JSONArray imgPath;
            try{
                imgPath = article.getJSONObject("blocks")
                        .getJSONObject("main").getJSONArray("elements")
                        .getJSONObject(0).getJSONArray("assets");
            }
            catch(Exception e){
                imgPath = article.getJSONObject("blocks")
                        .getJSONArray("body").getJSONObject(0)
                        .getJSONArray("elements").getJSONObject(0)
                        .getJSONArray("assets");
            }
            for (int j = 0; j < imgPath.length(); j++){
                int w = imgPath.getJSONObject(j).getJSONObject("typeData").getInt("width");
                if (w >= 2000){
                    imgURL = imgPath.getJSONObject(j).getString("file");
                    imgFound = true;
                    break;
                }
            }
            if (!imgFound){
                imgURL = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTitle(){
        return title;
    }

    public String getImgURL(){
        return imgURL;
    }

    public String getSection(){
        return section;
    }

    public String getContent(){
        return content;
    }

    public String getArticleID() {
        return articleID;
    }

    public String getWebURL() {
        return webURL;
    }

    public String getDate() {
        return date;
    }
}
