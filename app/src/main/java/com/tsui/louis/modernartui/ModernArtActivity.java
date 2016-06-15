package com.tsui.louis.modernartui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

public class ModernArtActivity extends AppCompatActivity {

    private static final String MOMA_URL = "http://www.moma.org";
    private static final String TAG = "ModernArtActivity";

    private DialogFragment mDialog;
    private ImageView mImageView, mImageView2, mImageView3, mImageView4, mImageView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modern_art);

        Toolbar modernArtToolbar = (Toolbar) findViewById(R.id.modernart_toolbar);
        setSupportActionBar(modernArtToolbar);

        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView2 = (ImageView) findViewById(R.id.imageView2);
        mImageView3 = (ImageView) findViewById(R.id.imageView3);
        mImageView4 = (ImageView) findViewById(R.id.imageView4);
        mImageView5 = (ImageView) findViewById(R.id.imageView5);

        // programmatically apply initial filter
        mImageView.setColorFilter(Color.argb(255, 255, 0, 255), PorterDuff.Mode.MULTIPLY);
        mImageView2.setColorFilter(Color.argb(255, 127, 255, 0), PorterDuff.Mode.MULTIPLY);
        mImageView3.setColorFilter(Color.argb(255, 255, 127, 0));
        mImageView5.setColorFilter(Color.argb(255, 0, 255, 255));

        SeekBar seekBar = (SeekBar) findViewById(R.id.seek_colour);
        if (seekBar != null) {
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // alter the colour of non-monochromatic views depending on the progress
                    // what other ways are there for doing this?
                    mImageView.setColorFilter(Color.argb(255, 255-progress, progress, 255-progress),
                            PorterDuff.Mode.MULTIPLY);
                    mImageView2.setColorFilter(Color.argb(255, 127, 255-progress, progress),
                            PorterDuff.Mode.MULTIPLY);
                    mImageView3.setColorFilter(Color.argb(255, 255-progress, 127, progress));
                    mImageView5.setColorFilter(Color.argb(255, progress, 255-progress, 255-progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    Toast.makeText(getApplicationContext(), "Started tracking seekbar",
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Toast.makeText(getApplicationContext(), "Stopped tracking seekbar",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.activity_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.more_info:
                // open a dialog
                mDialog = InfoDialogFragment.newInstance();
                mDialog.show(getSupportFragmentManager(), "MoreInfo");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void launchWebpage(boolean shouldLaunch) {
        if (shouldLaunch) {
            // launch browser to MOMA home page
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MOMA_URL));

            if (webIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(webIntent);
            }

        } else {
            mDialog.dismiss();
        }
    }

    // For the dialog created upon selecting more information
    public static class InfoDialogFragment extends DialogFragment {

        public static InfoDialogFragment newInstance() {
            return new InfoDialogFragment();
        }

        // Build the AlertDialog
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_info)
                   .setPositiveButton(R.string.dialog_webpage,
                           new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   ((ModernArtActivity) getActivity()).launchWebpage(true);
                               }
                           })
                   .setNegativeButton(R.string.dialog_cancel,
                           new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   ((ModernArtActivity) getActivity()).launchWebpage(false);
                               }
                           });
            return builder.create();
        }
    }
}
