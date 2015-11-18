package com.group6.malaware;

/**
 * Originally found at http://theopentutorials.com/tutorials/android/listview/android-expandable-list-view-example/
 * Edited by Cyril Mathew on 11/18/15.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> childCollections;
    private List<String> generatorList;

    public ExpandableListAdapter(Activity context, List<String> generatorList, Map<String, List<String>> laptopCollections) {
        this.context = context;
        this.childCollections = laptopCollections;
        this.generatorList = generatorList;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return childCollections.get(generatorList.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        //final String detail = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item, null);
        }

        TextView tView_genNum = (TextView) convertView.findViewById(R.id.txt_genNum);
        TextView tView_genRate = (TextView) convertView.findViewById(R.id.txt_genNum);
        TextView tView_genCost = (TextView) convertView.findViewById(R.id.txt_genNum);

        //ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
        /*delete.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to remove?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                List<String> child =
                                        childCollections.get(generatorList.get(groupPosition));
                                child.remove(childPosition);
                                notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });*/

        //item.setText(detail);
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {return childCollections.get(generatorList.get(groupPosition)).size();}

    public Object getGroup(int groupPosition) {return generatorList.get(groupPosition);}

    public int getGroupCount() {return generatorList.size();}

    public long getGroupId(int groupPosition) {return groupPosition;}

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        String generatorName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item, null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.generatorGroups);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(generatorName);
        return convertView;
    }

    public boolean hasStableIds() {return true;}

    public boolean isChildSelectable(int groupPosition, int childPosition) {return true;}
}