package com.studio08.ronen.Zivug.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.studio08.ronen.Zivug.Model.Contact;
import com.studio08.ronen.Zivug.Model.ContactLab;
import com.studio08.ronen.Zivug.R;
import com.studio08.ronen.Zivug.TagView;

import java.io.File;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO: populate recyclerview with previous dates from a cursor querying the UUIDs from a list inside contact (like the tags)
 * */

public class AddContactActivity extends AppCompatActivity implements TagView.OnTagDeletedListener {

    public static final String EXTRA_UPDATING = "updating";
    public static final String EXTRA_CONTACT_ID = "UUID";

    private static final String STATE_IN_PERMISSION = "gallery";
    private static final String TAG = "AddContactActivity";
    private static final int SET_LOCATION_RESULT = 1121;
    static final int SET_TAGS_RESULT = 1122;
    private static final int REQUEST_CONTACT = 1123;
    private static final int REQUEST_PHOTO = 1124;
    private static final int REQUEST_GALLERY = 1125;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 100;

    private Contact mContact;

    private RadioGroup radioGroup;
    private RadioButton maleRadioButton, femaleRadioButton;
    private ImageView mPhotoImage, mGalleryImage;
    private CircularImageView mImageView;
    private TextView mTagsTextView;
    private File mPhotoFile;
    private int genderSelection = 2;

    private boolean userIsUpdating;

    private Intent pickContact, captureImageIntent, galleryImageIntent;

    private String mPicturePath;

    private EditText nameEditText, ageEditText, notesEditText;
    private Button deleteContactButton;
    private RelativeLayout mLinearLayout;
    private LinearLayout mTagsLinearLayout;

    private boolean isInPermission = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);

        userIsUpdating = getIntent().getBooleanExtra(EXTRA_UPDATING, false);

        if (userIsUpdating) {
            // get the specific contact
            UUID contactId = (UUID) getIntent().getSerializableExtra(EXTRA_CONTACT_ID);
            mContact = ContactLab.get(this).getContact(contactId);
        } else {
            mContact = new Contact(); // New UUID
            Log.d(TAG, "New Contact: " + mContact.getId().toString());
            mPhotoFile = ContactLab.get(this).getPhotoFile(mContact); // New File Name
        }

        if (savedInstanceState != null)
            isInPermission = savedInstanceState.getBoolean(STATE_IN_PERMISSION);


        initViews();
        if (userIsUpdating) fillViews();

        captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        galleryImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Disable if can't use button
        boolean canTakePhoto = mPhotoFile != null &&
                captureImageIntent.resolveActivity(getPackageManager()) != null;
        mPhotoImage.setEnabled(canTakePhoto);
        if (!canTakePhoto) mPhotoImage.setAlpha(0.5f);

        if (canTakePhoto) {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPhotoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(captureImageIntent, REQUEST_PHOTO);
            }
        });

        // Disable if can't use button
