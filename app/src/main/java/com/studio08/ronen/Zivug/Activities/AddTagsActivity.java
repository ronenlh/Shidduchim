package com.studio08.ronen.Zivug.Activities;



import android.support.v4.app.DialogFragment;

import com.studio08.ronen.Zivug.Model.ContactLab;
import com.studio08.ronen.Zivug.R;

import java.util.UUID;

public class AddTagsActivity extends AddFilterActivity<ContactLab.Tag> {

    public static final String TAG_RESULT = "tag";
    private static final String TAG = "AddTagsActivity";

    void loadData() {
        mFilterList = ContactLab.get(this).getTags();
        this.mStringFilter = new String[mFilterList.size()];
        for (int i = 0; i < this.mStringFilter.length; i++) {
            this.mStringFilter[i] = mFilterList.get(i).getName();
        }
    }

    @Override
    DialogFragment setDialogFragment() {
        return new AddTagDialogFragment();
    }

    @Override
    public void onAddSelected(ContactLab.Filter filter) {
        ContactLab.get(this).addTag((ContactLab.Tag) filter);
        loadData();
        setData();
    }

    @Override
    public void onEditSelected(ContactLab.Filter filter) {
        ContactLab.get(this).updateTag((ContactLab.Tag) filter);
        loadData();
        setData();
    }

    public static class AddTagDialogFragment extends AddFilterDialogFragment<ContactLab.Tag> {
        @Override
        ContactLab.Tag getFilter(UUID mId) {
            return ContactLab.get(getContext()).getTag(mId);
        }

        @Override
        void setButtonText() {
            updateButton = R.string.update_tag;
            addButton = R.string.add_tag;
        }

        @Override
        protected ContactLab.Tag createFilter(String name) {
            return new ContactLab.Tag(name);
        }
    }
}