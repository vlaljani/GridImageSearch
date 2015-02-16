package com.codepath.gridimagesearch.adapters;

import android.app.Activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.helpers.ImageResultParcelable;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vibhalaljani on 2/11/15.
 *
 * This is the adapter to populate the staggered grid view. It extends the generic adapter so we
 * can see a progress bar in the last row.
 */



public class ImageResultsAdapter extends GenericAdapter<ImageResultParcelable> {

    private static class ViewHolder {
        ImageView ivSearchRes;
    }

    /*public ImageResultsAdapter(Context context, List<ImageResultParcelable> images) {
        //super(context, R.layout.image_item, images);
        super(context,images);
    }*/

    public ImageResultsAdapter(Activity mActivity, List<ImageResultParcelable> images) {
        //super(context, R.layout.image_item, images);
        super(mActivity,images);
    }

    // Method to return data for a row that's not the last row.
    @Override
    public View getDataRow(int position, View convertView, ViewGroup parent) {
        ImageResultParcelable imageResult = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = mActivity.getLayoutInflater().inflate(R.layout.image_item, parent, false);

            viewHolder.ivSearchRes = (ImageView) convertView.findViewById(R.id.ivSearchRes);

            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();

        Picasso.with(convertView.getContext()).
                load(imageResult.getTbUrl()).
                placeholder(convertView.getContext().getResources().getDrawable(R.drawable.loading)).
                into(viewHolder.ivSearchRes);

        return convertView;
    }

}