//        boolean canOpenGallery = galleryImageIntent.resolveActivity(getPackageManager()) != null;
//        mGalleryImage.setEnabled(canOpenGallery);
//        if (!canOpenGallery) mGalleryImage.setAlpha(0.5f);

        mGalleryImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openGalleryOrRequestPermission();
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryOrRequestPermission();
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void openGalleryOrRequestPermission() {
        // for the new API 23 dangerous permission model
        if (hasFilesPermission()) {
            startActivityForResult(galleryImageIntent, REQUEST_GALLERY);
        } else if (!isInPermission) {
            // we keep track of whether or no we are in the process of requesting permissions
            isInPermission = true;
            requestGalleryPermission();
        }
    }

    private boolean hasFilesPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void initViews() {
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        genderSelection = getIntent().getIntExtra(MainActivity.EXTRA_GENDER, 2);
        mImageView = (CircularImageView) findViewById(R.id.add_profile_pic);
        maleRadioButton = (RadioButton) findViewById(R.id.male_selection);
        femaleRadioButton = (RadioButton) findViewById(R.id.female_selection);
//        mTagsTextView = (TextView) findViewById(R.id.tags_textview);

        mPhotoImage = (ImageView) findViewById(R.id.camera_image);
        mGalleryImage = (ImageView) findViewById(R.id.gallery_image);

        nameEditText = (EditText) findViewById(R.id.add_name);
        ageEditText = (EditText) findViewById(R.id.add_age);
        notesEditText = (EditText) findViewById(R.id.add_notes);
        deleteContactButton = (Button) findViewById(R.id.delete_contact);
        mTagsLinearLayout = (LinearLayout) findViewById(R.id.tagsLayout);


        // This disable hardware acceleration to fix a bug
        // https://github.com/hdodenhof/CircleImageView/issues/31
        mLinearLayout = (RelativeLayout) findViewById(R.id.linear_layout_editor);
        mLinearLayout.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        // default values depending on where was the activity opened
        setGenderRadioButtons(genderSelection);
    }

    private void setGenderRadioButtons(int gender) {
        switch (gender) {
            case Contact.MALE:
                maleRadioButton.setChecked(true);
                femaleRadioButton.setChecked(false);
                loadImage(Contact.MALE);
                break;
            case Contact.FEMALE:
                maleRadioButton.setChecked(false);
                femaleRadioButton.setChecked(true);
                loadImage(Contact.FEMALE);
                break;
            case Contact.NOT_SET:
                break;
        }
    }

    private void fillViews() {
        // in case user is updating contact
        nameEditText.setText(mContact.getName());
        ageEditText.setText("" + mContact.getAge());
        notesEditText.setText(mContact.getNotes());
        mPicturePath = mContact.getPicturePath();
        deleteContactButton.setVisibility(View.VISIBLE);

        updateTags();

        setGenderRadioButtons(mContact.getGender());
        loadImage(mContact.getGender());

    }

    private void updateTags() {
//        StringBuilder stringBuilder = new StringBuilder();
//        if (mContact.getTags() != null) {
//            for (ContactLab.Tag tag : mContact.getTags()) {
//                if (!stringBuilder.toString().isEmpty()) stringBuilder.append(", ");
//                if (tag != null) stringBuilder.append(tag.toString());
//            }
//            stringBuilder.trimToSize();
//            mTagsTextView.setText(stringBuilder.toString());
//        }

        if (mContact.getTags() != null) {
            TagView tagView;
            mTagsLinearLayout.removeAllViews();
            for (ContactLab.Tag tag : mContact.getTags()) {
                tagView = new TagView(this, tag);
                mTagsLinearLayout.addView(tagView);
            }
        }
    }

    @Override
    public void tagDeleted(ContactLab.Tag tag) {
        mContact.deleteTag(tag);
        updateTags();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestGalleryPermission() {
        // https://developer.android.com/training/permissions/requesting.html

        Log.d(TAG, "requestGalleryPermission");

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.\
            } else {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(STATE_IN_PERMISSION, isInPermission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_EXTERNAL_STORAGE: {
                isInPermission = false;
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "permission was granted");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (hasFilesPermission())
                        startActivityForResult(galleryImageIntent, REQUEST_GALLERY);

                } else {
                    Log.d(TAG, "permission denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.male_selection:
                if (checked)
                    loadImage(Contact.MALE);
                break;
            case R.id.female_selection:
                if (checked)
                    loadImage(Contact.FEMALE);
                break;
        }
    }

    private void loadImage(int gender) {
        if (gender == Contact.MALE)
            Picasso.with(this)
                    .load("file://" + mPicturePath)
                    .fit().centerCrop()
//                    .placeholder(R.drawable.male_avatar)
                    .error(R.drawable.male_avatar)
                    .into(mImageView);
        else if (gender == Contact.FEMALE)
            Picasso.with(this)
                    .load("file://" + mPicturePath)
                    .fit().centerCrop()
//                    .placeholder(R.drawable.female_avatar)
                    .error(R.drawable.female_avatar)
                    .into(mImageView);
    }


    public void setLocation(View view) {
        Intent intent = new Intent(this, AddLocationActivity.class);
        startActivityForResult(intent, SET_LOCATION_RESULT);
    }

    public void setTags(View view) {
        Intent intent = new Intent(this, AddTagsActivity.class);
        startActivityForResult(intent, SET_TAGS_RESULT);
    }

    private void addContact() {
        if (nameEditText.getText().toString().isEmpty() || nameEditText.getText().toString().trim().equals("")) {
            Toast.makeText(this, R.string.no_name_inserted, Toast.LENGTH_SHORT).show();
            return;
        }
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, R.string.no_gender_selected, Toast.LENGTH_SHORT).show();
            return;
        }

        String name = nameEditText.getText().toString();

        mContact.setName(name);

        int gender = maleRadioButton.isChecked() ? Contact.MALE : Contact.FEMALE;
        mContact.setGender(gender);

        if (!ageEditText.getText().toString().isEmpty() || !ageEditText.getText().toString().trim().equals(""))
            mContact.setAge(Integer.parseInt(ageEditText.getText().toString()));

        if (!notesEditText.getText().toString().isEmpty() || !notesEditText.getText().toString().trim().equals("")) {
            String notes = notesEditText.getText().toString();
            mContact.setNotes(notes);

            // try to get the phone number and e-mail if there are
            Pattern eMailPattern = Pattern.compile("^[A-Za-z][A-Za-z0-9]*([._-]?[A-Za-z0-9]+)@[A-Za-z].[A-Za-z]{0,3}?.[A-Za-z]{0,2}$");
            Pattern phoneNumberPattern = Pattern.compile("^[+]?[0-9]{10,13}$");

            Matcher eMailMatcher = eMailPattern.matcher(notes);
            Matcher phoneNumberMatcher = phoneNumberPattern.matcher(notes);

            if (eMailMatcher.find()) {
                String eMail = eMailMatcher.group(1);
                Log.d(TAG, eMail);
                mContact.setEmail(eMail);
            }

            if (phoneNumberMatcher.find()) {
                String phoneNumber = phoneNumberMatcher.group(1);
                Log.d(TAG, phoneNumber);
                mContact.setPhone(phoneNumber);
            }
        }

        mContact.setPicturePath(mPicturePath);

        if (userIsUpdating)
            ContactLab.get(this).updateContact(mContact);
        else
            ContactLab.get(this).addContact(mContact);


        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PHOTO  && data != null) {
            mPicturePath = mPhotoFile.getAbsolutePath();

            if (radioGroup.getCheckedRadioButtonId() == 0)
                loadImage(Contact.MALE);
            else
                loadImage(Contact.FEMALE);

        } else if (requestCode == SET_LOCATION_RESULT  && data != null) {

        } else if (requestCode == SET_TAGS_RESULT && data != null) {
            ContactLab.Tag mTag = (ContactLab.Tag) data.getSerializableExtra(AddTagsActivity.TAG_RESULT);
//            StringBuilder stringBuilder = new StringBuilder();
//            if (!mTagsTextView.getText().toString().isEmpty()) {
//                stringBuilder.append(mTagsTextView.getText().toString());
//                stringBuilder.append(", ");
//            }
//            stringBuilder.append(mTag.getName());
//
//            mTagsTextView.setText(stringBuilder.toString());

            mContact.addTag(mTag);

            updateTags();

        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();

            // specify fields you want to return values for
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            // perform query, contactUri is like a "where" clause here
            Cursor cursor = this.getContentResolver().query(contactUri, queryFields, null, null, null);

            try {
                // double-check for result
                if (cursor.getCount() == 0) return;

                // pull out the first column of the first row of data
                cursor.moveToFirst();
                String contact = cursor.getString(0);
                nameEditText.setText(contact);
            } finally {
                cursor.close();
            }
        } else if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

            try {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                // String picturePath contains the path of selected Image
                mPicturePath = cursor.getString(columnIndex);
                Log.d(TAG, "mPicturePath = " + mPicturePath);

                if (radioGroup.getCheckedRadioButtonId() == 0)
                    loadImage(Contact.MALE);
                else
                    loadImage(Contact.FEMALE);

            } finally {
                cursor.close();
            }
        }

    }

    // Option Menu

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // check if we can import from contacts, if not disable the button
        PackageManager packageManager = getPackageManager();
        pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            menu.findItem(R.id.action_contacts).setEnabled(false);
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.add_contact_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.action_done:
                addContact();
                return true;

            case R.id.action_contacts:
                startActivityForResult(pickContact, REQUEST_CONTACT);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void deleteContact(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.are_you_sure)
                .setTitle("Delete Contact");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                ContactLab.get(AddContactActivity.this).deleteContact(mContact);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("AddContact Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        AppIndex.AppIndexApi.start(mClient, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mClient, getIndexApiAction());
        mClient.disconnect();
    }
}
