package com.codepath.gridimagesearch;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.gridimagesearch.activities.MainActivity;


public class AdvancedSearchActivity extends ActionBarActivity {

    // Need to make arrays so we're not doing everything thrice
    private Spinner spImgSize;
    private Spinner spImgColor;
    private Spinner spImgType;
    private EditText etImgSite;

    private ArrayAdapter<CharSequence> adapterImgSize;
    private ArrayAdapter<CharSequence> adapterImgColor;
    private ArrayAdapter<CharSequence> adapterImgType;

    private String color;
    private String size;
    private String type;
    private String site;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);

        color = null;
        size = null;
        type = null;
        site = null;

        spImgSize = (Spinner) findViewById(R.id.spImgSize);
        spImgColor = (Spinner) findViewById(R.id.spImgColor);
        spImgType = (Spinner) findViewById(R.id.spImgType);
        etImgSite = (EditText) findViewById(R.id.etImgSite);

        adapterImgSize = ArrayAdapter.createFromResource(this,
                R.array.img_sizes, android.R.layout.simple_spinner_item);
        adapterImgColor = ArrayAdapter.createFromResource(this,
                R.array.img_colors, android.R.layout.simple_spinner_item);
        adapterImgType = ArrayAdapter.createFromResource(this,
                R.array.img_types, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapterImgSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterImgColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterImgType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spImgSize.setAdapter(adapterImgSize);
        spImgColor.setAdapter(adapterImgColor);
        spImgType.setAdapter(adapterImgType);

        setupViewListeners();
    }

    private void setupViewListeners() {
        spImgSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                size = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spImgColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                color = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spImgType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = (String) parent.getItemAtPosition(position);
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

    public void onImageSearch(View v) {
        if (size != null && size.equals(getResources().getString(R.string.no_filter))) {
            size = null;
        }
        if (color != null && color.equals(getResources().getString(R.string.no_filter))) {
            color = null;
        }
        if (type != null && type.equals(getResources().getString(R.string.no_filter))) {
            type = null;
        }
        if (!(etImgSite.getText().toString().isEmpty())) {
            site = etImgSite.getText().toString();
        }

        Intent i = new Intent(AdvancedSearchActivity.this, MainActivity.class);
        i.putExtra("size", size);
        i.putExtra("color", color);
        i.putExtra("type", type);
        i.putExtra("site", site);

        setResult(RESULT_OK, i);
        finish();
    }
}
