package com.group6.malaware;

/**
 * Created by Cyril Mathew on 11/5/15.
 * Has definition for each resource generator object
 */

public class Generator {
    private int costOEach = 10;
    private int numOfGenerators = 0;
    private double genRate = 1.0;
    private double upgradeLevel = 1.0;

    public Generator(int initialCost, double baseGen) {
        costOEach = initialCost;
        genRate = baseGen;
    }

    public int getNumOfGenerators() {
        return numOfGenerators;
    }

    public int getCost() {
        return costOEach;
    }

    public void addVirus(int amount) {
        //Rudimentary method for adding a certain amount of viruses
        numOfGenerators += amount;
        costOEach += 10 * amount;
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
