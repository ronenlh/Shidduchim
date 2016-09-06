package com.studio08.ronen.Zivug.Activities;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.studio08.ronen.Zivug.Model.ContactLab;
import com.studio08.ronen.Zivug.R;

import java.util.List;

public class AddTagsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AddTagDialogFragment.onAddSelectedListener {

    public static final String TAG_RESULT = "tag";
    private AutoCompleteTextView mTextView;
    private ListView mListView;
    private ContactLab.Tag mTag;
    private String[] mStringTags;
    private List<ContactLab.Tag> mTags;

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
        mTags = ContactLab.get(this).getTags();
        this.mStringTags = new String[mTags.size()];
        for (int i = 0; i < this.mStringTags.length; i++) {
            this.mStringTags[i] = mTags.get(i).getName();
        }
    }

    private void setData() {
        mTextView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, this.mStringTags));

        mListView.setAdapter(new ArrayAdapter<>(this,
                R.layout.tag_list_item, R.id.tag_list_text, this.mStringTags));

        mListView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.add_tag_menu, menu);

        return true;
    }

    public void addTag(View view) {
//        String text = mTextView.getText().toString();
//
//        mTag = new ContactLab.Tag(text);
//        ContactLab.get(this).addTag(mTag);
//
//        Toast.makeText(this, "Tag " + mTag.getName() + " Added", Toast.LENGTH_SHORT).show();

        DialogFragment dialogFragment = new AddTagDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "AddTagDialogFragment");


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(TAG_RESULT, mTags.get(i));
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onAddSelected(String tag) {
        mTag = new ContactLab.Tag(tag);
        ContactLab.get(this).addTag(mTag);
        loadData();
        setData();
    }
}
