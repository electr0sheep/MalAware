package com.group6.malaware;

/**
 * Created by Cyril Mathew on 11/5/15.
 * Has definition for each resource generator object
 * Edited by Adrian Palmerola 11/26/15
 * Added logic for modifier upgrades
 */

public class Generator {
     /*
        baseCost is the basic cost of a generator before gains and growth
        upgradeGain is the amount of resources that the baseCost is increased by.
        upgradeCostGrowth is exponential value that baseCost and upgradeGain are increased by.
        totalUpgradeCost stores the cost of the next generator.
        modifierUpgradeCost stores the cost of the next generator upgrade.
    */
    private int baseCost = 0;
    private int upgradeGain = 0;
    private double upgradeCostGrowth = 0d;
    private double totalUpgradeCost = 0;
    private double modifierUpgradeCost = 0;

    /*
        numOfGenerators is Number of Generators
        genRate is generation rate of the generator.
        upgradeLevel is modifier of the generator, 1.0 is 100% generation rate and 1.2 is 120% generation rate.
        upgradeLevelBonus is the amount added to upgradeLevel when an upgrade is bought.
        upgradeModLevel is the amount of times a player has bought an upgrade for the generator.
    */
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
    /*
        DefaultMods() resets the modifiers of the generator so that they can be either reset or loaded in with reloadMods()
        The base cost of generator upgrades is this formula
            (2000000+(genRate*1000)+((baseCost+upgradeGain)^(upgradeCostGrowth))-(upgradeLevelBonus*40000));
    */
    public void defaultMods()
    {
        upgradeModLevel = 0;
        double temp = Math.pow((baseCost+upgradeGain), upgradeCostGrowth);
        modifierUpgradeCost = (int)(2000000+(genRate*1000)+temp-(upgradeLevelBonus*40000));
    }
    /*
        reloadMods() is called when the app is turned on again. This function takes the upgradeLevel that was saved and calculates
            what the player had before the app was closed. 
    */
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
    /*
       Reset() resets the generator back to square one. It will not produce anything until the player
            once again purchases them. In addition all upgrades for that generator are removed.
    */
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

    /*
        void calcCost()
        Calculates the cost of a generator once a generator is purchased
    */
    private void calcCost()
    {
        double tmp_term = Math.pow((baseCost+(numOfGenerators * upgradeGain)), upgradeCostGrowth);
        // tmp_DReturn makes players use other generators due to cost increase for owning multiple generators
        // Slows the player down for balancing reasons. Has little impact on better generators.
        double tmp_DReturn = Math.pow(numOfGenerators, 1.4);
        totalUpgradeCost = (int)tmp_term + tmp_DReturn;
    }

    /*
        addVirus(int amount)
        Purchases a given amount of generators
    */
    public void addVirus(int amount) {
        numOfGenerators += amount;
        calcCost();
    }
    /*
        getUpgradeLevel()
        Returns the upgrade level of the generator
    */
    public double getUpgradeLevel() {
        return upgradeLevel;
    }
    /*
        getUpgradeLevelBonus
        Returns the bonus modifier of the generator
    */
    public double getUpgradeLevelBonus() {
        return upgradeLevelBonus;
    }
    /*
        setUpgradeLevel(double level)
        Used to set the upgradeLevel to a new value. (1.0 = 100%)
    */
    public void setUpgradeLevel(double level){
        upgradeLevel = level;
    }

     /*
        getCostOfUpgradeModifier()
        Returns the cost of the next generator upgrade
    */
    public double getCostOfUpgradeModifier(){
        return modifierUpgradeCost;
    }
     /*
        getModifierLevel()
        Returns the level of the modifier
    */
    public double getModifierLevel() {
        return upgradeModLevel;
    }
    
     /*
        upgrade()
        This function upgrades the generator by using the increasing upgradeLevel by upgradeLevelBonus
        The cost is then increased by a base amount of 3,750,000 plus the current cost and increased by
            an extra 20% plus 10% for every level gained previously
    */
    public void upgrade(){
        double temp = 3750000;
        upgradeLevel += upgradeLevelBonus;
        modifierUpgradeCost=((modifierUpgradeCost+temp)*(1.20+(.10*upgradeModLevel)));
        upgradeModLevel += 1;
    }
    /*
        calcVirusGenPerSec()
        Calculates the total generation of the generator
    */
    public double calcVirusGenPerSec() {
        return genRate * numOfGenerators * upgradeLevel;
    }
}
