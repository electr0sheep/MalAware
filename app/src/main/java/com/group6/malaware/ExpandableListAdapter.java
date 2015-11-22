package com.group6.malaware;

/**
 * Originally found at http://theopentutorials.com/tutorials/android/listview/android-expandable-list-view-example/
 * Edited by Cyril Mathew on 11/18/15.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private MainActivity callingActivity;
    private Map<String, List<String>> childCollections;
    private List<String> generatorList;
    private final short SIZE = 6;
    private int lastExpandedGroupPosition = -1;

    private Vector<TextView> tView_genNum = new Vector<TextView>(SIZE);
    private Vector<TextView> tView_genRate = new Vector<TextView>(SIZE);
    private Vector<TextView> tView_genCost = new Vector<TextView>(SIZE);

    private Vector<Button> btn_timesOne = new Vector<Button>(SIZE);
    private Vector<Button> btn_timesTen = new Vector<Button>(SIZE);
    private Vector<Button> btn_timesTwoFive = new Vector<Button>(SIZE);

    ExpandableListView expListView;


    public ExpandableListAdapter(Activity context, List<String> generatorList, Map<String, List<String>> generatorCollections) {
        this.context = context;
        this.childCollections = generatorCollections;
        this.generatorList = generatorList;
        this.callingActivity = (MainActivity) context;
        this.expListView = callingActivity.expListView;

        tView_genNum.setSize(SIZE);
        tView_genRate.setSize(SIZE);
        tView_genCost.setSize(SIZE);

        btn_timesOne.setSize(SIZE);
        btn_timesTen.setSize(SIZE);
        btn_timesTwoFive.setSize(SIZE);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return childCollections.get(generatorList.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            Log.i("Info", "ConvertView is null");
            convertView = inflater.inflate(R.layout.child_item, null);
        }

        TextView tmp_genNum = (TextView) convertView.findViewById(R.id.txt_genNum);
        TextView tmp_genRate = (TextView) convertView.findViewById(R.id.txt_genRate);
        TextView tmp_genCost = (TextView) convertView.findViewById(R.id.txt_genCost);

        tmp_genNum.setText("# of Generators: " + callingActivity.gameManager.getNumOfGenerators(groupPosition));
        tmp_genRate.setText("Generation Rate:  " + callingActivity.gameManager.getGenRate(groupPosition));
        tmp_genCost.setText("Cost Per Generator:  " + callingActivity.gameManager.getCostOfGenerators(groupPosition));

        Button tmpButton_x1 = (Button) convertView.findViewById(R.id.btn_x1);
        Button tmpButton_x10 = (Button) convertView.findViewById(R.id.btn_x10);
        Button tmpButton_x25 = (Button) convertView.findViewById(R.id.btn_x25);


        tmpButton_x1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Info", "Add to Group Position: " + groupPosition);
                callingActivity.gameManager.attemptBuy(groupPosition, 1);
                updateGroup(groupPosition);
            }
        });

        tmpButton_x10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callingActivity.gameManager.attemptBuy(groupPosition, 10);
                updateGroup(groupPosition);
            }
        });

        tmpButton_x25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callingActivity.gameManager.attemptBuy(groupPosition, 25);
                updateGroup(groupPosition);
            }
        });

        Log.i("Info", "addingPosition: " + groupPosition);

        tView_genNum.add(groupPosition, tmp_genNum);
        tView_genRate.add(groupPosition, tmp_genRate);
        tView_genCost.add(groupPosition, tmp_genCost);

        btn_timesOne.add(groupPosition, tmpButton_x1);
        btn_timesTen.add(groupPosition, tmpButton_x10);
        btn_timesTwoFive.add(groupPosition, tmpButton_x25);

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return childCollections.get(generatorList.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return generatorList.get(groupPosition);
    }

    public int getGroupCount() {
        return generatorList.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
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

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void updateGroup(int groupPosition) {
        Log.i("Info", "Update Position: " + groupPosition);
        tView_genNum.get(groupPosition).setText("# of Generators: " + callingActivity.gameManager.getNumOfGenerators(groupPosition));
        tView_genRate.get(groupPosition).setText("Generation Rate:  " + callingActivity.gameManager.getGenRate(groupPosition));
        tView_genCost.get(groupPosition).setText("Cost Per Generator:  " + callingActivity.gameManager.getCostOfGenerators(groupPosition));
    }

    @Override
    public void onGroupExpanded(int groupPosition){
        //collapse the old expanded group, if not the same
        //as new group to expand
        if(groupPosition != lastExpandedGroupPosition){
            expListView.collapseGroup(lastExpandedGroupPosition);
        }

        super.onGroupExpanded(groupPosition);
        lastExpandedGroupPosition = groupPosition;
    }
}