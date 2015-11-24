package com.group6.malaware;

import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by michaeldegraw on 11/24/15.
 */
public final class AutoTap extends ActionSkill {
    public AutoTap(MainActivity activity, GameManager manager, FloatingActionButton actionButton, TextView txt){
        super(activity, manager, actionButton, txt, "Auto Tap",
                "Auto tap simulates the terminal being tapped and resources will be continuously added to your resource pool",
                "Each upgrade to auto tap will increase the duration of auto tap by 5 seconds", 10, 60, 10d);
    }

    @Override
    protected double getCostOfUpgradeLevel(int upgradeLevel) {
        return cost + (upgradeLevel * 50);
    }

    @Override
    public void activate(){
        count = (1 + duration) + ((upgradeLevel - 1) * 5);
        actionButton.setImageResource(android.R.color.transparent);
        actionButton.setEnabled(false);
        txt.setVisibility(TextView.VISIBLE);
        manager.tempIncreaseResourcesPerSec(1, 5000 + (upgradeLevel * 5000));
        final Timer activeTimer = new Timer();
        // begin active timer
        activeTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (count == 1) {
                    count = cooldown + 2;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            actionButton.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(android.R.color.holo_red_dark)));
                        }
                    });
                    activeTimer.cancel();
                    final Timer cooldownTimer = new Timer();
                    cooldownTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if (count == 1) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        actionButton.setImageResource(R.drawable.auto_tap);
                                        txt.setVisibility(TextView.GONE);
                                        actionButton.setEnabled(true);
                                        actionButton.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(android.R.color.holo_green_dark)));
                                    }
                                });
                                cooldownTimer.cancel();
                            }
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txt.setText(Integer.toString(count));
                                }
                            });

                            count--;
                        }
                    }, 0, 1000);
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txt.setText(Integer.toString(count));
                    }
                });
                count--;;
            }
        }, 0, 1000);
    }
}
