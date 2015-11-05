package com.group6.malaware;

/**
 * Created by Cyril on 11/1/2015.
 */
public class Rootkit
{
    private int costOEach = 10;
    private int numOfGenerators = 0;
    private double baseGen = 1.0;
    private double calc = 0;

    public int getNumOfGenerators(){return numOfGenerators;}
    public int getCost(){return costOEach;}

    public void addVirus(int amount)
    {
        numOfGenerators+=amount;
        costOEach += 10*amount;
    }

    public double calcVirusGenPerSec()
    {
        calc = baseGen* numOfGenerators;

        return calc;
    }
}