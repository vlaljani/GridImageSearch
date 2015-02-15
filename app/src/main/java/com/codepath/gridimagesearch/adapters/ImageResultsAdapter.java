package com.codepath.gridimagesearch.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.helpers.ImageResultParcelable;
import com.codepath.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vibhalaljani on 2/11/15.
 */



public class ImageResultsAdapter extends ArrayAdapter<ImageResultParcelable> {

    private static class ViewHolder {
        ImageView ivSearchRes;
    }

    public ImageResultsAdapter(Context context, List<ImageResultParcelable> images) {
        super(context, R.layout.image_item, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResultParcelable imageResult = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_item,
                    parent, false);

            viewHolder.ivSearchRes = (ImageView) convertView.findViewById(R.id.ivSearchRes);

            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

        Picasso.with(getContext()).load(imageResult.getTbUrl()).into(viewHolder.ivSearchRes);

        return convertView;
    }

}
