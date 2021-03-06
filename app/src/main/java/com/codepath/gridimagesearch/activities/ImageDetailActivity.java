package com.codepath.gridimagesearch.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.ShareActionProvider;
import android.provider.MediaStore.Images;
import android.widget.Toast;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.helpers.Constants;
import com.codepath.gridimagesearch.helpers.ImageResultParcelable;
import com.codepath.gridimagesearch.helpers.TouchImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

public class ImageDetailActivity extends ActionBarActivity {

    // TouchImageView for zoom and pan
    private TouchImageView ivFullImg;
    private ImageView ivBg;
    private TextView tvTitle;
    private ShareActionProvider miShareAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        ivFullImg = (TouchImageView) findViewById(R.id.ivFullImg);
        ivBg = (ImageView) findViewById(R.id.ivBg);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ImageResultParcelable curr_image = getIntent().getParcelableExtra(Constants.curr_image_extra);

        //tvTitle.setText(Html.fromHtml(curr_image.getTitle()));

        int aspectRatio = curr_image.getWidth() / curr_image.getHeight();

        // This piece of code gets the display width, and height. It tries to do some aspect ratio
        // maintenance to get the largest possible good image if the image's real width and/or height
        // are > screen width/height
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int targetWidth = size.x;
        int targetHeight = size.y;

        if (curr_image.getWidth() <= targetWidth && curr_image.getHeight() <= targetHeight) {
            targetHeight = curr_image.getHeight();
            targetWidth = curr_image.getWidth();
        }

        else if (curr_image.getWidth() > targetWidth) {
            targetHeight = targetWidth / aspectRatio;
        }
        else if (curr_image.getHeight() > targetHeight) {
            targetWidth = aspectRatio * targetHeight;
        }
        Picasso.with(this).load(curr_image.getUrl()).into(ivBg);
        Picasso.with(this).
                load(curr_image.getUrl()).resize(targetWidth, targetHeight).
                placeholder(getResources().getDrawable(R.drawable.loading)).
                into(ivFullImg, new Callback() {
                    @Override
                    public void onSuccess() {
                        // Setup share intent now that image has loaded
                        setupShareIntent();
                    }

                    @Override
                    public void onError() {
                        // ...
                    }
                });
    }

    private Uri getLocalBitmapUri() {

        View view  = new View(this);


        Drawable mDrawable = ivFullImg.getDrawable();
        Bitmap immutableBitmap = ((BitmapDrawable)mDrawable).getBitmap();
        Bitmap mutableBitmap = immutableBitmap.copy(Bitmap.Config.ARGB_8888, true);

        view.draw(new Canvas(mutableBitmap));

        String path = Images.Media.insertImage(ImageDetailActivity.this.getContentResolver(),
                immutableBitmap, tvTitle.getText().toString(), null);

        return Uri.parse(path);
    }

    public void setupShareIntent() {
        Uri bmpUri = getLocalBitmapUri();
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType(Constants.intent_type_for_img);

            // Attach share event to the menu item provider
            miShareAction.setShareIntent(shareIntent);
        } else {
            Log.e("ERROR", "Could not share image because bmpUri is null");
            Toast.makeText(this,
                    getResources().getString(R.string.img_share_error),
                    Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_detail, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);
        // Fetch reference to the share action provider
        miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
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
}
