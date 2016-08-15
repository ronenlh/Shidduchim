package com.studio08.ronen.Zivug.Model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.studio08.ronen.Zivug.Activities.AddContactActivity;
import com.studio08.ronen.Zivug.Activities.ContactActivity;
import com.studio08.ronen.Zivug.CursorRecyclerAdapter;
import com.studio08.ronen.Zivug.R;

import java.util.UUID;

/**
 * Created by Ronen on 9/8/16.
 */

public class ContactsRVCursorAdapter extends CursorRecyclerAdapter<ContactsRVCursorAdapter.ContactHolder> {
    private Context mContext;

    public ContactsRVCursorAdapter(Context context, Cursor cursor) {
        super(cursor);
        this.mContext = context;
    }

    @Override
    public ContactsRVCursorAdapter.ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_item_contact, parent, false);

        return new ContactsRVCursorAdapter.ContactHolder(view);
    }

    @Override
    public void onBindViewHolderCursor(ContactsRVCursorAdapter.ContactHolder holder, Cursor cursor) {

        String uuidString = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_ENTRY_UUID));
        Contact mContact = ContactLab.get(mContext).getContact(UUID.fromString(uuidString));

        // bind viewHolder's view to model object
        holder.mNameTextView.setText(mContact.getName());

        String mPicturePath = mContact.getPicturePath(); // why is picturePath null?? resolved: forgot to update getContentValues method in ContactLav and the ContactCursorWraper

        int mPlaceholder = (mContact.getGender() == Contact.MALE)? R.drawable.male_avatar : R.drawable.female_avatar;

            Picasso.with(mContext)
                    .load("file://" + mPicturePath)  // if empty mPicturePath is null then placeholder is shown.
                    .fit().centerCrop()
//                    .placeholder(mPlaceholder)
                    .error(mPlaceholder)
                    .into(holder.mPictureImageView);

        // This disable hardware acceleration to fix a bug
        // https://github.com/hdodenhof/CircleImageView/issues/31
        holder.mItemLinearLayout.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        holder.mView.setTag(mContact.getId());
    }

    class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView mNameTextView;
        CircularImageView mPictureImageView;
        View mView;
        LinearLayout mItemLinearLayout;

        public ContactHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mNameTextView = (TextView) itemView.findViewById(R.id.name_tv);
            mPictureImageView = (CircularImageView) itemView.findViewById(R.id.contact_iw);
            mView = itemView;
            mItemLinearLayout = (LinearLayout) itemView.findViewById(R.id.item_linear_layout);
        }

        @Override
        public void onClick(View view) {
            Intent intent = ContactActivity.newIntent(mContext, (UUID) view.getTag());
            mContext.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View view) {
            Intent intent = new Intent(mContext, AddContactActivity.class);
            intent.putExtra(AddContactActivity.EXTRA_UPDATING, true);
            intent.putExtra(AddContactActivity.EXTRA_CONTACT_ID, (UUID) view.getTag());
            mContext.startActivity(intent);

            // true if the callback consumed the long click, false otherwise.
            return true;
        }
    }
}


