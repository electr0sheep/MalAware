package com.group6.malaware;

/**
 * Created by Cyril Mathew on 11/5/15.
 * Has definition for each resource generator object
 */

public class Generator {
    private int baseCost = 0;
    private int upgradeGain = 0;
    private double upgradeCostGrowth = 0d;
    private int totalUpgradeCost = 0;

    private int numOfGenerators = 0;
    private double genRate = 1.0;
    private double upgradeLevel = 1.0;

    public Generator(double baseGenRate, int baseCost, int upgradeGain, double upgradeCostGrowth) {
        this.baseCost = baseCost;
        this.upgradeGain = upgradeGain;
        this.upgradeCostGrowth = upgradeCostGrowth;
        genRate = baseGenRate;
        calcCost();
    }

    public int getNumOfGenerators() {
        return numOfGenerators;
    }

    public int getCost() {
        return totalUpgradeCost;
    }

    private void calcCost()
    {
        double tmp_term = Math.pow((numOfGenerators * upgradeGain), upgradeCostGrowth);
        totalUpgradeCost = (baseCost+ ((int) tmp_term));
    }

    public void addVirus(int amount) {
        numOfGenerators += amount;
        calcCost();
    }

    public double getUpgradeLevel() {
        return upgradeLevel;
    }

    public void setUpgradeLevel(double level){
        upgradeLevel = level;
    }

    // this function needs to be reworked
    public double getCostOfUpgrade(){
        return (upgradeLevel - .5) * 1000;
    }

    public void upgrade(){
        upgradeLevel += .1;
    }

    public double calcVirusGenPerSec() {
        double blah = genRate;
        double blah2 = numOfGenerators;
        double blah3 = upgradeLevel;
        return genRate * numOfGenerators * upgradeLevel;
    }
}
