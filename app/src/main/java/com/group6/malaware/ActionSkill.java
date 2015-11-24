package com.group6.malaware;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;

/**
 * Created by michaeldegraw on 11/24/15.
 */
public abstract class ActionSkill {
    MainActivity activity;              // associated activity
    GameManager manager;                // associated GameManager controller object
    FloatingActionButton actionButton;  // the floating action button associated with this action skill
    TextView txt;                       // the text view associated with this cd
    String name;                        // name of the action skill i.e. "Auto Tap"
    String purchaseMessage;             // purchase message i.e. "This upgrade does this and that
    String upgradeMessage;              // upgrade message i.e. "Each upgrade will do..."
    int count;                          // used when counting down from duration and cooldown
    int duration;                       // the active phase when the skill is used
    int cooldown;                       // the cooldown of the skill
    int upgradeLevel;                   // tracks how many times the skill has been upgraded
    double cost;                        // how much the skill costs

    protected ActionSkill(MainActivity activity, GameManager manager, FloatingActionButton actionButton, TextView txt, String name, String purchaseMessage, String upgradeMessage, int duration, int cooldown, double cost){
        this.activity = activity;
        this.manager = manager;
        this.actionButton = actionButton;
        this.txt = txt;
        this.name = name;
        this.purchaseMessage = purchaseMessage;
        this.upgradeMessage = upgradeMessage;
        this.duration = duration;
        this.cooldown = cooldown;
        this.cost = cost;
        this.upgradeLevel = 0;
    }

    // activate must be defined by a new Action Skill
    public abstract void activate();

    public void upgrade(){
        manager.subtractResources(getCostOfUpgradeLevel(upgradeLevel));
        cost += getCostOfUpgradeLevel(upgradeLevel);
        upgradeLevel++;
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
}