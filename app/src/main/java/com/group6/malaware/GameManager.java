package com.group6.malaware;

import android.content.SharedPreferences;

import java.text.DecimalFormat;

/**
 * Created by michaeldegraw on 9/22/15.
 * Edited by Michael DeGraw and Cyril Mathew on 11/4/2015.
 * Has most of the game's logic and functional methods
 */
public class GameManager {
    private double totalResources = 0d;
    private double resourcesPerSec = 0d;
    private double modifier = 1d;
    Generator coreAdware = new Generator(.5, 7, 3, 1.2);
    Generator coreMalware = new Generator(1.5, 45, 10, 1.2);
    Generator coreWorm = new Generator(3, 100, 24, 1.2);
    Generator coreTrojan = new Generator(8, 250, 50, 1.2);
    Generator coreRootkit = new Generator(15, 400, 100, 1.2);
    Generator coreHijacker = new Generator(50, 800, 300, 1.2);
    int ASautoTapUpgradeLevel = 1;
    int autoTapCost = 10;
    int resGenCost = 20;
    int timeWarpCost = 30;
    int ASincreaseResourceGenerationUpgradeLevel;
    int AStimeWarpUpgradeLevel;

    int resetLevel = 0;

    // Generator constants
    public static final int ADWARE = 0;
    public static final int MALWARE = 1;
    public static final int WORM = 2;
    public static final int TROJAN = 3;
    public static final int ROOTKIT = 4;
    public static final int HIJACKER = 5;

    //Upgrade Constants
    public static final int AUTO_TAP = 0;
    public static final int RESOURCE_GEN_INCREASE = 1;
    public static final int TIME_WARP = 2;

    public void toggleIncreaseGen(boolean active){
        if (active) {
            modifier += modifier * ASincreaseResourceGenerationUpgradeLevel;
        } else {
            modifier = 1;
        }
        calcTotalResourcesPerSec();
    }

    public void toggleAutoTap(boolean active){
        if (active){
            calcTotalResourcesPerSec();
            resourcesPerSec += ASautoTapUpgradeLevel;
        } else {
            calcTotalResourcesPerSec();
        }
    }

    public void resetLevelInc()
    {
        resetLevel++;
    }

    public int getResetLevel()
    {
        return  resetLevel;
    }


   //Redundant with attemptBuy?
    public boolean autoTapPurchased() {
        return ASautoTapUpgradeLevel > 0;
    }

    public int autoTapDuration(int upgradeLevel){
        return 5 + (5 * upgradeLevel);
    }

    public boolean increaseResourceGenerationPurchased() {
        return ASincreaseResourceGenerationUpgradeLevel > 0;
    }

    public double resourceGenerationModifier(int upgradeLevel){
        return .5 + .5 * upgradeLevel;
    }

    public boolean timeWarpPurchased() {
        return AStimeWarpUpgradeLevel > 0;
    }

    public int timeWarpTime(int upgradeLevel){
        return 4 + upgradeLevel;
    }

    public int getUpgradeCost(int upgradeType)
    {
        switch(upgradeType)
        {
            case AUTO_TAP:
                return autoTapCost;
            case RESOURCE_GEN_INCREASE:
                return resGenCost;
            case TIME_WARP:
                return timeWarpCost;
        }
        return 0;
    }

    public int getUpgradeLevel(int upgradeType)
    {
        switch(upgradeType)
        {
            case AUTO_TAP:
                return ASautoTapUpgradeLevel;
            case RESOURCE_GEN_INCREASE:
                return ASincreaseResourceGenerationUpgradeLevel;
            case TIME_WARP:
                return AStimeWarpUpgradeLevel;
        }
        return 0;
    }

    // if there are not enough resources, subtractResources will throw an exception
    //  and exit the function
    public void attemptUpgrade(int upgradeType){
        switch(upgradeType)
        {
            case AUTO_TAP:
                subtractResources(autoTapCost);
                autoTapCost *= 5;
                ASautoTapUpgradeLevel++;
                break;
            case RESOURCE_GEN_INCREASE:
                subtractResources(resGenCost);
                resGenCost *= 5;
                ASincreaseResourceGenerationUpgradeLevel++;
                break;
            case TIME_WARP:
                subtractResources(timeWarpCost);
                timeWarpCost *= 5;
                AStimeWarpUpgradeLevel++;
                break;
            default:
                break;
        }
    }

