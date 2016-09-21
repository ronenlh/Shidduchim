package com.studio08.ronen.Zivug.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.studio08.ronen.Zivug.ContactsRVFragment;
import com.studio08.ronen.Zivug.Drawer.ExpandableListAdapter;
import com.studio08.ronen.Zivug.Model.Contact;
import com.studio08.ronen.Zivug.Model.ContactLab;
import com.studio08.ronen.Zivug.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ContactsRVFragment.OnFragmentInteractionListener, ExpandableListAdapter.OnChildItemClickListener {

    public static final String EXTRA_GENDER = "gender";
    private static final String TAG = MainActivity.class.getSimpleName();

    // Tabs Vars
    private TabLayout tabLayout;
    private ViewPager viewPager;

    // Fragments
    private ContactsRVFragment menFragment, womenFragment, currentFragment;

    // Tags and Locations in drawer properties
    private Map<String, List<ContactLab.Tag>> mNameAndListMap;
    private ExpandableListView expandableListView;
    public ExpandableListAdapter expandableListAdapter;
    private boolean isTagsExpanded, isLocationsExpanded;

    private DrawerLayout mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set logo toolbar
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo_zivug_white);

        // FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                addContact();
            }
        });

        // Drawer
        mDrawerList = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerList, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mDrawerList.setDrawerListener(toggle);
        mDrawerList.addDrawerListener(toggle);
        mDrawerList.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Log.d(TAG, "onDrawerOpened: " + drawerView.toString());
                updateTagsAndLocations();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // maybe not the best place, but record whether the items are expanded or not
                isTagsExpanded = expandableListView.isGroupExpanded(0);
                isLocationsExpanded = expandableListView.isGroupExpanded(1);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        setupTagsAndLocations();

        // Tabs setup
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupTagsAndLocations() {

        mNameAndListMap = new HashMap<>();
        mNameAndListMap.put("Tags",         ContactLab.get(this).getTags());
        mNameAndListMap.put("Locations",    ContactLab.get(this).getLocations());

        List<String> groupNames = new ArrayList<>();
        groupNames.add("Tags");
        groupNames.add("Locations");

        expandableListView = (ExpandableListView) findViewById(R.id.drawer_exp_list);
        expandableListAdapter = new ExpandableListAdapter(this, groupNames, mNameAndListMap);
        /* The choice mode has been moved from list view to adapter in order
            to not extend the class ExpansibleListView */
        expandableListAdapter.setChoiceMode(ExpandableListAdapter.CHOICE_MODE_MULTIPLE);
        expandableListView.setAdapter(expandableListAdapter);

        // Handle the click when the user clicks an any child
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                expandableListAdapter.setClicked(groupPosition, childPosition);
                return false;
            }
        });


        // expand them by default
        expandableListView.expandGroup(0);
        expandableListView.expandGroup(1);

//        // Set the adapter for the list view
//        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
//                R.layout.drawer_list_item, R.id.text_list_item, mSampleTitles));
//        // Set the list's click listener
//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
//
//        // Set the adapter for the list view
//        mDrawerList2.setAdapter(new ArrayAdapter<String>(this,
//                R.layout.drawer_list_item, R.id.text_list_item, mSampleTitles2));
//        // Set the list's click listener
//        mDrawerList2.setOnItemClickListener(new DrawerItemClickListener());

    }

    private void updateTagsAndLocations() {
        mNameAndListMap = new HashMap<>();
        mNameAndListMap.put("Tags",         ContactLab.get(this).getTags());
        mNameAndListMap.put("Locations",    ContactLab.get(this).getLocations());

        List<String> groupNames = new ArrayList<>();
        groupNames.add("Tags");
        groupNames.add("Locations");

        expandableListAdapter = new ExpandableListAdapter(this, groupNames, mNameAndListMap);
        /* The choice mode has been moved from list view to adapter in order
            to not extend the class ExpansibleListView */
        expandableListAdapter.setChoiceMode(ExpandableListAdapter.CHOICE_MODE_MULTIPLE);
        expandableListView.setAdapter(expandableListAdapter);

        if (isTagsExpanded) expandableListView.expandGroup(0);
        if (isLocationsExpanded) expandableListView.expandGroup(1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "QueryTextSubmit: " + query);
                searchContacts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "QueryTextChange: " + newText);
                searchContacts(newText);
                return false;
            }
        });

        return true;
    }

    private void searchContacts(String query) {
        // I need to update the cursor where clause with every onQueryTextChange,
        // not just for the name but for all fields
        if (currentFragment == null) currentFragment = menFragment;
        if (query.isEmpty()) currentFragment.updateUI();
        else currentFragment.searchContacts(query);
    }

    private void searchbyTags(ContactLab.Tag[] tags) {
        // I need to update the cursor where clause with every onQueryTextChange,
        // not just for the name but for all fields
        if (currentFragment == null) currentFragment = menFragment;
        currentFragment.searchbyTags(tags);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
        case R.id.action_settings:
            return true;
        case R.id.add_contact:
            addContact();
            return true;
        case R.id.action_login:
            Intent loginIntent = new Intent(this, OAuthActivity.class);
            startActivity(loginIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addContact() {
        Intent intent = new Intent(this, AddContactActivity.class);
        int position = tabLayout.getSelectedTabPosition(); // correlates with gender static vars in Contacts Fragment
        intent.putExtra(EXTRA_GENDER, position);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        menFragment = ContactsRVFragment.newInstance(Contact.MALE);
        adapter.addFragment(menFragment, "MEN");

        womenFragment = ContactsRVFragment.newInstance(Contact.FEMALE);
        adapter.addFragment(womenFragment, "WOMEN");

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentFragment = (position == 0)? menFragment : womenFragment;
                Log.d(TAG, "onPageSelected: currentFragment " + currentFragment.toString());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onContactsRVFragmentInteraction(Uri uri) {

    }

    @Override
    public void onChildItemClickCallback(SparseArray<SparseBooleanArray> checkedPositions) {
        Log.d(TAG, checkedPositions.toString());
        Log.d(TAG, "\t"+ mNameAndListMap.toString());

        SparseBooleanArray tagGroupBooleanArray = checkedPositions.get(0); // 0 is the tags array
        List<ContactLab.Tag> tagList = mNameAndListMap.get("Tags");


        List<ContactLab.Tag> tmpTagList = new ArrayList<>(tagGroupBooleanArray.size());

        for (int i = 0; i < tagList.size(); i++)
            if (tagGroupBooleanArray.get(i, false)) // false is a default value
                tmpTagList.add(tagList.get(i)); // if true I add it to the list to send only the selected true values to the query

        ContactLab.Tag[] tags = tmpTagList.toArray(new ContactLab.Tag[tmpTagList.size()]);

        searchbyTags(tags);

        // need to correlate checkedPositions with mNameAndListMap and then query the true Tag

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the contacts list by updating the cursor paramenter

    }
}
