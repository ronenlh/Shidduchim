package com.studio08.ronen.Zivug;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.UUID;


public class EditContactActivity extends AppCompatActivity {

    public static final String EXTRA_CONTACT_ID = "UUID";

    private static String TAG = "EditContactActivity";
    private static int setLocation_RESULT = 1121;
    private static int setTags_RESULT = 1122;

    private RadioGroup radioGroup;
    private RadioButton maleRadioButton, femaleRadioButton;
    private CircularImageView mImageView;
    int imageResourceId;

    EditText nameEditText, ageEditText, notesEditText;

    Contact mContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        // get the specific contact
        UUID contactId = (UUID) getIntent().getSerializableExtra(EXTRA_CONTACT_ID);
        mContact = ContactLab.get(this).getContact(contactId);

        initViews();
        fillViews();

    }

    private void initViews() {
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mImageView = (CircularImageView) findViewById(R.id.add_profile_pic);
        maleRadioButton = (RadioButton) findViewById(R.id.male_selection);
        femaleRadioButton = (RadioButton) findViewById(R.id.female_selection);

        nameEditText = (EditText) findViewById(R.id.add_name);
        ageEditText = (EditText) findViewById(R.id.add_age);
        notesEditText = (EditText) findViewById(R.id.add_notes);

        // default values depending on where was the activity opened
        switch (mContact.getGender()) {
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

    private void fillViews() {
        nameEditText.setText(mContact.getName());
        ageEditText.setText(""+mContact.getAge());
        notesEditText.setText(mContact.getNotes());
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

        String name = nameEditText.getText().toString();
        int age = Integer.parseInt(ageEditText.getText().toString());
        int gender = maleRadioButton.isChecked()? Contact.MALE : Contact.FEMALE;
        String notes = notesEditText.getText().toString();

        mContact.setName(name);
        mContact.setGender(gender);
        mContact.setNotes(notes);
        mContact.setAge(age);

        ContactLab.get(this).updateContact(mContact);

        finish();
    }

    public void deleteContact(View view) {
        ContactLab.get(this).deleteContact(mContact);
        // need to write this on the main activity
//        Snackbar.make(view, R.string.contact_deleted, Snackbar.LENGTH_LONG)
//                        .setAction(R.string.undo_delete_contact, null).show();

        finish();
    }
}
