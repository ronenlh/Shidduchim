package com.studio08.ronen.Zivug;

import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.api.GoogleApiClient;

// this should be mapfragment but leaving this for now so i dont have to import dependencies
public class MapFragment extends Fragment {
    private GoogleApiClient mClient;

    private class SearchTask extends AsyncTask<Location, Void, Void> {

        @Override
        protected Void doInBackground(Location... locations) {
            return null;
        }
    }
}
