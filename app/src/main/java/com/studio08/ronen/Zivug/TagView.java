package com.studio08.ronen.Zivug;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.studio08.ronen.Zivug.Model.ContactLab;

import java.util.UUID;

/**
 * Created by Ronen on 22/8/16.
 */

public class TagView extends LinearLayout {

    private static final String TAG = "TagView";
    private ContactLab.Tag mTag;
    private Context mContext;
    private OnTagDeletedListener mCallback;

    public void setObject(ContactLab.Tag tag) {
        mTag = tag;
    }


    public interface OnTagDeletedListener {
        void tagDeleted(ContactLab.Tag UUID);
    }

    public TagView(Context context, ContactLab.Tag tag) {
        super(context);
        this.mTag = tag;
        init(context);
    }

    public TagView(Context context) {
        super(context);
        init(context);
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TagView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;

        try {
            mCallback = (OnTagDeletedListener) mContext;
        } catch (ClassCastException e) {
            Log.w(TAG, "init: " + mContext.toString() + " must implement OnTagDeletedListener", e);
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.tag_view, this, true);

        TextView textView = (TextView) layout.findViewById(R.id.tag_name);
        TextView x = (TextView) layout.findViewById(R.id.XButton);

        if (mTag == null) return;

        textView.setText(mTag.getName());

        x.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.tagDeleted(mTag);
            }
        });
    }
}
