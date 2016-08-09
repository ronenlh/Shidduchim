package com.studio08.ronen.Zivug;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.UUID;


public class AddContactActivity extends AppCompatActivity {

    private static String TAG = "AddContactActivity";
    private static int setLocation_RESULT = 1121;
    private static int setTags_RESULT = 1122;

    private RadioGroup radioGroup;
    private RadioButton maleRadioButton, femaleRadioButton;
    private CircularImageView mImageView;
    int genderSelection = 2;

    EditText nameEditText, ageEditText, notesEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        genderSelection = getIntent().getIntExtra(MainActivity.EXTRA_GENDER, 2);
        mImageView = (CircularImageView) findViewById(R.id.add_profile_pic);
        maleRadioButton = (RadioButton) findViewById(R.id.male_selection);
        femaleRadioButton = (RadioButton) findViewById(R.id.female_selection);

        nameEditText = (EditText) findViewById(R.id.add_name);
        ageEditText = (EditText) findViewById(R.id.add_age);
        notesEditText = (EditText) findViewById(R.id.add_notes);

        // default values depending on where was the activity opened
        switch (genderSelection) {
            case Contact.MALE:
                maleRadioButton.setChecked(true);
                femaleRadioButton.setChecked(false);
                mImageView.setImageResource(Contact.getFillerResourceId(Contact.MALE));
                break;
            case Contact.FEMALE:
                maleRadioButton.setChecked(false);
                femaleRadioButton.setChecked(true);
                mImageView.setImageResource(Contact.getFillerResourceId(Contact.FEMALE));
                break;
            case Contact.NOT_SET:
                break;
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
        startActivityForResult(intent, setLocation_RESULT);
    }

    public void setTags(View view) {
        Intent intent = new Intent(this, AddTagsActivity.class);
        startActivityForResult(intent, setTags_RESULT);
    }

    public void addContact(View view) {

        if (nameEditText.getText().toString().trim().equals("")) {
            Toast.makeText(this, R.string.no_name_inserted, Toast.LENGTH_SHORT).show();
            return;
        }
        if(radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, R.string.no_gender_selected, Toast.LENGTH_SHORT).show();
            return;
        }

        String id = UUID.randomUUID().toString();
        String name = nameEditText.getText().toString();
        String age = ageEditText.getText().toString();
        int gender = maleRadioButton.isChecked()? Contact.MALE : Contact.FEMALE;
        String notes = notesEditText.getText().toString();


        DatabaseHelper mDbHelper = new DatabaseHelper(this);

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Entry.COLUMN_NAME_ENTRY_ID, id);
        values.put(DatabaseContract.Entry.COLUMN_NAME_NAME, name);
        values.put(DatabaseContract.Entry.COLUMN_NAME_GENDER, gender);
        values.put(DatabaseContract.Entry.COLUMN_NAME_AGE, age);
        values.put(DatabaseContract.Entry.COLUMN_NAME_NOTES, notes);
//        values.put(DatabaseContract.Entry.COLUMN_NAME_LOCATION, content);
//        values.put(DatabaseContract.Entry.COLUMN_NAME_TAGS, content);
//        values.put(DatabaseContract.Entry.COLUMN_NAME_PREV_DATES, content);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(
                DatabaseContract.Entry.TABLE_NAME,
                // the name of a column in which the framework can insert NULL in the event that the ContentValues is empty
                DatabaseContract.Entry.COLUMN_NAME_NULLABLE,
                values);

        if (newRowId < 0) Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Row Id: "+newRowId );
    }
}
