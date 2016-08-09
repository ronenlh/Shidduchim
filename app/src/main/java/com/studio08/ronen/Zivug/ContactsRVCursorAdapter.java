package com.studio08.ronen.Zivug;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;
import java.util.UUID;

/**
 * Created by Ronen on 9/8/16.
 */

public class ContactsRVCursorAdapter extends CursorRecyclerAdapter<ContactsRVCursorAdapter.ContactHolder> {
    private Context context;

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

        String uuidString = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_ENTRY_UUID));
        Contact contact = ContactLab.get(context).getContact(UUID.fromString(uuidString));

        // bind viewHolder's view to model object
        holder.mNameTextView.setText(contact.getName());
        int resourceId = contact.getResourceId();
        Log.d("onBindViewHolderCursor", ""+resourceId);
        holder.mPictureImageView.setImageResource(resourceId);
        holder.mView.setTag(contact.getId());

        Log.i("ContactsRVCursorAdapter", "Cursor(0)" + cursor.getColumnName(0));
    }

    class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView mNameTextView;
        CircularImageView mPictureImageView;
        View mView;

        public ContactHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mNameTextView = (TextView) itemView.findViewById(R.id.name_tv);
            mPictureImageView = (CircularImageView) itemView.findViewById(R.id.contact_iw);
            mView = itemView;
        }

        @Override
        public void onClick(View view) {
            Intent intent = ContactActivity.newIntent(context, (UUID) view.getTag());
            context.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View view) {
            Intent intent = new Intent(context, EditContactActivity.class);
            intent.putExtra(EditContactActivity.EXTRA_CONTACT_ID, (UUID) view.getTag());
            context.startActivity(intent);

            // true if the callback consumed the long click, false otherwise.
            return true;
        }
    }
}


