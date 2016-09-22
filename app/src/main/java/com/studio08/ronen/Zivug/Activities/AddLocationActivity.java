package com.studio08.ronen.Zivug.Activities;



import com.studio08.ronen.Zivug.Model.ContactLab;

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
}