package com.group6.malaware;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by michaeldegraw on 11/24/15.
 */
public final class AutoTap extends ActionSkill {
    public AutoTap(MainActivity activity, GameManager manager, SharedPreferences sharedPref, FloatingActionButton actionButton, TextView txt){
        super(activity, manager, sharedPref, actionButton, txt, "Auto Tap",
                "Auto tap simulates the terminal being tapped and resources will be continuously added to your resource pool",
                "Each upgrade to auto tap will increase the duration of auto tap by 5 seconds", 10, 60, 10d);
    }

    @Override
    protected double getCostOfUpgradeLevel(int upgradeLevel) {
        return cost + (upgradeLevel * 50);
    }

    @Override
    public void activateEffect() {
        duration = 5 + (upgradeLevel * 5);
        manager.tempIncreaseResourcesPerSec(1d, 5000 + (upgradeLevel * 5000));
    }
}
