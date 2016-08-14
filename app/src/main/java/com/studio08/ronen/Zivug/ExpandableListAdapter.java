package com.studio08.ronen.Zivug;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Ronen on 14/8/16.
 */

public class ExpandableListAdapter extends android.widget.BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> collections;
    private List<String> data;

    public ExpandableListAdapter(Activity context, List<String> data,
                                 Map<String, List<String>> collections) {
        this.context = context;
        this.collections = collections;
        this.data = data;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return collections.get(data.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandableItem = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_list_item, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.text_list_item);

        item.setText(expandableItem);
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
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_item,
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
}
