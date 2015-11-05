package com.group6.malaware;

/**
 * Created by Elior on 11/5/15.
 */
public class Generator
{
    private int costOEach = 10;
    private int numOfGenerators = 0;
    private double baseGenRate = 1.0;
    private double calculatedAmount = 0;

    public Generator(int initialCost, double baseGen)
    {
        costOEach = initialCost;
        baseGenRate = baseGen;
    }

    public int getNumOfGenerators(){return numOfGenerators;}
    public int getCost(){return costOEach;}

    public void addVirus(int amount)
    {
        numOfGenerators+= amount;
        costOEach += 10*amount;
    }

    public double calcVirusGenPerSec()
    {
        calculatedAmount = baseGenRate*numOfGenerators;

        return calculatedAmount;
    }

}
