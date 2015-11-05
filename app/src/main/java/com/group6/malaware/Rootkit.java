package com.group6.malaware;

/**
 * Created by Cyril on 11/1/2015.
 */
public class Rootkit
{
    private int costOEach = 10;
    private int resourceTotal = 0;
    private int numOfUpgrades= 0;
    private double baseGen = 1.0;
    private double calc = 0;

    void addVirus()
    {
        resourceTotal++;
        costOEach += 10;
    }

    void addUpgrade()
    {
        numOfUpgrades++;
    }

    double calcVirusGenPerSec()
    {
        calc = baseGen*resourceTotal;

        return calc;
    }
}