    public void addGenerator(int type, int amount) {
        if (amount < 0) {
            throw new RuntimeException("GameManager.addGenerator cannot add negative generators");
        }

        switch (type) {
            case ADWARE:
                coreAdware.addVirus(amount);
                break;
            case MALWARE:
                coreMalware.addVirus(amount);
                break;
            case WORM:
                coreWorm.addVirus(amount);
                break;
            case TROJAN:
                coreTrojan.addVirus(amount);
                break;
            case ROOTKIT:
                coreRootkit.addVirus(amount);
                break;
            case HIJACKER:
                coreHijacker.addVirus(amount);
                break;
            default:
                throw new RuntimeException("GameManager.addGenerator attempted to add unknown generator");
        }
        calcTotalResourcesPerSec();
    }

    public void addResources(double amount) {
        totalResources += amount;
    }

    public void subtractResources(double amount) {
        if (amount > totalResources) {
            throw new RuntimeException("GameManager.subtractResources cannot subtract more resources than available");
        } else {
            totalResources -= amount;
        }
    }

    public boolean attemptBuy(int type, int amount) {
        switch (type) {
            case ADWARE:
                if ((amount * coreAdware.getCost()) <= totalResources) {
                    totalResources -= (amount * coreAdware.getCost());
                    coreAdware.addVirus(amount);
                    break;
                } else
                    return false;
            case MALWARE:
                if ((amount * coreMalware.getCost()) <= totalResources) {
                    totalResources -= (amount * coreMalware.getCost());
                    coreMalware.addVirus(amount);
                    break;
                } else
                    return false;
            case WORM:
                if ((amount * coreWorm.getCost()) <= totalResources) {
                    totalResources -= (amount * coreWorm.getCost());
                    coreWorm.addVirus(amount);
                    break;
                } else
                    return false;
            case TROJAN:
                if ((amount * coreTrojan.getCost()) <= totalResources) {
                    totalResources -= (amount * coreTrojan.getCost());
                    coreTrojan.addVirus(amount);
                    break;
                } else
                    return false;
            case ROOTKIT:
                if ((amount * coreRootkit.getCost()) <= totalResources) {
                    totalResources -= (amount * coreRootkit.getCost());
                    coreRootkit.addVirus(amount);
                    break;
                } else
                    return false;
            case HIJACKER:
                if ((amount * coreHijacker.getCost()) <= totalResources) {
                    totalResources -= (amount * coreHijacker.getCost());
                    coreHijacker.addVirus(amount);
                    break;
                } else
                    return false;
            default:
                throw new RuntimeException("GameManager.attemptBuy cannot buy unknown generators");
        }
        calcTotalResourcesPerSec();
        return true;
    }

    public int getNumOfGenerators(int type) {
        switch (type) {
            case ADWARE:
                return coreAdware.getNumOfGenerators();
            case MALWARE:
                return coreMalware.getNumOfGenerators();
            case WORM:
                return coreWorm.getNumOfGenerators();
            case TROJAN:
                return coreTrojan.getNumOfGenerators();
            case ROOTKIT:
                return coreRootkit.getNumOfGenerators();
            case HIJACKER:
                return coreHijacker.getNumOfGenerators();
            default:
                throw new RuntimeException("GameManager.getNumOfGenerators cannot lookup unknown generator type");
        }
    }

    public int getCostOfGenerators(int type) {
        switch (type) {
            case ADWARE:
                return coreAdware.getCost();
            case MALWARE:
                return coreMalware.getCost();
            case WORM:
                return coreWorm.getCost();
            case TROJAN:
                return coreTrojan.getCost();
            case ROOTKIT:
                return coreRootkit.getCost();
            case HIJACKER:
                return coreHijacker.getCost();
            default:
                throw new RuntimeException("GameManager.getNumOfGenerators cannot lookup unknown generator type");
        }
    }

