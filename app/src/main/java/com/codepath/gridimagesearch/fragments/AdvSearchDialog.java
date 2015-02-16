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

import java.util.HashMap;
import java.util.Map;


/**
 * Created by vibhalaljani on 2/15/15.
 *
 * Fragment dialog for advanced search
 */
public class AdvSearchDialog extends DialogFragment {

    private static final HashMap<Integer, String> SIZES;
    private static final HashMap<Integer, String> COLORS;
    private static final HashMap<Integer, String> TYPES;

    static {
        SIZES = new HashMap<>();
        SIZES.put(0, "icon");
        SIZES.put(1, "small");
        SIZES.put(2, "medium");
        SIZES.put(3, "large");
        SIZES.put(4, "xlarge");
        SIZES.put(5, "xxlarge");
        SIZES.put(6, "huge");

        COLORS = new HashMap<>();
        COLORS.put(0, "black");
        COLORS.put(1, "blue");
        COLORS.put(2, "brown");
        COLORS.put(3, "gray");
        COLORS.put(4, "green");
        COLORS.put(5, "orange");
        COLORS.put(6, "pink");
        COLORS.put(7, "purple");
        COLORS.put(8, "red");
        COLORS.put(9, "teal");
        COLORS.put(10, "white");
        COLORS.put(11, "yellow");

        TYPES = new HashMap<>();
        TYPES.put(0, "face");
        TYPES.put(1, "photo");
        TYPES.put(2, "clipart");
        TYPES.put(3, "lineart");
    }

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
                } else {
                    filters.setSite(null);
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
                    filters.setSize(SIZES.get(position - 1));
                } else {
                    filters.setSize(null);
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
                    filters.setColor(COLORS.get(position - 1));
                } else {
                    filters.setColor(null);
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
                    filters.setType(TYPES.get(position - 1));
                } else {
                    filters.setType(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
