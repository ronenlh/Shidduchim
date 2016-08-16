package com.studio08.ronen.Zivug.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.studio08.ronen.Zivug.Model.ContactLab;
import com.studio08.ronen.Zivug.R;

import java.util.List;

public class AddTagsActivity extends AppCompatActivity {

    AutoCompleteTextView mTextView;
    ContactLab.Tag mTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tags);

        mTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_tags);

        // Autocomplete tags
        List<ContactLab.Tag> mTags = ContactLab.get(this).getTags();
        String[] tags = new String[mTags.size()];
        for (int i = 0; i < tags.length; i++) {
            tags[i] = mTags.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, tags);
        mTextView.setAdapter(adapter);
    }


    public void addTag(View view) {
        String text = mTextView.getText().toString();

        mTag = new ContactLab.Tag(text);
        ContactLab.get(this).addTag(mTag);

        Toast.makeText(this, "Tag " + mTag.getName() + " Added", Toast.LENGTH_SHORT).show();
    }
}
