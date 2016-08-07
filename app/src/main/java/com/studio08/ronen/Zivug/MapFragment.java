package com.studio08.ronen.Zivug;

import android.location.Location;
import android.os.AsyncTask;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.SupportMapFragment;

public class MapFragment extends SupportMapFragment {
    private GoogleApiClient mClient;

    private class SearchTask extends AsyncTask<Location, Void, Void> {

        @Override
        protected Void doInBackground(Location... locations) {
            return null;
        }
    }
}
