package com.studio08.ronen.Zivug.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.studio08.ronen.Zivug.Model.ContactLab;
import com.studio08.ronen.Zivug.Model.DatabaseContract;
import com.studio08.ronen.Zivug.R;

import java.util.List;
import java.util.UUID;

import static java.security.AccessController.getContext;

public class AddTagsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AddTagDialogFragment.onAddSelectedListener, AdapterView.OnItemLongClickListener {

    public static final String TAG_RESULT = "tag";
    private static final String TAG = "AddTagsActivity";
    private AutoCompleteTextView mTextView;
    private ListView mListView;
    private ContactLab.Tag mTag;
    private String[] mStringTags;
    private List<ContactLab.Tag> mTags;
    private ActionMode mActionMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tags);

//        mTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_tags);
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
//        mTextView.setAdapter(new ArrayAdapter<>(this,
//                android.R.layout.simple_dropdown_item_1line, this.mStringTags));

        mListView.setAdapter(new ArrayAdapter<>(this,
                R.layout.tag_list_item, R.id.tag_list_text, this.mStringTags));

        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.add_tag_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.action_add:
                addTag();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void addTag() {
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
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (mActionMode != null) {
            return false;
        }
        UUID itemId = mTags.get(i).getId();
        mActionMode = startSupportActionMode(mActionModeCallback);
        mActionMode.setTag(itemId.toString());
        view.setSelected(true);
        Log.d(TAG, "onItemLongClick: itemId "+ itemId.toString());
        return true;
    }

    @Override
    public void onAddSelected(String tag) {
        mTag = new ContactLab.Tag(tag);
        ContactLab.get(this).addTag(mTag);
        loadData();
        setData();
    }

    private final ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.edit_menu, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }
        /** TODO: implement undo button, modularize */
        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_edit:


                    mode.finish(); // Action picked, so close the CAB
                    return true;
                case R.id.action_delete:
                    ContactLab.get(AddTagsActivity.this).deleteTag(UUID.fromString(mode.getTag().toString()));

                    loadData();
                    setData();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

}
