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
        defaultMods();
        calcCost();
    }
    public void defaultMods()
    {
        upgradeModLevel = 0;
        double temp = Math.pow((baseCost+upgradeGain), upgradeCostGrowth);
        modifierUpgradeCost = (int)(2000000+(genRate*1000)+temp-(upgradeLevelBonus*40000));
    }
    public void reloadMods()
    {
        defaultMods();
        double temp = (upgradeLevel) - 1.0;
        for (;temp>0;temp-=upgradeLevelBonus)
        {
            upgrade();
            upgradeLevel-=upgradeLevelBonus;
        }
    }
    public void reset(){
        defaultMods();
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
        // tmp_DReturn makes players use other generators due to cost increase for owning multiple generators
        // Slows the player down for balancing reasons. Has little impact on better generators.
        double tmp_DReturn = Math.pow(numOfGenerators, 1.4);
        totalUpgradeCost = (int)tmp_term + tmp_DReturn;
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
        modifierUpgradeCost=(modifierUpgradeCost+3750000)*(1.20+(.10*upgradeModLevel));
        upgradeModLevel += 1;
    }

    public double calcVirusGenPerSec() {
        return genRate * numOfGenerators * upgradeLevel;
    }
}
