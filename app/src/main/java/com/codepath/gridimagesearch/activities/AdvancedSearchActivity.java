package com.codepath.gridimagesearch.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.helpers.Constants;
import com.codepath.gridimagesearch.helpers.ImageFiltersParcelable;

/*
 * This activity is actually not used anymore because it was replaced with a lightweight modal
 * overlay.
 */
public class AdvancedSearchActivity extends ActionBarActivity {

    private Spinner spImgSize;
    private Spinner spImgColor;
    private Spinner spImgType;
    private EditText etImgSite;

    private ArrayAdapter<CharSequence> adapterImgSize;
    private ArrayAdapter<CharSequence> adapterImgColor;
    private ArrayAdapter<CharSequence> adapterImgType;

    private ImageFiltersParcelable filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);

        ImageFiltersParcelable main_activity_filters = getIntent().
                getParcelableExtra(Constants.inFilters);
        filters = new ImageFiltersParcelable();
        filters.copyFilters(main_activity_filters);

        setupViews();
    }

    // Set up the Spinners, adapters, and the current selection on the spinners, if any.
    private void setupViews() {


        // Find views
        spImgSize = (Spinner) findViewById(R.id.spImgSize);
        spImgColor = (Spinner) findViewById(R.id.spImgColor);
        spImgType = (Spinner) findViewById(R.id.spImgType);
        etImgSite = (EditText) findViewById(R.id.etImgSite);

        // Create adapters
        adapterImgSize = ArrayAdapter.createFromResource(this,
                R.array.img_sizes, android.R.layout.simple_spinner_item);
        adapterImgColor = ArrayAdapter.createFromResource(this,
                R.array.img_colors, android.R.layout.simple_spinner_item);
        adapterImgType = ArrayAdapter.createFromResource(this,
                R.array.img_types, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapterImgSize.setDropDownViewResource(R.layout.item_spinner);
        adapterImgColor.setDropDownViewResource(R.layout.item_spinner);
        adapterImgType.setDropDownViewResource(R.layout.item_spinner);

        // Apply the adapter to the spinner
        spImgSize.setAdapter(adapterImgSize);
        spImgColor.setAdapter(adapterImgColor);
        spImgType.setAdapter(adapterImgType);

        // Populate the spinners with the previous selections
        if (filters.getSize() != null)
            spImgSize.setSelection(adapterImgSize.getPosition(filters.getSize()));
        if (filters.getColor() != null)
            spImgColor.setSelection(adapterImgColor.getPosition(filters.getColor()));
        if (filters.getType() != null) {
            spImgType.setSelection(adapterImgType.getPosition(filters.getType()));
        }
        if (filters.getSite() != null) {
            etImgSite.setText(filters.getSite());
        }

        // Set up the listeners
        setupViewListeners();

    }

    // Method to set up listeners on the spinners
    private void setupViewListeners() {
        spImgSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String size = (String) parent.getItemAtPosition(position);
                if (!size.equals(getResources().getString(R.string.no_filter))) {
                    filters.setSize(size);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spImgColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String color = (String) parent.getItemAtPosition(position);
                if (!color.equals(getResources().getString(R.string.no_filter))) {
                    filters.setColor(color);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spImgType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = (String) parent.getItemAtPosition(position);
                if (!type.equals(getResources().getString(R.string.no_filter))) {
                    filters.setType(type);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_advanced_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // On click action for the search button
    public void onImageSearch(View v) {

        if (!(etImgSite.getText().toString().isEmpty())) {
            filters.setSite(etImgSite.getText().toString());
        }

        Intent i = new Intent(AdvancedSearchActivity.this, MainActivity.class);
        i.putExtra(Constants.newFilters, filters);

        setResult(RESULT_OK, i);
        finish();
    }
}
