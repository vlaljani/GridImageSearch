package com.codepath.gridimagesearch.helpers;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vibhalaljani on 2/14/15.
 *
 * Parcelable for ImageFilters so they can be passed around in intents and bundles
 */
public class ImageFiltersParcelable implements Parcelable {

    private String size;
    private String color;
    private String type;
    private String site;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public ImageFiltersParcelable() {
        size = null;
        color = null;
        type = null;
        site = null;
    }

    public String getQuery() {
        StringBuilder filter_query = new StringBuilder();
        filter_query.append(Constants.emptyStr);
        if (size != null) {
            filter_query.append(Constants.appender).append(Constants.imgSizeKey).append(size);
        }
        if (color != null) {
            filter_query.append(Constants.appender).append(Constants.imgColorKey).append(color);
        }
        if (type != null) {
            filter_query.append(Constants.appender).append(Constants.imgTypeKey).append(type);
        }
        if (site != null) {
            filter_query.append(Constants.appender).append(Constants.imgSiteKey).append(site);
        }
        return filter_query.toString();
    }

    public void copyFilters(ImageFiltersParcelable anotherFilter) {
        this.size = anotherFilter.size;
        this.color = anotherFilter.color;
        this.type = anotherFilter.type;
        this.site = anotherFilter.site;
    }

    public void reset() {
        setSize(null);
        setColor(null);
        setSite(null);
        setType(null);
    }

    private ImageFiltersParcelable(Parcel in) {
        this.size = in.readString();
        this.color = in.readString();
        this.type = in.readString();
        this.site = in.readString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(size);
        dest.writeString(color);
        dest.writeString(type);
        dest.writeString(site);
    }

    public int describeContents() {
        return 0;
    }


    public static final Parcelable.Creator<ImageFiltersParcelable> CREATOR
            = new Parcelable.Creator<ImageFiltersParcelable>() {

        public ImageFiltersParcelable createFromParcel(Parcel in) {
            return new ImageFiltersParcelable(in);
        }

        public ImageFiltersParcelable[] newArray(int size) {
            return new ImageFiltersParcelable[size];
        }
    };

}
