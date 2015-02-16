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
import android.widget.Toast;

import com.codepath.gridimagesearch.adapters.ImageResultsAdapter;
import com.codepath.gridimagesearch.fragments.AdvSearchDialog;
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


public class MainActivity extends ActionBarActivity implements
        AdvSearchDialog.AdvSearchDialogListener{

    // Edit: No longer needed because we're using SearchView
    //private EditText etSearchQuery;
    private StaggeredGridView gvImages;
    private ArrayList<ImageResultParcelable> imageResults;
    private ImageResultsAdapter imageResultsAdapter;
    private String query = null;
    private ImageFiltersParcelable q_filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting this up because otherwise the progress bar starts acting and it looks weird
        query = Constants.defaultQuery;
        q_filters = new ImageFiltersParcelable();
        setupViews();
    }

    // Method to find the views and set them up
    private void setupViews() {
        // Edit: No longer needed because we're using SearchView
        // etSearchQuery = (EditText) findViewById(R.id.etSearchQuery);
        gvImages = (StaggeredGridView) findViewById(R.id.gvImages);
        imageResults = new ArrayList<ImageResultParcelable>();
        imageResultsAdapter = new ImageResultsAdapter(MainActivity.this, imageResults);
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
                i.putExtra(Constants.curr_image_extra, curr_image);
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

        // When a user submits a query on the action bar search view, invoke this.
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String q) {
                query = q;
                // Edit: We're back to clearing the arraylist because we're no longer extending
                // the ArrayAdapter, but a generic adapter
                //imageResultsAdapter.clear();
                imageResults.clear();
                imageResultsAdapter.notifyDataSetChanged();
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

    // Method to decide the course of action to take when a user selects an action from the
    // action bar.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // If the settings icon is clicked
        // Edit: This stub is no longer needed because the activity was replaced with a light-weight
        // modal overlay.
        /*if (id == R.id.search_settings) {
            Intent i = new Intent(MainActivity.this, AdvancedSearchActivity.class);
            i.putExtra(Constants.inFilters, q_filters);
            startActivityForResult(i, ADV_SEARCH_REQ_CODE);
        }*/
        if (id == R.id.search_settings) {
            showAdvSearchDialog(q_filters);
        }

        return super.onOptionsItemSelected(item);
    }

    // This is the method that's called when the settings action is selected on the MainActivity.
    // This generates a dialog with advanced search filters.
    private void showAdvSearchDialog(ImageFiltersParcelable inFilters) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        AdvSearchDialog advSearchDialog = AdvSearchDialog.newInstance(inFilters);
        advSearchDialog.show(fm, Constants.advSearchDialogTag);
    }

    // This is the callback method for when the Search button is hit on the advancedSearchDialog.
    public void onFinishAdvSearchDialog(ImageFiltersParcelable newFilters) {
        q_filters.copyFilters(newFilters);

        // since this is a new query
        // Edit: We're now clearing the arrayList instead of the adapter because imageResultsAdapter
        // is no longer extending ArrayAdapter, but our generic adapter for loading bar.
        //imageResultsAdapter.clear();
        Log.i("WHA", "goes here");
        imageResults.clear();
        imageResultsAdapter.notifyDataSetChanged();
        getSearchImages(query, q_filters, 0);
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

    // This method determines if the mobile device is connected to the internet
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    // Edit: This method is used when we return to MainActivity from another activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check if we're back from the Advanced Search activity
        if (resultCode == RESULT_OK && requestCode == Constants.ADV_SEARCH_REQ_CODE) {
            // Extract advanced filters from result extras
            q_filters.copyFilters((ImageFiltersParcelable) data.getParcelableExtra(
                                                                            Constants.newFilters));

            // since this is a new query
            // Edit: we're clearing the array list directly because ImageResultsAdapter no longer
            // extends ArrayAdapter. It extends BaseAdapter which doesn't have clear/addAll
            // functionality
            //imageResultsAdapter.clear();
            imageResults.clear();
            imageResultsAdapter.notifyDataSetChanged();
            getSearchImages(query, q_filters, 0);
        }
    }

    // Method to call the Google API with the main query, advanced filters, and
    // the page (for scrolling)
    private void getSearchImages(final String query,
                                 ImageFiltersParcelable curr_filters,
                                 int page) {

        q_filters.copyFilters(curr_filters);


        StringBuilder urlBuilder = new StringBuilder();
        // Add the query to the url
        urlBuilder.append(Constants.appender).append(Constants.queryKey).append(query);
        urlBuilder.append(Constants.appender).append(Constants.pageKey).append(page * Constants.RESULT_SIZE);
        urlBuilder.append(q_filters.getQuery());

        String url = urlBuilder.toString();

        if (!isNetworkAvailable()) {
            Toast.makeText(this, getResources().getString(R.string.internet_error),
                    Toast.LENGTH_SHORT).show();
        }

        Log.i("WHA", urlBuilder.toString());
        if (isNetworkAvailable() && query != null) {
            GoogleImgSearchRestClient.get(url, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray imageResultsJSON = response.getJSONObject(Constants.gAPIJSONRoot)
                                .getJSONArray(Constants.gAPIJSONImgArrayKey);

                        // Edit: no longer needed because we're using SearchView
                        /*etSearchQuery.setText("");
                        etSearchQuery.append(query);
                        etSearchQuery.requestFocus();*/

                        //Edit: we're adding to the array list  because ImageResultsAdapter no longer
                        // extends ArrayAdapter. It extends BaseAdapter which doesn't have clear/addAll
                        // functionality
                        imageResults.addAll(ImageResultParcelable.fromJSONArray(imageResultsJSON));
                        imageResultsAdapter.notifyDataSetChanged();
                        //imageResultsAdapter.addAll(ImageResultParcelable.fromJSONArray(imageResultsJSON));

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
