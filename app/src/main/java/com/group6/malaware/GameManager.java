package com.group6.malaware;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * Created by michaeldegraw on 9/22/15.
 * Edited by Michael DeGraw and Cyril Mathew on 11/4/2015.
 * Has most of the game's logic and functional methods
 */
public class GameManager
{
    private double totalResources = 0;
    private double resourcesPerSec = 0;
    Generator coreAdware = new Generator(10, 1.0);
    Generator coreMalware = new Generator(10, 1.0);
    Generator coreWorm = new Generator(10, 1.0);
    Generator coreTrojan = new Generator(10, 1.0);
    Generator coreRootkit = new Generator(10, 1.0);
    Generator coreHijacker = new Generator(10, 1.0);

    public double getTotalResources(){
        return totalResources;
    }

    public boolean subtractResources(double amount){
        double temp = totalResources;
        temp -= amount;
        if (temp >= 0){
            totalResources = temp;
            return true;
        } else {
            return false;
        }
    }

    public void addGenerator(int type, int amount)
    {
        switch(type)
        {
            case 0: coreAdware.addVirus(amount);
                break;
            case 1: coreMalware.addVirus(amount);
                break;
            case 2: coreWorm.addVirus(amount);
                break;
            case 3:coreTrojan.addVirus(amount);
                break;
            case 4:coreRootkit.addVirus(amount);
                break;
            case 5:coreHijacker.addVirus(amount);
                break;
            default:
                throw new RuntimeException("GameManager.addGenerator attempted to add unknown generator");
        }
        calcTotalResourcesPerSec();
    }

    public void getNumOfGenerators(int type)
    {
        switch(type)
        {
            case 0: coreAdware.getNumOfGenerators();
                break;
            case 1: coreMalware.getNumOfGenerators();
                break;
            case 2:
                coreWorm.getNumOfGenerators();
                break;
            case 3:coreTrojan.getNumOfGenerators();
                break;
            case 4:coreRootkit.getNumOfGenerators();
                break;
            case 5:coreHijacker.getNumOfGenerators();
                break;
            default:
                break;
        }
    }

    // reworked some faulty logic
    public void calcTotalResourcesPerSec()
    {
        resourcesPerSec = (coreAdware.calcVirusGenPerSec()+
                coreMalware.calcVirusGenPerSec()+
                coreWorm.calcVirusGenPerSec()+
                coreTrojan.calcVirusGenPerSec()+
                coreRootkit.calcVirusGenPerSec()+
                coreHijacker.calcVirusGenPerSec());
    }

    public double getResourcesPerSec(){
        return resourcesPerSec;
    }

    public void addResources(double amount){
        totalResources += amount;
    }

    public double getTotalResourcesPerFrame(int FPS){
        return resourcesPerSec / FPS;
    }

    public void attemptBuy(int type, int amount)
    {
        switch(type)
        {
            case 0: if ((amount*coreAdware.getCost()) <= totalResources)
                    {
                        totalResources -= (amount*coreAdware.getCost());
                        coreAdware.addVirus(amount);
                    }
                    else
                        Log.i("Error", "Can't buy that many");
                break;
            case 1: if ((amount*coreMalware.getCost()) <= totalResources)
                    {
                        totalResources -= (amount*coreMalware.getCost());
                        coreMalware.addVirus(amount);
                    }
                    else
                        Log.i("Error", "Can't buy that many");
                break;
            case 2:if ((amount*coreWorm.getCost()) <= totalResources)
                    {
                        totalResources -= (amount*coreWorm.getCost());
                        coreWorm.addVirus(amount);
                    }
                    else
                        Log.i("Error", "Can't buy that many");
                break;
            case 3:if ((amount*coreTrojan.getCost()) <= totalResources)
                    {
                        totalResources -= (amount*coreTrojan.getCost());
                        coreTrojan.addVirus(amount);
                    }
                    else
                        Log.i("Error", "Can't buy that many");
                break;
            case 4:if ((amount*coreRootkit.getCost()) <= totalResources)
                    {
                        totalResources -= (amount*coreRootkit.getCost());
                        coreRootkit.addVirus(amount);
                    }
                    else
                        Log.i("Error", "Can't buy that many");
                break;
            case 5:if ((amount*coreHijacker.getCost()) <= totalResources)
                    {
                        totalResources -= (amount*coreHijacker.getCost());
                        coreHijacker.addVirus(amount);
                    }
                    else
                        Log.i("Error", "Can't buy that many");
                break;
            default:
                break;
        }
        calcTotalResourcesPerSec();
    }

