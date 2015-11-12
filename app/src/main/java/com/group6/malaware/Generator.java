package com.group6.malaware;

/**
 * Created by Cyril Mathew on 11/5/15.
 * Has definition for each resource generator object
 */

public class Generator
{
    private int costOEach = 10;
    private int numOfGenerators = 0;
    // I don't think we need to keep track of the original gen rate
    //private double baseGenRate = 1.0;
    private double genRate = 1.0;

    public Generator(int initialCost, double baseGen)
    {
        costOEach = initialCost;
        genRate = baseGen;
    }

    public int getNumOfGenerators(){return numOfGenerators;}
    public int getCost(){return costOEach;}

    public void addVirus(int amount)
    {
        //Rudimentary method for adding a certain amount of viruses
        numOfGenerators+= amount;
        costOEach += 10 * amount;
    }

    public double calcVirusGenPerSec()
    {
        return genRate * numOfGenerators;
    }
}
