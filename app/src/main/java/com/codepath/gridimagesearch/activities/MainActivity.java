package com.codepath.gridimagesearch.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.gridimagesearch.AdvancedSearchActivity;
import com.codepath.gridimagesearch.adapters.ImageResultsAdapter;
import com.codepath.gridimagesearch.net.GoogleImgSearchRestClient;
import com.codepath.gridimagesearch.models.ImageResult;
import com.codepath.gridimagesearch.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private EditText etSearchQuery;
    private GridView gvImages;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter imageResultsAdapter;
    private final int ADV_SEARCH_REQ_CODE = 7;
    private String query = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();

        imageResults = new ArrayList<ImageResult>();
        imageResultsAdapter = new ImageResultsAdapter(this, imageResults);
        gvImages.setAdapter(imageResultsAdapter);



    }

    private void setupViewListeners() {
        gvImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageResult curr_image = imageResults.get(position);
                Intent i = new Intent(MainActivity.this, ImageDetailActivity.class);
                i.putExtra("current_image", curr_image);
                startActivity(i);
            }
        });

    }

    private void setupViews() {
        etSearchQuery = (EditText) findViewById(R.id.etSearchQuery);
        gvImages = (GridView) findViewById(R.id.gvImages);

        setupViewListeners();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_settings) {
            Intent i = new Intent(MainActivity.this, AdvancedSearchActivity.class);
            startActivityForResult(i, ADV_SEARCH_REQ_CODE);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == ADV_SEARCH_REQ_CODE) {
            // Extract name value from result extras
            String size = data.getExtras().getString("size", null);
            String color = data.getExtras().getString("color", null);
            String type = data.getExtras().getString("type", null);
            String site = data.getExtras().getString("site", null);
            // Toast the name to display temporarily on screen
            getSearchImages(query, size, color, type, site);
        }
    }

    private void getSearchImages(final String query,
                                 String size,
                                 String color,
                                 String type,
                                 String site) {
        String appender = "&";
        String imgColorKey = "imgcolor=";
        String imgSizeKey = "imgsz=";
        String imgTypeKey = "imgtype=";
        String imgSiteKey = "as_sitesearch";


        String queryKey = "q=";
        StringBuilder urlBuilder = new StringBuilder().append(appender + queryKey + query);
        if (size != null) {
            urlBuilder.append(appender + imgSizeKey + size);
        }
        if (color != null) {
            urlBuilder.append(appender + imgColorKey + color);
        }
        if (type != null) {
            urlBuilder.append(appender + imgTypeKey + type);
        }
        if (site != null) {
            urlBuilder.append(appender + imgSiteKey + site);
        }
        String url = urlBuilder.toString();
        GoogleImgSearchRestClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("JSON", "goes here");
                try {
                    JSONArray imageResultsJSON = response.getJSONObject("responseData")
                            .getJSONArray("results");
                    imageResultsAdapter.clear(); // don't wanna call this on pagination!!
                    etSearchQuery.setText("");
                    etSearchQuery.append(query);
                    etSearchQuery.requestFocus();
                    imageResultsAdapter.addAll(ImageResult.fromJSONArray(imageResultsJSON));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    // Method that is fired when the Search button is clicked
    public void onImageSearch(View view) {
        query = etSearchQuery.getText().toString();
        getSearchImages(query, null, null, null, null);
    }
}
