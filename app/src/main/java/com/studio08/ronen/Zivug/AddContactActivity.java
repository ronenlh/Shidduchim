package com.studio08.ronen.Zivug;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;


public class AddContactActivity extends AppCompatActivity {

    private static String TAG = "AddContactActivity";
    private static final int SET_LOCATION_RESULT = 1121;
    private static final int SET_TAGS_RESULT = 1122;
    private static final int REQUEST_CONTACT = 1123;
    private static final int REQUEST_PHOTO = 1124;
    private static final int REQUEST_GALLERY = 1125;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;

    private Contact mContact;

    private RadioGroup radioGroup;
    private RadioButton maleRadioButton, femaleRadioButton;
    private ImageView mPhotoImage, mGalleryImage;
    private CircularImageView mImageView;
    private File mPhotoFile;
    int genderSelection = 2;
    int imageResourceId;

    Intent pickContact, captureImageIntent, galleryImageIntent;

    EditText nameEditText, ageEditText, notesEditText, phoneEditText, emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        mContact = new Contact();

        initViews();

        mPhotoFile = ContactLab.get(this).getPhotoFile(mContact);

        captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        galleryImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

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
                Log.d(TAG, "onClick");
                // for the new API 23 dangerous permission model
                requestGalleryPermission();
            }
        });


    }

    private void initViews() {
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        genderSelection = getIntent().getIntExtra(MainActivity.EXTRA_GENDER, 2);
        mImageView = (CircularImageView) findViewById(R.id.add_profile_pic);
        maleRadioButton = (RadioButton) findViewById(R.id.male_selection);
        femaleRadioButton = (RadioButton) findViewById(R.id.female_selection);

        mPhotoImage = (ImageView) findViewById(R.id.camera_image);
        mGalleryImage = (ImageView) findViewById(R.id.gallery_image);

        nameEditText = (EditText) findViewById(R.id.add_name);
        ageEditText = (EditText) findViewById(R.id.add_age);
        notesEditText = (EditText) findViewById(R.id.add_notes);
//        phoneEditText = (EditText) findViewById(R.id.add_phone);
//        emailEditText = (EditText) findViewById(R.id.add_email);

        // default values depending on where was the activity opened
        switch (genderSelection) {
            case Contact.MALE:
                maleRadioButton.setChecked(true);
                femaleRadioButton.setChecked(false);
                imageResourceId = Contact.getInitialFillerResourceId(Contact.MALE);
                mImageView.setImageResource(imageResourceId);
                break;
            case Contact.FEMALE:
                maleRadioButton.setChecked(false);
                femaleRadioButton.setChecked(true);
                imageResourceId = Contact.getInitialFillerResourceId(Contact.MALE);
                mImageView.setImageResource(imageResourceId);
                break;
            case Contact.NOT_SET:
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestGalleryPermission() {
        // https://developer.android.com/training/permissions/requesting.html

        Log.d(TAG, "requestGalleryPermission");

        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.\
            } else {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "permission was granted");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startActivityForResult(galleryImageIntent, REQUEST_GALLERY);

                } else {
                    Log.d(TAG, "permission denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.male_selection:
                if (checked)
                    mImageView.setImageResource(R.drawable.avatar_01);
                    break;
            case R.id.female_selection:
                if (checked)
                    mImageView.setImageResource(R.drawable.avatar_21);
                    break;
        }
    }

    public void setLocation(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivityForResult(intent, SET_LOCATION_RESULT);
    }

    public void setTags(View view) {
        Intent intent = new Intent(this, AddTagsActivity.class);
        startActivityForResult(intent, SET_TAGS_RESULT);
    }

    private void addContact() {
        if (nameEditText.getText().toString().trim().equals("")) {
            Toast.makeText(this, R.string.no_name_inserted, Toast.LENGTH_SHORT).show();
            return;
        }
        if(radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, R.string.no_gender_selected, Toast.LENGTH_SHORT).show();
            return;
        }

        String name = nameEditText.getText().toString();
        int age = Integer.parseInt(ageEditText.getText().toString());
        int gender = maleRadioButton.isChecked()? Contact.MALE : Contact.FEMALE;
        String notes = notesEditText.getText().toString();
//        String phone = phoneEditText.getText().toString();
//        String email = emailEditText.getText().toString();

        mContact.setName(name);
        mContact.setGender(gender);
        mContact.setNotes(notes);
        mContact.setAge(age);
//        mContact.setPhone(phone);
//        mContact.setEmail(email);

        ContactLab.get(this).addContact(mContact);

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SET_LOCATION_RESULT) {

        } else if (requestCode == SET_TAGS_RESULT) {

        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();

            // specify fields you want to return values for
            String[] queryFields = new String[] {
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
        } else if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK &&  data != null) {

            Uri selectedImage = data.getData();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

            try {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                // String picturePath contains the path of selected Image
                String picturePath = cursor.getString(columnIndex);
                mContact.setPicturePath(picturePath);
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

    public void takePicture(View view) {

    }

    public void selectPicture(View view) {

    }
}
