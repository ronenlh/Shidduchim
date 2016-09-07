package com.studio08.ronen.Zivug.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.studio08.ronen.Zivug.Model.Contact;
import com.studio08.ronen.Zivug.Model.ContactLab;
import com.studio08.ronen.Zivug.Model.ContactsRVCursorAdapter;
import com.studio08.ronen.Zivug.Model.DatabaseContract;
import com.studio08.ronen.Zivug.R;

import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

public class ContactActivity extends AppCompatActivity {
    private static final String TAG = "ContactActivity";
    private static final int EDIT_CONTACT = 1130;
    UUID contactId;
    CollapsingToolbarLayout collapsingToolbar;

    private static final String EXTRA_CONTACT_ID = "com.studio08.ronen.Zivug.contact_id";

    private Contact mContact;

    TextView contactDetailsTextView, contactOverviewTextView, contactDatesTextView, contactNotesTextView;
    private RecyclerView mRecyclerView;
    private ContactsRVCursorAdapter mCursorAdapter;

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
        contactId = (UUID) getIntent().getSerializableExtra(EXTRA_CONTACT_ID);
        mContact = ContactLab.get(this).getContact(contactId);

        Log.d(TAG, mContact.toString());

        initViews();
        if (mContact.getPicturePath() != null) initHeader();
        initInfo();
        initRecyclerView();
    }

    private void initViews() {
        contactDetailsTextView = (TextView) findViewById(R.id.contact_details);
        contactOverviewTextView = (TextView) findViewById(R.id.contact_overview);
        contactDatesTextView = (TextView) findViewById(R.id.contact_dates);
        contactNotesTextView = (TextView) findViewById(R.id.contact_notes);
        mRecyclerView = (RecyclerView) findViewById(R.id.card_recyclerview);
    }

    private void initHeader() {

        // Setup and initialization collapsing toolbar
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mContact.getName());
        collapsingToolbar.setExpandedTitleColor(Color.parseColor("#44ffffff"));

        ImageView headerImageView = (ImageView) findViewById(R.id.iv_header);

        // take a bitmap image used in image view
        File imageFile = new File(mContact.getPicturePath());
        if (imageFile.exists()) {
            Bitmap mBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            mBitmap = Bitmap.createScaledBitmap(mBitmap, 100, 100, true);

            // extract colors from images used, should check async version
            Palette.from(mBitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    int mutedColor = palette.getMutedColor(R.color.colorPrimary); // param is default color if the swatch isn't available
                    int vibrantDarkColor = palette.getDarkVibrantColor(R.color.colorPrimary); // param is default color if the swatch isn't available
                    int lightMutedColor = palette.getLightMutedColor(Color.WHITE); // param is default color if the swatch isn't available
                    int vibrantLightColor = palette.getLightVibrantColor(Color.WHITE);
                    collapsingToolbar.setContentScrimColor(vibrantDarkColor);
                    collapsingToolbar.setExpandedTitleColor(lightMutedColor);
                    collapsingToolbar.setCollapsedTitleTextColor(vibrantLightColor);
                    collapsingToolbar.setStatusBarScrimColor(mutedColor);
                }
            });
        }

        Picasso.with(this)
                .load("file://" + mContact.getPicturePath())
                .fit().centerCrop()
//                .transform(new CropSquareTransformation()) // inner class in this file
                .into(headerImageView);
    }

    private void initInfo() {
        StringBuilder mDetails = new StringBuilder("Name: " + mContact.getName());
        if (mContact.getAge() > 0)              mDetails.append("\nAge: " + mContact.getAge());
        if (mContact.getEmail() != null)        mDetails.append("\nE-Mail: " + mContact.getEmail());
        if (mContact.getPhone() != null)        mDetails.append("\nPhone: " + mContact.getPhone());
        if (mContact.getTags() != null
                && !mContact.getTags().isEmpty()) {
            // represent Tags into string:
            mDetails.append("\nTags: ");
            Set<ContactLab.Tag> tagsSet = mContact.getTags();
            ContactLab.Tag[] tagArray = new ContactLab.Tag[tagsSet.size()];
            tagArray = tagsSet.toArray(tagArray);
            mDetails.append(Arrays.toString(tagArray));
//            for (int i = 0; i < tagArray.length; i++) {
//                mDetails.append(tagArray[i].getName());
//                if (i < tagArray.length - 1) mDetails.append(", ");
//            }
        }

        contactDetailsTextView.setText(mDetails.toString());

        String mNotes = mContact.getNotes();
        contactNotesTextView.setText(mNotes);

        contactOverviewTextView.setText(mContact.toString());
    }

    private void initRecyclerView() {

        ContactLab contactLab = ContactLab.get(this);
        Cursor cursor = contactLab.queryContactsTable(
                DatabaseContract.Entry.COLUMN_NAME_ENTRY_UUID + " MATCH ?",
                mContact.getPreviousDatesStringArray()
        );
        try {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
            mRecyclerView.setLayoutManager(mLayoutManager);

            mCursorAdapter = new ContactsRVCursorAdapter(this, cursor);
            mRecyclerView.setAdapter(mCursorAdapter);
        } finally {
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.action_edit:
                editContact();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void editContact() {
        Intent intent = new Intent(this, AddContactActivity.class);
        intent.putExtra(AddContactActivity.EXTRA_UPDATING, true);
        intent.putExtra(AddContactActivity.EXTRA_CONTACT_ID, mContact.getId());
        startActivityForResult(intent, EDIT_CONTACT);
    }

    @Override
    public void onPause() {
        super.onPause();
        ContactLab.get(this).updateContact(mContact);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");

        mContact = ContactLab.get(this).getContact(contactId);

        initHeader();
        initInfo();
    }

    //    public class CropSquareTransformation implements Transformation {
//        @Override public Bitmap transform(Bitmap source) {
//            mBitmap = source;
//            int size = Math.min(source.getWidth(), source.getHeight());
//            int x = (source.getWidth() - size) / 2;
//            int y = (source.getHeight() - size) / 2;
//            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
//            if (result != source) {
//                source.recycle();
//            }
//            return result;
//        }
//
//        @Override public String key() { return "square()"; }
//    }
}
