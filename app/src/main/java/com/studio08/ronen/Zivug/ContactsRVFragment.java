package com.studio08.ronen.Zivug;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studio08.ronen.Zivug.Model.Contact;
import com.studio08.ronen.Zivug.Model.ContactLab;
import com.studio08.ronen.Zivug.Model.ContactsRVCursorAdapter;
import com.studio08.ronen.Zivug.Model.DatabaseContract;

//import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactsRVFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactsRVFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsRVFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_GENDER = "gender";

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 3;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    private int mGenderParam = 2; // after getInt

    protected LayoutManagerType mCurrentLayoutManagerType;

//    protected RadioButton mLinearLayoutRadioButton;
//    protected RadioButton mGridLayoutRadioButton;

    protected RecyclerView mRecyclerView;
    protected ContactsRVCursorAdapter mCursorAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected SwipeRefreshLayout swLayout;

    private OnFragmentInteractionListener mListener;

    public ContactsRVFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param gender the gender of the Contacts Fragment.
     * @return A new instance of fragment ContactsRVFragment.
     */
    public static ContactsRVFragment newInstance(int gender) {
        ContactsRVFragment fragment = new ContactsRVFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GENDER, gender);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGenderParam = getArguments().getInt(ARG_GENDER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.contacts_rv);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        updateUI();

        // change the layout of the rv from the UI, maybe from settings
        /*mLinearLayoutRadioButton = (RadioButton) rootView.findViewById(R.id.linear_layout_rb);
        mLinearLayoutRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
            }
        });

        mGridLayoutRadioButton = (RadioButton) rootView.findViewById(R.id.grid_layout_rb);
        mGridLayoutRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);
            }
        });*/

        swLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swlayout);
        swLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);
        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateUI();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        return rootView;
    }

    public void updateUI() {
        ContactLab contactLab = ContactLab.get(getContext());
        Cursor cursor;

        if (mGenderParam == Contact.MALE)
            cursor = contactLab.queryContactsTable(
                    DatabaseContract.Entry.COLUMN_NAME_GENDER + " MATCH ?",
                    new String[] { "" + Contact.MALE }
            );
        else if (mGenderParam == Contact.FEMALE)
            cursor = contactLab.queryContactsTable(
                    DatabaseContract.Entry.COLUMN_NAME_GENDER + " MATCH ?",
                    new String[] { "" + Contact.FEMALE }
            );
        else cursor = ContactLab.get(getContext()).queryContactsTable(null, null);

        if (mCursorAdapter == null) {
            mCursorAdapter = new ContactsRVCursorAdapter(getContext(), cursor);
            mRecyclerView.setAdapter(mCursorAdapter);
        } else {
            mCursorAdapter.swapCursor(cursor);
        }

    }

    public void searchContacts(String query) {

        Cursor cursor = ContactLab.get(getContext()).getWordMatches(query);

        if (mCursorAdapter == null)
            mCursorAdapter = new ContactsRVCursorAdapter(getContext(), cursor);
        else
            mCursorAdapter.swapCursor(cursor);

        if (mRecyclerView != null)
            mRecyclerView.setAdapter(mCursorAdapter);
    }

    public void searchbyTags(ContactLab.Tag[] tags) {
        Cursor cursor = ContactLab.get(getContext()).getTagMatches(tags, mGenderParam);

        if (mCursorAdapter == null)
            mCursorAdapter = new ContactsRVCursorAdapter(getContext(), cursor);
        else
            mCursorAdapter.swapCursor(cursor);

        if (mRecyclerView != null)
            mRecyclerView.setAdapter(mCursorAdapter);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onContactsRVFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
            mListener = (OnFragmentInteractionListener) context;
        else throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type
        void onContactsRVFragmentInteraction(Uri uri);
    }
}


