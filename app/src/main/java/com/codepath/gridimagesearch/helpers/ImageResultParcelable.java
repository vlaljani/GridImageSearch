package com.codepath.gridimagesearch.helpers;

import android.os.Parcel;
import android.os.Parcelable;

import com.codepath.gridimagesearch.models.ImageResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vibhalaljani on 2/14/15.
 */
public class ImageResultParcelable implements Parcelable {

    private String url;
    private String tbUrl;
    private int width;
    private int height;
    private String title;
    private int tbHeight;
    private int tbWidth;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getTbUrl() {
        return tbUrl;
    }

    public void setTbUrl(String tbUrl) {
        this.tbUrl = tbUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public int getTbWidth() {
        return tbWidth;
    }

    public void setTbWidth(int tbWidth) {
        this.tbWidth = tbWidth;
    }

    public int getTbHeight() {
        return tbHeight;
    }

    public void setTbHeight(int tbHeight) {
        this.tbHeight = tbHeight;
    }

    public ImageResultParcelable(JSONObject jsonObject) {
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

    private ImageResultParcelable (Parcel in) {
        this.url = in.readString();
        this.tbUrl = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.title = in.readString();
        this.tbHeight = in.readInt();
        this.tbWidth = in.readInt();
    }

    // called upon ImageResult.fromJSONArray(...)
    public static ArrayList<ImageResultParcelable> fromJSONArray(JSONArray jsonArray) {
        ArrayList<ImageResultParcelable> imageResults = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject imageJSON = null;
            try {
                imageJSON = (JSONObject) jsonArray.get(i);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            ImageResultParcelable image = new ImageResultParcelable(imageJSON);
            imageResults.add(image);
        }
        return imageResults;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(tbUrl);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(title);
        dest.writeInt(tbHeight);
        dest.writeInt(tbWidth);
    }

    public int describeContents() {
        return 0;
    }


    public static final Parcelable.Creator<ImageResultParcelable> CREATOR
            = new Parcelable.Creator<ImageResultParcelable>() {

        public ImageResultParcelable createFromParcel(Parcel in) {
            return new ImageResultParcelable(in);
        }

        public ImageResultParcelable[] newArray(int size) {
            return new ImageResultParcelable[size];
        }
    };

}
