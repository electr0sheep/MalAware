package com.group6.malaware;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by michaeldegraw on 11/24/15.
 */


/****************************************************************************
 * ======================================================================= *
 * ======================================================================= *
 * NOTE FOR IMPROVEMENT: implement function to activate the action button  *
 * cd, and handle whatever the action skill does seperately (should be     *
 * easy with the implementation of tempIncreaseResourcePerSec() in         *
 * GameManager. Also, have methods that return a DialogFragment ready      *
 * to display                                                              *
 * ALSO: should implement functions that say what the action skill is      *
 * currently doing, and what it will do when it is upgraded
 * ======================================================================= *
 * ======================================================================= *
 ***************************************************************************/
public abstract class ActionSkill {
    protected MainActivity activity;              // associated activity
    protected GameManager manager;                // associated GameManager controller object
    protected SharedPreferences sharedPref;       // associated SharedPreferences object
    protected FloatingActionButton actionButton;  // the floating action button associated with this action skill
    protected TextView txt;                       // the text view associated with this cd
    protected String name;                        // name of the action skill i.e. "Auto Tap"
    protected String purchaseMessage;             // purchase message i.e. "This upgrade does this and that
    protected String upgradeMessage;              // upgrade message i.e. "Each upgrade will do..."
    protected int count;                          // used when counting down from duration and cooldown
    protected int duration;                       // the active phase when the skill is used
    protected int cooldown;                       // the cooldown of the skill
    protected int upgradeLevel;                   // tracks how many times the skill has been upgraded
    protected double cost;                        // how much the skill costs
    protected boolean actionSkillActive;          // boolean that keeps track of which Timer object is active

    protected ActionSkill(MainActivity activity, GameManager manager, SharedPreferences sharedPref, FloatingActionButton actionButton, TextView txt, String name, String purchaseMessage, String upgradeMessage, int duration, int cooldown, double cost){
        this.activity = activity;
        this.manager = manager;
        this.sharedPref = sharedPref;
        this.actionButton = actionButton;
        this.txt = txt;
        this.name = name;
        this.purchaseMessage = purchaseMessage;
        this.upgradeMessage = upgradeMessage;
        this.duration = duration;
        this.cooldown = cooldown;
        this.cost = cost;
        this.upgradeLevel = sharedPref.getInt(name, 0);
        this.actionSkillActive = false;

        int cooldownProgress = sharedPref.getInt(name + "_cooldown", 0);
        // check to see if cooldown was in progress
        if (cooldownProgress != 0){
            if (cooldownProgress < 0){
                activateActionButtonCD(0, -cooldownProgress);
            } else {
                activateActionButtonCD(cooldownProgress, cooldown);
            }
        }
    }

    protected void end(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(name, upgradeLevel);

        // determine state of cooldown and store it
        // count should be 0 if there is no cooldown in progress
        if (actionSkillActive || count == 0) {
            editor.putInt(name + "_cooldown", count);
        } else {
            editor.putInt(name + "_cooldown", -count);
        }

        editor.apply();
    }

    public void reset(){
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.remove(getName());
        editor.apply();
        upgradeLevel = 0;
    }

    public void activate(){
        activateEffect();
        activateActionButtonCD(duration, cooldown);
    }

    private void activateActionButtonCD(final int duration, final int cooldown){
        actionSkillActive = true;
        count = (1 + duration);
        actionButton.setImageResource(android.R.color.transparent);
        actionButton.setEnabled(false);
        txt.setVisibility(TextView.VISIBLE);
        final Timer activeTimer = new Timer();
        // begin active timer
        activeTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (count == 1) {
                    actionSkillActive = false;
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

    // each Action Skill must define what it does in addition
    //  to the standard active/cooldown cycles
    public abstract void activateEffect();

    public void upgrade(){
        manager.subtractResources(getCostOfUpgradeLevel(upgradeLevel));
        upgradeLevel++;
        cost += getCostOfUpgradeLevel(upgradeLevel);
    }

    // the cost of upgrades must be defined by a new Action Skill
    protected abstract double getCostOfUpgradeLevel(int upgradeLevel);

    public double getUpgradeCost(){
        return getCostOfUpgradeLevel(upgradeLevel);
    }

    public boolean purchased(){
        return upgradeLevel > 0;
    }

    public String getPurchaseMessage(){
        return purchaseMessage;
    }

    public String getUpgradeMessage(){
        return upgradeMessage;
    }

    public int getUpgradeLevel(){
        return upgradeLevel;
    }

    public String getName(){
        return name;
    }
}