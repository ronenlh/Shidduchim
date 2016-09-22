package com.studio08.ronen.Zivug.Activities;



import android.support.v4.app.DialogFragment;

import com.studio08.ronen.Zivug.Model.ContactLab;
import com.studio08.ronen.Zivug.R;

import java.util.UUID;

public class AddLocationActivity extends AddFilterActivity<ContactLab.Location> {

    public static final String TAG_RESULT = "tag";
    private static final String TAG = "AddLocationActivity";

    void loadData() {
        mFilterList = ContactLab.get(this).getLocations();
        this.mStringFilter = new String[mFilterList.size()];
        for (int i = 0; i < this.mStringFilter.length; i++) {
            this.mStringFilter[i] = mFilterList.get(i).getName();
        }
    }

    @Override
    DialogFragment setDialogFragment() {
        return new AddLocationDialogFragment();
    }

    @Override
    public void onAddSelected(ContactLab.Filter filter) {
        ContactLab.get(this).addLocation((ContactLab.Location) filter);
        loadData();
        setData();
    }

    @Override
    public void onEditSelected(ContactLab.Filter filter) {
        ContactLab.get(this).updateLocation((ContactLab.Location) filter);
        loadData();
        setData();
    }

    public static class AddLocationDialogFragment extends AddFilterDialogFragment<ContactLab.Location> {
        @Override
        ContactLab.Location getFilter(UUID mId) {
            return ContactLab.get(getContext()).getLocation(mId);

        }

        @Override
        void setButtonText() {
            updateButton = R.string.update_location;
            addButton = R.string.add_location;
        }

        @Override
        protected ContactLab.Location createFilter(String name) {
            return new ContactLab.Location(name);
        }
    }
}