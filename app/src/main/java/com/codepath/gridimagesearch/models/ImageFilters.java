package com.codepath.gridimagesearch.models;

import com.codepath.gridimagesearch.helpers.Constants;

import java.io.Serializable;

/**
 * Created by vibhalaljani on 2/14/15.
 */
public class ImageFilters implements Serializable {
    private String size;
    private String color;
    private String type;
    private String site;

    public ImageFilters() {
        size = null;
        color = null;
        type = null;
        site = null;
    }

    public String getQuery() {
        StringBuilder filter_query = new StringBuilder();
        filter_query.append("");
        if (size != null) {
            filter_query.append(Constants.appender + Constants.imgSizeKey + size);
        }
        if (color != null) {
            filter_query.append(Constants.appender + Constants.imgColorKey + color);
        }
        if (type != null) {
            filter_query.append(Constants.appender + Constants.imgTypeKey + type);
        }
        if (site != null) {
            filter_query.append(Constants.appender + Constants.imgSiteKey + site);
        }
        return filter_query.toString();
    }

    public void copyFilters(ImageFilters anotherFilter) {
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
}