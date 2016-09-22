package com.studio08.ronen.Zivug.Drawer;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.studio08.ronen.Zivug.ContactsRVFragment;
import com.studio08.ronen.Zivug.Model.ContactLab;
import com.studio08.ronen.Zivug.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Ronen on 14/8/16.
 * Multiple choices from https://github.com/jiahaoliuliu/ExpandableListViewWithChoice
 */

public class ExpandableListAdapter extends android.widget.BaseExpandableListAdapter {

    public interface OnChildItemClickListener {
        void onChildItemClickCallback(SparseArray<SparseBooleanArray> checkedChildPositionsMultiple);
    }

    /**
     * Multiple choice for all the groups
     */
    public static final int CHOICE_MODE_MULTIPLE = AbsListView.CHOICE_MODE_MULTIPLE;

    // TODO: Coverage this case
    // Example:
    //https://github.com/commonsguy/cw-omnibus/blob/master/ActionMode/ActionModeMC/src/com/commonsware/android/actionmodemc/ActionModeDemo.java
    public static final int CHOICE_MODE_MULTIPLE_MODAL = AbsListView.CHOICE_MODE_MULTIPLE_MODAL;

    /**
     * No child could be selected
     */
    public static final int CHOICE_MODE_NONE = AbsListView.CHOICE_MODE_NONE;

    /**
     * One single choice per group
     */
    public static final int CHOICE_MODE_SINGLE_PER_GROUP = AbsListView.CHOICE_MODE_SINGLE;

    /**
     * One single choice for all the groups
     */
    public static final int CHOICE_MODE_SINGLE_ABSOLUTE = 10001;

    private static final String TAG = ExpandableListAdapter.class.getSimpleName();
    private Activity mContext;
    private Map<String, List<ContactLab.Filter>> collections;
    private List<String> data;
    private LayoutInflater inflater;
    private SparseArray<SparseBooleanArray> checkedPositions;
    private OnChildItemClickListener mListener;

    // The default choice is the multiple one
    private int choiceMode = CHOICE_MODE_MULTIPLE;;

    public ExpandableListAdapter(Activity context, List<String> names,
                                 Map<String, List<ContactLab.Filter>> collections) {
        this.mContext = context;
        this.collections = collections;
        this.data = names;
        inflater = LayoutInflater.from(context);
        checkedPositions = new SparseArray<SparseBooleanArray>();

        if (context instanceof OnChildItemClickListener)
            mListener = (OnChildItemClickListener) context;
        else throw new RuntimeException(context.toString()
                + " must implement OnChildItemClickListener");
    }