    public String getResourcesString() {
        DecimalFormat df = new DecimalFormat("###.000");
        String text = "";
        double temp = totalResources;
        int thousandsModifier = 0;

        while (temp >= 1d){
            temp /= 1000d;
            thousandsModifier ++;
        }

        if (thousandsModifier > 0){
            temp *= 1000d;
            thousandsModifier --;
        }

        text = df.format(temp).replaceAll(".000", "");

        switch (thousandsModifier){
            case 0:
                break;
            case 1:
                text += " K";
                break;
            case 2:
                text += " M";
                break;
            case 3:
                text += " B";
                break;
            case 4:
                text += " T";
                break;
            default:
                throw new RuntimeException("ResourceManager.getResourceString cannot handle numbers this large");
        }

        return text;
    }

    public void storeData(SharedPreferences sharedPref){
        // set up editor
        SharedPreferences.Editor editor = sharedPref.edit();

        // store data for resources
        editor = putDouble(editor, "resources", totalResources);

        // store data for viruses
        editor.putInt("num_malware", coreMalware.getNumOfGenerators());
        editor.putInt("num_worms", coreWorm.getNumOfGenerators());
        editor.putInt("num_adware", coreAdware.getNumOfGenerators());
        editor.putInt("num_rootkits", coreRootkit.getNumOfGenerators());
        editor.putInt("num_trojans", coreTrojan.getNumOfGenerators());
        editor.putInt("num_hijackers", coreHijacker.getNumOfGenerators());

        // store data for time
        editor.putLong("time", System.currentTimeMillis());

        // apply changes
        editor.apply();
    }

    public void loadData(SharedPreferences sharedPref){
        if (sharedPref == null){
            throw new RuntimeException("Attempted to load resources from a null SharedPreferences pointer");
        } else {
            // load resources
            totalResources = getDouble(sharedPref, "resources", 0.0);

            // load viruses
            coreMalware.setNumOfGenerators(sharedPref.getInt("num_malware", 0));
            coreWorm.setNumOfGenerators(sharedPref.getInt("num_worms", 0));
            coreAdware.setNumOfGenerators(sharedPref.getInt("num_adware", 0));
            coreRootkit.setNumOfGenerators(sharedPref.getInt("num_rootkits", 0));
            coreTrojan.setNumOfGenerators(sharedPref.getInt("num_trojans", 0));
            coreHijacker.setNumOfGenerators(sharedPref.getInt("num_hijackers", 0));
        }
    }

    public long getStoredTime(SharedPreferences sharedPref){
        return sharedPref.getLong("time", 0);
    }

    // Some SharedPreferences wizardy since SharedPreferences can't store doubles natively
    // CODE TAKEN FROM http://stackoverflow.com/questions/16319237/cant-put-double-sharedpreferences
    private SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value){
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    private double getDouble(final SharedPreferences prefs, final String key, final double defaultValue){
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    public String convertNumToString(double number){
        DecimalFormat df = new DecimalFormat("###.000");
        String text = "";
        int thousandsModifier = 0;

        while (number >= 1d){
            number /= 1000d;
            thousandsModifier ++;
        }

        if (thousandsModifier > 0){
            number *= 1000d;
            thousandsModifier --;
        }

        text = df.format(number).replaceAll(".000", "");

        switch (thousandsModifier){
            case 0:
                break;
            case 1:
                text += " K";
                break;
            case 2:
                text += " M";
                break;
            case 3:
                text += " B";
                break;
            case 4:
                text += " T";
                break;
            default:
                throw new RuntimeException("ResourceManager.convertNumToString cannot handle numbers this large");
        }

        return text;
    }
}
