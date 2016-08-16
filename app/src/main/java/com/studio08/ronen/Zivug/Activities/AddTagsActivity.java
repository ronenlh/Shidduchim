package com.studio08.ronen.Zivug.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.studio08.ronen.Zivug.Model.ContactLab;
import com.studio08.ronen.Zivug.R;

import java.util.List;

public class AddTagsActivity extends AppCompatActivity {

    private AutoCompleteTextView mTextView;
    private ListView mListView;
    private ContactLab.Tag mTag;
    private String[] mTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tags);

        mTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_tags);
        mListView = (ListView) findViewById(R.id.add_tags_listview);

        loadData();
        setData();
    }



    private void loadData() {
        List<ContactLab.Tag> mTags = ContactLab.get(this).getTags();
        this.mTags = new String[mTags.size()];
        for (int i = 0; i < this.mTags.length; i++) {
            this.mTags[i] = mTags.get(i).getName();
        }
    }

    private void setData() {
        mTextView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, this.mTags));

        mListView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, this.mTags));
    }


    public void addTag(View view) {
        String text = mTextView.getText().toString();

        mTag = new ContactLab.Tag(text);
        ContactLab.get(this).addTag(mTag);

        Toast.makeText(this, "Tag " + mTag.getName() + " Added", Toast.LENGTH_SHORT).show();

        loadData();
        setData();
    }
}
