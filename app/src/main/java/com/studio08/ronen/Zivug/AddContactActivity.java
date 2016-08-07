package com.studio08.ronen.Zivug;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddContactActivity extends AppCompatActivity {

    private RadioButton maleRadioButton, femaleRadioButton;
    private CircleImageView mImageView;
    int genderSelection = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        genderSelection = getIntent().getIntExtra(MainActivity.EXTRA_GENDER, 2);
        mImageView = (CircleImageView) findViewById(R.id.add_profile_pic);
        maleRadioButton = (RadioButton) findViewById(R.id.male_selection);
        femaleRadioButton = (RadioButton) findViewById(R.id.female_selection);

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
        startActivityForResult(intent, 1121);
    }
}
