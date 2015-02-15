package com.codepath.gridimagesearch.net;

import android.util.Log;

import com.codepath.gridimagesearch.helpers.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by vibhalaljani on 2/11/15.
 *
 * This is a static client for the Google Image API
 */
public class GoogleImgSearchRestClient {
    private static final String BASE_URL = "https://ajax.googleapis.com/ajax/services/" +
            "search/images?v=1.0&rsz=" + Constants.RESULT_SIZE;


    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Log.i("url", "url is " + getAbsoluteUrl(url));
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
