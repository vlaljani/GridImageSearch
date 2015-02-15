package com.codepath.gridimagesearch.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.helpers.Constants;
import com.codepath.gridimagesearch.helpers.ImageFiltersParcelable;


/**
 * Created by vibhalaljani on 2/15/15.
 *
 * Fragment dialog for advanced search
 */
public class AdvSearchDialog extends DialogFragment {
    private Spinner spImgSize;
    private Spinner spImgColor;
    private Spinner spImgType;
    private EditText etImgSite;

    private ArrayAdapter<CharSequence> adapterImgSize;
    private ArrayAdapter<CharSequence> adapterImgColor;
    private ArrayAdapter<CharSequence> adapterImgType;

    private ImageFiltersParcelable filters;

    public AdvSearchDialog() {
        // Empty constructor required for DialogFragment
    }

    public interface AdvSearchDialogListener {
        void onFinishAdvSearchDialog(ImageFiltersParcelable newFilters);
    }

    public static AdvSearchDialog newInstance(ImageFiltersParcelable inFilters) {

        AdvSearchDialog frag = new AdvSearchDialog();

        Bundle args = new Bundle();
        args.putParcelable(Constants.inFilters, inFilters);

        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advanced_search, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setupViews(view);
        return view;
    }

    private void setupViews(View view) {


        // Find views
        spImgSize = (Spinner) view.findViewById(R.id.spImgSize);
        spImgColor = (Spinner) view.findViewById(R.id.spImgColor);
        spImgType = (Spinner) view.findViewById(R.id.spImgType);

        etImgSite = (EditText) view.findViewById(R.id.etImgSite);

        ImageButton btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        Button btnSearch = (Button) view.findViewById(R.id.btnSearch);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvSearchDialogListener listener = (AdvSearchDialogListener) getActivity();
                if (!(etImgSite.getText().toString().isEmpty())) {
                    filters.setSite(etImgSite.getText().toString());
                }
                listener.onFinishAdvSearchDialog(filters);
                dismiss();
            }
        });

        // Create adapters
        adapterImgSize = ArrayAdapter.createFromResource(getActivity(),
                R.array.img_sizes, android.R.layout.simple_spinner_item);
        adapterImgColor = ArrayAdapter.createFromResource(getActivity(),
                R.array.img_colors, android.R.layout.simple_spinner_item);
        adapterImgType = ArrayAdapter.createFromResource(getActivity(),
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
        filters = new ImageFiltersParcelable();
        filters.copyFilters((ImageFiltersParcelable) getArguments().
                                               getParcelable(Constants.inFilters));
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

}
