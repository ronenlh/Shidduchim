package com.studio08.ronen.Zivug;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.UUID;

public class ContactActivity extends AppCompatActivity {
    private static final String TAG = "ContactActivity";
    CollapsingToolbarLayout collapsingToolbar;

    private static final String EXTRA_CONTACT_ID = "com.studio08.ronen.Zivug.contact_id";

    private Contact mContact;

    TextView contactDetailsTextView, contactOverviewTextView, contactDatesTextView, contactNotesTextView;

    public static Intent newIntent(Context packageContext, UUID contactId) {
        Intent intent = new Intent(packageContext, ContactActivity.class);
        intent.putExtra(EXTRA_CONTACT_ID, contactId);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);


        // get the specific contact
        UUID contactId = (UUID) getIntent().getSerializableExtra(EXTRA_CONTACT_ID);
        mContact = ContactLab.get(this).getContact(contactId);

        // Setup and initialization collapsing toolbar
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mContact.getName());
        collapsingToolbar.setExpandedTitleColor(Color.parseColor("#44ffffff"));

        Log.d(TAG, mContact.toString());

        initViews();
        initHeader();
        initInfo();
    }

    private void initViews() {
        contactDetailsTextView = (TextView) findViewById(R.id.contact_details);
        contactOverviewTextView = (TextView) findViewById(R.id.contact_overview);
        contactDatesTextView = (TextView) findViewById(R.id.contact_dates);
        contactNotesTextView = (TextView) findViewById(R.id.contact_notes);
    }

    private void initHeader() {
        ImageView headerImageView = (ImageView) findViewById(R.id.iv_header);

        Picasso.with(this)
                .load("file://" + mContact.getPicturePath())
                .fit().centerCrop()
//                .transform(new CropSquareTransformation()) // inner class in this file
                .into(headerImageView);

        // take a bitmap image used in image view
        File imageFile = new File(mContact.getPicturePath());
        if (imageFile.exists()) {
            Bitmap mBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            mBitmap = Bitmap.createScaledBitmap(mBitmap,100,100,true);

            // extract colors from images used, should check async version
            Palette.from(mBitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    int mutedColor = palette.getMutedColor(R.color.colorPrimary); // param is default color if the swatch isn't available
                    int vibrantDarkColor = palette.getDarkVibrantColor(R.color.colorPrimary); // param is default color if the swatch isn't available
                    int lightMutedColor = palette.getLightMutedColor(R.color.colorPrimary); // param is default color if the swatch isn't available
                    int vibrantLightColor = palette.getLightVibrantColor(Color.WHITE);
                    collapsingToolbar.setContentScrimColor(vibrantDarkColor);
                    collapsingToolbar.setExpandedTitleColor(lightMutedColor);
                    collapsingToolbar.setCollapsedTitleTextColor(vibrantLightColor);
                    collapsingToolbar.setStatusBarScrimColor(mutedColor);
                }
            });
        }
    }

    private void initInfo() {
        String mDetails = "Age: " + mContact.getAge();
        contactDetailsTextView.setText(mDetails);

        String mNotes = mContact.getNotes();
        contactNotesTextView.setText(mNotes);

        contactOverviewTextView.setText(mContact.toString());
    }

    @Override
    public void onPause() {
        super.onPause();
        ContactLab.get(this).updateContact(mContact);
    }

    public class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() { return "square()"; }
    }
}
