package com.group6.malaware;

import android.app.Notification;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by michaeldegraw on 11/24/15.
 */
public class TimeWarp extends ActionSkill {
    public TimeWarp(MainActivity activity, GameManager manager, SharedPreferences sharedPref, FloatingActionButton actionButton, TextView txt) {
        super(activity, manager, sharedPref, actionButton, txt, "Time Warp",
                "This action skill will skip ahead in time and provide resources equal to 5 minutes worth of time",
                "Each upgrade to time warp will provide an additional minute worth of resources", 0, 60, 30d);
    }

    @Override
    protected double getCostOfUpgradeLevel(int upgradeLevel) {
        return cost + (upgradeLevel * 150);
    }

    @Override
    public void activateEffect() {
        double resourceIncrease = manager.getResourcesPerSec() * (240 + upgradeLevel * 60);
        manager.addResources(resourceIncrease);
        Toast.makeText(activity, "You gained " + manager.convertNumToString(resourceIncrease) + " resources", Toast.LENGTH_SHORT).show();
    }
}
