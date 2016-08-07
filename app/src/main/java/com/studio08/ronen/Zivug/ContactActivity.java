package com.studio08.ronen.Zivug;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;

import android.widget.ImageView;

import java.util.UUID;

public class ContactActivity extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbar;

    private static final String EXTRA_CONTACT_ID = "com.studio08.ronen.Zivug.contact_id";
    private String firstName = "First";
    private String lastName = "Last";

    public static Intent newIntent(Context packageContext, UUID contactId) {
        Intent intent = new Intent(packageContext, ContactActivity.class);
        intent.putExtra(EXTRA_CONTACT_ID, contactId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // crashes
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Setup and initialization collapsing toolbar
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(firstName + " " + lastName);
        collapsingToolbar.setExpandedTitleColor(Color.parseColor("#44ffffff"));

        // initialization ImageView
        ImageView header = (ImageView) findViewById(R.id.iv_header);

        // take a bitmap image used in image view
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.header);

        // extract colors from images used, should check async version
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int mutedColor = palette.getMutedColor(R.color.colorPrimary); // param is default color if the swatch isn't available
                int vibrantDarkColor = palette.getDarkVibrantColor(R.color.colorPrimary); // param is default color if the swatch isn't available
                int lightMutedColor = palette.getLightMutedColor(R.color.colorPrimary); // param is default color if the swatch isn't available
                collapsingToolbar.setContentScrimColor(vibrantDarkColor);
                collapsingToolbar.setExpandedTitleColor(lightMutedColor);
            }
        });
    }
}