    public String getGenRateString(int type) {
        switch (type) {
            case ADWARE:
                return convertNumToString(coreAdware.calcVirusGenPerSec());
            case MALWARE:
                return convertNumToString(coreMalware.calcVirusGenPerSec());
            case WORM:
                return convertNumToString(coreWorm.calcVirusGenPerSec());
            case TROJAN:
                return convertNumToString(coreTrojan.calcVirusGenPerSec());
            case ROOTKIT:
                return convertNumToString(coreRootkit.calcVirusGenPerSec());
            case HIJACKER:
                return convertNumToString(coreHijacker.calcVirusGenPerSec());
            default:
                throw new RuntimeException("GameManager.getNumOfGenerators cannot lookup unknown generator type");
        }
    }

    public String totalGenRateString() {
        return "Viruses Per Second: " + convertNumToString(getResourcesPerSec());
    }

    public double getTotalResources() {
        return totalResources;
    }

    public void calcTotalResourcesPerSec() {
        resourcesPerSec = ((coreAdware.calcVirusGenPerSec() +
                coreMalware.calcVirusGenPerSec() +
                coreWorm.calcVirusGenPerSec() +
                coreTrojan.calcVirusGenPerSec() +
                coreRootkit.calcVirusGenPerSec() +
                coreHijacker.calcVirusGenPerSec())
                * modifier)*(resetLevel+1);
    }

    public double getResourcesPerSec() {
        return resourcesPerSec;
    }

    public double getTotalResourcesPerFrame(int FPS) {
        return resourcesPerSec / FPS;
    }

    public String getResourcesString() {
        return convertNumToString(totalResources);
    }

