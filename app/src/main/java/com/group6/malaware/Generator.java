package com.group6.malaware;

/**
 * Created by Cyril Mathew on 11/5/15.
 * Has definition for each resource generator object
 * Edited by Adrian Palmerola 11/26/15
 * Added logic for modifier upgrades
 */

public class Generator {
    private int baseCost = 0;
    private int upgradeGain = 0;
    private double upgradeCostGrowth = 0d;
    private double totalUpgradeCost = 0;
    private double modifierUpgradeCost = 0;

    private int numOfGenerators = 0;
    private double genRate = 1.0;
    private double upgradeLevel = 1.0;
    private double upgradeLevelBonus = .1;
    private double upgradeModLevel = 0;

    public Generator(double baseGenRate, int baseCost, int upgradeGain, double upgradeCostGrowth, double upgradeLevelBonus) {
        this.baseCost = baseCost;
        this.upgradeGain = upgradeGain;
        this.upgradeCostGrowth = upgradeCostGrowth;
        this.upgradeLevelBonus = upgradeLevelBonus;
        genRate = baseGenRate;
        //Create the base cost to upgrade the modifier
        double temp = Math.pow((this.baseCost+this.upgradeGain), this.upgradeCostGrowth);
        modifierUpgradeCost = (int)(2000000+(this.genRate*1000)+temp-(this.upgradeLevelBonus*40000));
        calcCost();
    }

    public void reset(){
        numOfGenerators = 0;
        totalUpgradeCost = 0;
        upgradeLevel = 1d;
    }

    public int getNumOfGenerators() {
        return numOfGenerators;
    }

    public double getCost() {
        return totalUpgradeCost;
    }

    private void calcCost()
    {
        // double tmp_term = Math.pow((numOfGenerators * upgradeGain), upgradeCostGrowth);
        //  totalUpgradeCost = (baseCost+ ((int) tmp_term));
        // Corrected formula
        double tmp_term = Math.pow((baseCost+(numOfGenerators * upgradeGain)), upgradeCostGrowth);
        totalUpgradeCost = (int)tmp_term;
    }

    public void addVirus(int amount) {
        numOfGenerators += amount;
        calcCost();
    }

    public double getUpgradeLevel() {
        return upgradeLevel;
    }

    public double getUpgradeLevelBonus() {
        return upgradeLevelBonus;
    }

    public void setUpgradeLevel(double level){
        upgradeLevel = level;
    }

    // New upgrade for late game
    public double getCostOfUpgradeModifier(){
        return modifierUpgradeCost;
    }

    public double getModifierLevel() {
        return upgradeModLevel;
    }

    public void upgrade(){
        upgradeLevel += upgradeLevelBonus;
        modifierUpgradeCost*=(1.40+(.12*upgradeModLevel));
        upgradeModLevel += 1;
    }

    public double calcVirusGenPerSec() {
        return genRate * numOfGenerators * upgradeLevel;
    }
}
