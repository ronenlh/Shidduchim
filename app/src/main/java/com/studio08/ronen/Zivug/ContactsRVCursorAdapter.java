package com.studio08.ronen.Zivug;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.UUID;

/**
 * Created by Ronen on 9/8/16.
 */

public class ContactsRVCursorAdapter extends CursorRecyclerAdapter<ContactsRVCursorAdapter.ContactHolder> {
    private Context context;
    String contactId;

    public ContactsRVCursorAdapter(Context context, Cursor cursor) {
        super(cursor);
        this.context = context;
    }

    @Override
    public ContactsRVCursorAdapter.ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_item_contact, parent, false);

        return new ContactsRVCursorAdapter.ContactHolder(view);
    }

    @Override
    public void onBindViewHolderCursor(ContactsRVCursorAdapter.ContactHolder holder, Cursor cursor) {
        cursor.moveToFirst();
        contactId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_ENTRY_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_NAME));
        String gender = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_GENDER));
        String age = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_AGE));
        long image = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_IMAGE_RESOURCE));
        String notes = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_NOTES));
        String location = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_LOCATION));
        String tags = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_TAGS));
        String dates = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_PREV_DATES));

        // bind viewHolder's view to model object
        holder.mFirstNameTextView.setText(name);
//        holder.mLastNameTextView.setText(mContacts.get(position).getLastName());
        holder.mPictureImageView.setImageResource((int) image);
    }

    class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mFirstNameTextView;
        TextView mLastNameTextView;
        CircularImageView mPictureImageView;

        private Contact mContact;

        public ContactHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mFirstNameTextView = (TextView) itemView.findViewById(R.id.first_name_tw);
            mLastNameTextView = (TextView) itemView.findViewById(R.id.last_name_tw);
            mPictureImageView = (CircularImageView) itemView.findViewById(R.id.contact_iw);
        }

        @Override
        public void onClick(View view) {
            Intent intent = ContactActivity.newIntent(context, UUID.fromString(contactId));
            context.startActivity(intent);
        }
    }
}