    public String convertNumToString(double number) {
        DecimalFormat df; // = new DecimalFormat("###.000");
        String text = "";
        int thousandsModifier = 0;

        while (number >= 1d) {
            number /= 1000d;
            thousandsModifier++;
        }

        if (thousandsModifier > 0) {
            number *= 1000d;
            thousandsModifier--;
        }

        if (thousandsModifier > 0){
            df = new DecimalFormat("###.00");
        } else {
            df = new DecimalFormat("###");
        }

        text = df.format(number);
        //text = df.format(number).replaceAll("###....", "");

        switch (thousandsModifier) {
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

    public void storeData(SharedPreferences sharedPref) {
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

        // store data for upgrade levels for action skills
        editor.putInt("AS_auto_click_upgrade_level", ASautoTapUpgradeLevel);
        editor.putInt("AS_increase_resource_generation_upgrade_level", ASincreaseResourceGenerationUpgradeLevel);
        editor.putInt("AS_time_warp_upgrade_level", AStimeWarpUpgradeLevel);

        // store data for the current cost of upgrades
        // ISN'T THIS REDUNDANT WITH KEEPING TRACK OF THE UPGRADE LEVELS?
        editor.putInt("AS_auto_tap_upgrade_cost", autoTapCost);
        editor.putInt("AS_increase_resource_generation_upgrade_cost", resGenCost);
        editor.putInt("AS_time_warp_upgrade_cost", timeWarpCost);

        // store data for upgrade levels of generators
        editor = putDouble(editor, "malware_upgrade_level", coreMalware.getUpgradeLevel());
        editor = putDouble(editor, "worm_upgrade_level", coreWorm.getUpgradeLevel());
        editor = putDouble(editor, "adware_upgrade_level", coreAdware.getUpgradeLevel());
        editor = putDouble(editor, "rootkit_upgrade_level", coreRootkit.getUpgradeLevel());
        editor = putDouble(editor, "trojan_upgrade_level", coreTrojan.getUpgradeLevel());
        editor = putDouble(editor, "hijacker_upgrade_level", coreHijacker.getUpgradeLevel());

        // store data for time
        editor.putLong("time", System.currentTimeMillis());

        // apply changes
        editor.apply();
    }

    public void resetData(SharedPreferences sharedPref) {
        // set up editor
        SharedPreferences.Editor editor = sharedPref.edit();

        // reset resources
        editor = putDouble(editor, "resources", 0);

        // reset viruses
        editor.putInt("num_malware", 0);
        editor.putInt("num_worms", 0);
        editor.putInt("num_adware", 0);
        editor.putInt("num_rootkits", 0);
        editor.putInt("num_trojans", 0);
        editor.putInt("num_hijackers", 0);

        // reset upgrades
        editor.putInt("current_upgrade_visibility_level", 0);
        editor.putInt("AS_auto_click_upgrade_level", 0);
        editor.putInt("AS_increase_resource_generation_upgrade_level", 0);
        editor.putInt("AS_time_warp_upgrade_level", 0);
        editor.putInt("AS_auto_tap_upgrade_cost", 0);
        editor.putInt("AS_increase_resource_generation_upgrade_cost", 0);
        editor.putInt("AS_time_warp_upgrade_cost", 0);
        editor = putDouble(editor, "malware_upgrade_level", 0);
        editor = putDouble(editor, "worm_upgrade_level", 0);
        editor = putDouble(editor, "adware_upgrade_level", 0);
        editor = putDouble(editor, "rootkit_upgrade_level", 0);
        editor = putDouble(editor, "trojan_upgrade_level", 0);
        editor = putDouble(editor, "hijacker_upgrade_level", 0);

        // reset time
        editor.putLong("time", System.currentTimeMillis());

        // apply changes
        editor.apply();
    }

    public void loadData(SharedPreferences sharedPref) {
        if (sharedPref == null) {
            throw new RuntimeException("Attempted to load resources from a null SharedPreferences pointer");
        } else {
            // load resources
            totalResources = getDouble(sharedPref, "resources", 0.0);

            // load viruses
            coreMalware.addVirus(sharedPref.getInt("num_malware", 0));
            coreWorm.addVirus(sharedPref.getInt("num_worms", 0));
            coreAdware.addVirus(sharedPref.getInt("num_adware", 0));
            coreRootkit.addVirus(sharedPref.getInt("num_rootkits", 0));
            coreTrojan.addVirus(sharedPref.getInt("num_trojans", 0));
            coreHijacker.addVirus(sharedPref.getInt("num_hijackers", 0));

            // load upgrade levels for action skills
            ASautoTapUpgradeLevel = sharedPref.getInt("AS_auto_click_upgrade_level", 0);
            ASincreaseResourceGenerationUpgradeLevel = sharedPref.getInt("AS_increase_resource_generation_upgrade_level", 0);
            AStimeWarpUpgradeLevel = sharedPref.getInt("AS_time_warp_upgrade_level", 0);

            // load upgrade costs for action skills
            autoTapCost = sharedPref.getInt("AS_auto_tap_upgrade_cost", 10);
            resGenCost = sharedPref.getInt("AS_increase_resource_generation_upgrade_cost", 20);
            timeWarpCost = sharedPref.getInt("AS_time_warp_upgrade_cost", 30);

            // load upgrade levels for generators
            coreMalware.setUpgradeLevel(getDouble(sharedPref, "malware_upgrade_level", 1d));
            coreWorm.setUpgradeLevel(getDouble(sharedPref, "worm_upgrade_level", 1d));
            coreAdware.setUpgradeLevel(getDouble(sharedPref, "adware_upgrade_level", 1d));
            coreRootkit.setUpgradeLevel(getDouble(sharedPref, "rootkit_upgrade_level", 1d));
            coreTrojan.setUpgradeLevel(getDouble(sharedPref, "trojan_upgrade_level", 1d));
            coreHijacker.setUpgradeLevel(getDouble(sharedPref, "hijacker_upgrade_level", 1d));
        }
    }

    public long getStoredTime(SharedPreferences sharedPref) {
        return sharedPref.getLong("time", 0);
    }

    // Some SharedPreferences wizardy since SharedPreferences can't store doubles natively
    // CODE TAKEN FROM http://stackoverflow.com/questions/16319237/cant-put-double-sharedpreferences
    private SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    private double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }
}
