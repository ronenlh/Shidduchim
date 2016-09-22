package com.studio08.ronen.Zivug.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
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

import com.studio08.ronen.Zivug.Model.ContactLab;
import com.studio08.ronen.Zivug.R;

import java.util.List;
import java.util.UUID;

abstract class AddFilterActivity<E extends ContactLab.Filter> extends AppCompatActivity implements AdapterView.OnItemClickListener, AddFilterDialogFragment.onAddSelectedListener, AdapterView.OnItemLongClickListener {

    public static final String FIlTER_RESULT = "tag";
    private static final String TAG = "AddFilterActivity";
    private AutoCompleteTextView mTextView;
    private ListView mListView;
    private ActionMode mActionMode;

    protected String[] mStringFilter;
    protected List<E> mFilterList;

    protected DialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_filter);

//        mTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_tags);
        mListView = (ListView) findViewById(R.id.add_filter_listview);

        loadData();
        setData();

        dialogFragment = setDialogFragment();
    }



    abstract void loadData();

    abstract DialogFragment setDialogFragment();

    protected void setData() {
        mListView.setAdapter(new ArrayAdapter<>(this,
                R.layout.filter_list_item, R.id.filter_list_text, this.mStringFilter));

        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.add_filter_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.action_add:
                addItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void addItem() {
        openDialogFragment("");
    }

    private void openDialogFragment(String arg) {

        Bundle args = new Bundle();
        args.putString("id", arg);
        dialogFragment.setArguments(args);

        dialogFragment.show(getSupportFragmentManager(), "FilterDialogFragment");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(FIlTER_RESULT, mFilterList.get(i));
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (mActionMode != null) {
            return false;
        }
        UUID itemId = mFilterList.get(i).getId();
        mActionMode = startSupportActionMode(mActionModeCallback);
        mActionMode.setTag(itemId.toString());
        view.setSelected(true);
        Log.d(TAG, "onItemLongClick: itemId "+ itemId.toString());
        return true;
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

                    openDialogFragment((String) mode.getTag());

                    mode.finish(); // Action picked, so close the CAB
                    return true;
                case R.id.action_delete:
                    ContactLab.get(AddFilterActivity.this).deleteTag(UUID.fromString(mode.getTag().toString()));

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
