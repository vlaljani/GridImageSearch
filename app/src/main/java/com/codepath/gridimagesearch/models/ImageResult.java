package com.codepath.gridimagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vibhalaljani on 2/11/15.
 *
 * This class is no longer being used because there is a Parcelable version for it
 * because Serializable is not good for performance.
 */
public class ImageResult implements Serializable {
    private String url;
    private String tbUrl;
    private int width;
    private int height;
    private String title;
    private int tbHeight;
    private int tbWidth;

    // called upon new ImageResult(...)
    public ImageResult(JSONObject jsonObject) {
        try {
            this.setHeight(jsonObject.getInt("height"));
            this.setWidth(jsonObject.getInt("width"));
            this.setTbHeight(jsonObject.getInt("tbWidth"));
            this.setTbWidth(jsonObject.getInt("tbHeight"));
            this.setTbUrl(jsonObject.getString("tbUrl"));
            this.setTitle(jsonObject.getString("title"));
            this.setUrl(jsonObject.getString("url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // called upon ImageResult.fromJSONArray(...)
    public static ArrayList<ImageResult> fromJSONArray(JSONArray jsonArray) {
        ArrayList<ImageResult> imageResults = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject imageJSON = null;
            try {
                imageJSON = (JSONObject) jsonArray.get(i);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            ImageResult image = new ImageResult(imageJSON);
            imageResults.add(image);
        }
        return imageResults;
    }

    public int getTbHeight() {
        return tbHeight;
    }

    public void setTbHeight(int tbHeight) {
        this.tbHeight = tbHeight;
    }

    public int getTbWidth() {
        return tbWidth;
    }

    public void setTbWidth(int tbWidth) {
        this.tbWidth = tbWidth;
    }

    public String getTbUrl() {
        return tbUrl;
    }

    public void setTbUrl(String tbUrl) {
        this.tbUrl = tbUrl;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
