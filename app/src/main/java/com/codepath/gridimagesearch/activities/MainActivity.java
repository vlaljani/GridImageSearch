package com.codepath.gridimagesearch.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.gridimagesearch.adapters.ImageResultsAdapter;
import com.codepath.gridimagesearch.helpers.Constants;
import com.codepath.gridimagesearch.helpers.EndlessScrollListener;
import com.codepath.gridimagesearch.helpers.ImageFiltersParcelable;
import com.codepath.gridimagesearch.helpers.ImageResultParcelable;
import com.codepath.gridimagesearch.net.GoogleImgSearchRestClient;
import com.codepath.gridimagesearch.R;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    // Edit: No longer needed because we're using SearchView
    //private EditText etSearchQuery;
    private StaggeredGridView gvImages;
    private ArrayList<ImageResultParcelable> imageResults;
    private ImageResultsAdapter imageResultsAdapter;
    private final int ADV_SEARCH_REQ_CODE = 7;
    private String query = null;
    private ImageFiltersParcelable q_filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        q_filters = new ImageFiltersParcelable();
        setupViews();
    }

    // Method to find the views and set them up
    private void setupViews() {
        // Edit: No longer needed because we're using SearchView
        // etSearchQuery = (EditText) findViewById(R.id.etSearchQuery);
        gvImages = (StaggeredGridView) findViewById(R.id.gvImages);
        imageResults = new ArrayList<ImageResultParcelable>();
        imageResultsAdapter = new ImageResultsAdapter(this, imageResults);
        gvImages.setAdapter(imageResultsAdapter);



        setupViewListeners();
    }

    // Method to setup listeners wherever required
    private void setupViewListeners() {
        // Sets up a listener to call detailed activity when we click an image
        gvImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageResultParcelable curr_image = imageResults.get(position);
                Intent i = new Intent(MainActivity.this, ImageDetailActivity.class);
                i.putExtra("current_image", curr_image);
                startActivity(i);
            }
        });

        // Sets up a listener for endless scrolling
        gvImages.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (page < Constants.RESULT_SIZE) {
                    getSearchImages(query, q_filters, page);
                }
            }
        });

        // to block the user from hitting the enter key in Search because that puts an illegal
        // character in the query
        // Edit: No longer needed because we're using search view, and keycode_enter acts as
        // submit there
        /*etSearchQuery.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return (keyCode == KeyEvent.KEYCODE_ENTER);
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String q) {
                query = q;
                imageResultsAdapter.clear();
                q_filters.reset();
                getSearchImages(query, q_filters, 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // If the settings icon is clicked
        if (id == R.id.search_settings) {
            Intent i = new Intent(MainActivity.this, AdvancedSearchActivity.class);
            i.putExtra("advanced_filters", q_filters);
            startActivityForResult(i, ADV_SEARCH_REQ_CODE);
        }


        return super.onOptionsItemSelected(item);
    }

    // This method determines if the mobile device is connected to the internet
    // Edit: This didn't seem to work very well with the emulator :(
    public Boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -n 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == ADV_SEARCH_REQ_CODE) {
            // Extract advanced filters from result extras
            q_filters.copyFilters((ImageFiltersParcelable) data.getParcelableExtra("advanced_filters"));

            // since this is a new query
            imageResultsAdapter.clear();
            getSearchImages(query, q_filters, 0);
        }
    }

    private void getSearchImages(final String query,
                                 ImageFiltersParcelable curr_filters,
                                 int page) {
        String appender = "&";
        String pageKey = "start=";

        q_filters.copyFilters(curr_filters);

        String queryKey = "q=";

        // Add the query to the page
        StringBuilder urlBuilder = new StringBuilder().append(appender + queryKey + query);
        urlBuilder.append(appender + pageKey + (page * Constants.RESULT_SIZE));

        urlBuilder.append(q_filters.getQuery());

        String url = urlBuilder.toString();

        if (!isNetworkAvailable()) {
            Toast.makeText(this, getResources().getString(R.string.internet_error),
                    Toast.LENGTH_SHORT).show();
        }

        if (isNetworkAvailable()) {
            GoogleImgSearchRestClient.get(url, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray imageResultsJSON = response.getJSONObject("responseData")
                                .getJSONArray("results");

                        // Edit: no longer needed because we're using SearchView
                        /*etSearchQuery.setText("");
                        etSearchQuery.append(query);
                        etSearchQuery.requestFocus();*/

                        imageResultsAdapter.addAll(ImageResultParcelable.fromJSONArray(imageResultsJSON));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject e) {
                    Log.e("ERROR", e.toString());

                    Toast.makeText(MainActivity.this,
                            getResources().getString(R.string.generic_error),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Method that is fired when the Search button is clicked
    // Edit: no longer needed because we're using SearchView so Search button has also been removed
    /*public void onImageSearch(View view) {
        query = etSearchQuery.getText().toString();
        getSearchImages(query, null, null, null, null);
    }*/
}
