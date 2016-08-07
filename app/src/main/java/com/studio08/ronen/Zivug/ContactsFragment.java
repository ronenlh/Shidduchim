package com.studio08.ronen.Zivug;

import android.content.Context;
import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_GENDER = "gender";

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 3;
    private static final int DATASET_COUNT = 60;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    private int mGenderParam; // after getInt

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RadioButton mLinearLayoutRadioButton;
    protected RadioButton mGridLayoutRadioButton;

    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected SwipeRefreshLayout swLayout;

    protected List<Contact> mMaleContacts;
    protected List<Contact> mFemaleContacts;

    private OnFragmentInteractionListener mListener;

    public ContactsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param gender the gender of the Contacts Fragment.
     * @return A new instance of fragment ContactsFragment.
     */
    public static ContactsFragment newInstance(int gender) {
        ContactsFragment fragment = new ContactsFragment();
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

        // sample contact list
        if (mGenderParam == Contact.MALE) mMaleContacts = new ArrayList<>();
        else mFemaleContacts = new ArrayList<>();
        initSampleDataset(mGenderParam);
    }

    private void initSampleDataset(int genderParam) {
        if (mGenderParam == Contact.MALE) {
            for (int i = 1; i < 21; i++) {
                Contact contact = new Contact("Male Contact", "" + i, 12, Contact.MALE);
                mMaleContacts.add(contact);
            }
        } else if (mGenderParam == Contact.FEMALE) {
            for (int i = 1; i < 21; i++) {
                Contact contact = new Contact("Female Contact", "" + i, 25, Contact.FEMALE);
                mFemaleContacts.add(contact);
            }
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

        if (mGenderParam == Contact.MALE) mAdapter = new CustomAdapter(getContext(), mMaleContacts);
        else if (mGenderParam == Contact.FEMALE) mAdapter = new CustomAdapter(getContext(), mFemaleContacts);

        mRecyclerView.setAdapter(mAdapter);

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
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swLayout.setRefreshing(false);
                    }
                }, 5000);
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onContactsRVFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
        mAdapter.notifyDataSetChanged();
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

    private class CustomAdapter extends RecyclerView.Adapter<ContactHolder> {

        private List<Contact> mContacts;
        private Context context;

        public CustomAdapter(Context context, List<Contact> contacts) {
            this.context = context;
            this.mContacts = contacts;
        }

        @Override
        public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // called by the RecyclerView when it needs a new view to display an item
            // I create a view and wrap it in a ViewHolder
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(R.layout.list_item_contact, parent, false);
            return new ContactHolder(view);
        }

        @Override
        public void onBindViewHolder(ContactHolder holder, int position) {
            // bind viewHolder's view to model object
            holder.mContact = mContacts.get(position);
            holder.mFirstNameTextView.setText(mContacts.get(position).getFirstName());
            holder.mLastNameTextView.setText(mContacts.get(position).getLastName());
            holder.mPictureImageView.setImageResource(mContacts.get(position).getResourceId());
        }

        @Override
        public int getItemCount() {
            return mContacts.size();
        }
    }

    private class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mFirstNameTextView;
        TextView mLastNameTextView;
        CircleImageView mPictureImageView;

        private Contact mContact;

        public ContactHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mFirstNameTextView = (TextView) itemView.findViewById(R.id.first_name_tw);
            mLastNameTextView = (TextView) itemView.findViewById(R.id.last_name_tw);
            mPictureImageView = (CircleImageView) itemView.findViewById(R.id.contact_iw);
        }

        @Override
        public void onClick(View view) {
            Intent intent = ContactActivity.newIntent(getActivity(), mContact.getId());
            startActivity(intent);
        }
    }
}