    public ExpandableListAdapter(Activity context, List<String> data,
                                 Map<String, List<ContactLab.Filter>> collections, int choiceMode) {
        this(context, data, collections);
        // For now the choice mode CHOICE_MODE_MULTIPLE_MODAL
        // is not implemented
        if (choiceMode == CHOICE_MODE_MULTIPLE_MODAL) {
            throw new RuntimeException("The choice mode CHOICE_MODE_MULTIPLE_MODAL " +
                    "has not implemented yet");
        }
        this.choiceMode = choiceMode;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return collections.get(data.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
//            convertView = inflater.inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
            convertView = inflater.inflate(R.layout.drawer_list_item, null);
        }

        ContactLab.Filter filter = (ContactLab.Filter) getChild(groupPosition, childPosition);
        CheckedTextView checkedTextView = (CheckedTextView) convertView;
        checkedTextView.setText(filter.getName());

        if (checkedPositions.get(groupPosition) != null) {

            boolean isChecked = checkedPositions.get(groupPosition).get(childPosition);
            checkedTextView.setChecked(isChecked);

            if (isChecked) convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryLight));
            else convertView.setBackgroundColor(Color.WHITE);

        } else {
            // If it does not exist, mark the checkBox as false
            checkedTextView.setChecked(false);
            convertView.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return collections.get(data.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    public int getGroupCount() {
        return data.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String laptopName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_group_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.group_drawer_text);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * Update the list of the positions checked and update the view
     * @param groupPosition The position of the group which has been checked
     * @param childPosition The position of the child which has been checked
     */
    public void setClicked(int groupPosition, int childPosition) {
        switch (choiceMode) {
            case CHOICE_MODE_MULTIPLE:
                SparseBooleanArray checkedChildPositionsMultiple = checkedPositions.get(groupPosition);
                // if in the group there was not any child checked
                if (checkedChildPositionsMultiple == null) {
                    checkedChildPositionsMultiple = new SparseBooleanArray();
                    // By default, the status of a child is not checked
                    // So a click will enable it
                    checkedChildPositionsMultiple.put(childPosition, true);
                    // Adds a mapping from the specified key to the specified value, replacing the previous mapping from the specified key if there was one.
                    checkedPositions.put(groupPosition, checkedChildPositionsMultiple);
                } else {
                    boolean oldState = checkedChildPositionsMultiple.get(childPosition);
                    // Adds a mapping from the specified key to the specified value, replacing the previous mapping from the specified key if there was one.
                    checkedChildPositionsMultiple.put(childPosition, !oldState);
                }

                mListener.onChildItemClickCallback(checkedPositions);
                break;
            // TODO: Implement it
            case CHOICE_MODE_MULTIPLE_MODAL:
                throw new RuntimeException("The choice mode CHOICE_MODE_MULTIPLE_MODAL " +
                        "has not implemented yet");
            case CHOICE_MODE_NONE:
                checkedPositions.clear();
                break;
            case CHOICE_MODE_SINGLE_PER_GROUP:
                SparseBooleanArray checkedChildPositionsSingle = checkedPositions.get(groupPosition);
                // If in the group there was not any child checked
                if (checkedChildPositionsSingle == null) {
                    checkedChildPositionsSingle = new SparseBooleanArray();
                    // By default, the status of a child is not checked
                    checkedChildPositionsSingle.put(childPosition, true);
                    checkedPositions.put(groupPosition, checkedChildPositionsSingle);
                } else {
                    boolean oldState = checkedChildPositionsSingle.get(childPosition);
                    // If the old state was false, set it as the unique one which is true
                    if (!oldState) {
                        checkedChildPositionsSingle.clear();
                        checkedChildPositionsSingle.put(childPosition, !oldState);
                    } // Else does not allow the user to uncheck it
                }
                break;
            // This mode will remove all the checked positions from other groups
            // and enable just one from the selected group
            case CHOICE_MODE_SINGLE_ABSOLUTE:
                checkedPositions.clear();
                SparseBooleanArray checkedChildPositionsSingleAbsolute = new SparseBooleanArray();
                checkedChildPositionsSingleAbsolute.put(childPosition, true);
                checkedPositions.put(groupPosition, checkedChildPositionsSingleAbsolute);
                break;
        } // end Switch

        // Notify that some data has been changed
        notifyDataSetChanged();
        Log.v(TAG, "List position updated");
        Log.v(TAG, "SparseBooleanArray: " + PrintSparseArrays.sparseArrayToString(checkedPositions));
    }

    public int getChoiceMode() {
        return choiceMode;
    }

    /**
     * Set a new choice mode. This will remove
     * all the checked positions
     * @param choiceMode
     */
    public void setChoiceMode(int choiceMode) {
        this.choiceMode = choiceMode;
        // For now the choice mode CHOICE_MODEL_MULTIPLE_MODAL
        // is not implemented
        if (choiceMode == CHOICE_MODE_MULTIPLE_MODAL) {
            throw new RuntimeException("The choice mode CHOICE_MODE_MULTIPLE_MODAL " +
                    "has not implemented yet");
        }
        checkedPositions.clear();
        Log.v(TAG, "The choice mode has been changed. Now it is " + this.choiceMode);
    }

    /**
     * Method used to get the actual state of the checked lists
     * @return The list of the all the positions checked
     */
    public SparseArray<SparseBooleanArray> getCheckedPositions() {
        return checkedPositions;
    }

}
