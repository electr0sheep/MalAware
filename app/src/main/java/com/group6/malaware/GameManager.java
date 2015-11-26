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
    Generator coreMalware = new Generator(1.5, 46, 10, 1.2);
    Generator coreWorm = new Generator(3, 100, 24, 1.2);
    Generator coreTrojan = new Generator(8, 249, 50, 1.2);
    Generator coreRootkit = new Generator(15, 394, 100, 1.2);
    Generator coreHijacker = new Generator(50, 790, 300, 1.2);
    Generator coreBootInfector = new Generator(80, 1601, 400, 1.2);
    Generator corePolymorphicMalware = new Generator(150, 3021, 700, 1.2);
    Generator coreFourKMalware = new Generator(250, 2979, 800, 1.25);
    Generator coreCodeRedMalware = new Generator(400, 4318, 1300, 1.25);
    Generator coreRegrowingVirus = new Generator(700, 6646, 2000, 1.25);
    Generator coreBotNet = new Generator(1000, 10000, 3500, 1.25);
    Generator coreZombieVirus = new Generator(1500, 15000, 5000, 1.25);
    Generator coreSundayVirus = new Generator(2000, 20815, 6000, 1.25);
    Generator coreAllPurposeWorm = new Generator(3000, 27243, 8500, 1.25);
    Generator coreZmistVirus = new Generator(5000, 33310, 15000, 1.25);
    Generator coreMilitaryMalware = new Generator(7000, 27844, 13000, 1.3);
    Generator coreSuperTrojan = new Generator(10000, 33228, 20000, 1.3);
    Generator coreTyrannicalAdware = new Generator(13500, 41247, 28000, 1.3);
    Generator coreRansomVirus = new Generator(16000, 25800, 14000, 1.4);
    Generator coreILOVEUVirus = new Generator(20000, 31677, 17000, 1.4);
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
    public static final int BOOTINFECTOR = 6;
    public static final int POLYMORPHICMALWARE = 7;
    public static final int FOURKMALWARE = 8;
    public static final int CODEREDMALWARE = 9;
    public static final int REGROWINGVIRUS = 10;
    public static final int BOTNET = 11;
    public static final int ZOMBIEVIRUS = 12;
    public static final int SUNDAYVIRUS = 13;
    public static final int ALLPURPOSEWORM = 14;
    public static final int ZMISTVIRUS = 15;
    public static final int MILITARYMALWARE = 16;
    public static final int SUPERTROJAN = 17;
    public static final int TYRANNICALADWARE = 18;
    public static final int RANSOMVIRUS = 19;
    public static final int ILOVEUVIRUS = 20;

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
            case BOOTINFECTOR:
                coreBootInfector.addVirus(amount);
                break;
            case POLYMORPHICMALWARE:
                corePolymorphicMalware.addVirus(amount);
                break;
            case FOURKMALWARE:
                coreFourKMalware.addVirus(amount);
                break;
            case CODEREDMALWARE:
                coreCodeRedMalware.addVirus(amount);
                break;
            case REGROWINGVIRUS:
                coreRegrowingVirus.addVirus(amount);
                break;
            case BOTNET:
                coreBotNet.addVirus(amount);
                break;
            case ZOMBIEVIRUS:
                coreZombieVirus.addVirus(amount);
                break;
            case SUNDAYVIRUS:
                coreSundayVirus.addVirus(amount);
                break;
            case ALLPURPOSEWORM:
                coreAllPurposeWorm.addVirus(amount);
                break;
            case ZMISTVIRUS:
                coreZmistVirus.addVirus(amount);
                break;
            case MILITARYMALWARE:
                coreMilitaryMalware.addVirus(amount);
                break;
            case SUPERTROJAN:
                coreSuperTrojan.addVirus(amount);
                break;
            case TYRANNICALADWARE:
                coreTyrannicalAdware.addVirus(amount);
                break;
            case RANSOMVIRUS:
                coreRansomVirus.addVirus(amount);
                break;
            case ILOVEUVIRUS:
                coreILOVEUVirus.addVirus(amount);
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
            case BOOTINFECTOR:
                if ((amount * coreBootInfector.getCost()) <= totalResources) {
                    totalResources -= (amount * coreBootInfector.getCost());
                    coreBootInfector.addVirus(amount);
                    break;
                } else
                    return false;
            case POLYMORPHICMALWARE:
                if ((amount * corePolymorphicMalware.getCost()) <= totalResources) {
                    totalResources -= (amount * corePolymorphicMalware.getCost());
                    corePolymorphicMalware.addVirus(amount);
                    break;
                } else
                    return false;
            case FOURKMALWARE:
                if ((amount * coreFourKMalware.getCost()) <= totalResources) {
                    totalResources -= (amount * coreFourKMalware.getCost());
                    coreFourKMalware.addVirus(amount);
                    break;
                } else
                    return false;
            case CODEREDMALWARE:
                if ((amount * coreCodeRedMalware.getCost()) <= totalResources) {
                    totalResources -= (amount * coreCodeRedMalware.getCost());
                    coreCodeRedMalware.addVirus(amount);
                    break;
                } else
                    return false;
            case REGROWINGVIRUS:
                if ((amount * coreRegrowingVirus.getCost()) <= totalResources) {
                    totalResources -= (amount * coreRegrowingVirus.getCost());
                    coreRegrowingVirus.addVirus(amount);
                    break;
                } else
                    return false;
            case BOTNET:
                if ((amount * coreBotNet.getCost()) <= totalResources) {
                    totalResources -= (amount * coreBotNet.getCost());
                    coreBotNet.addVirus(amount);
                    break;
                } else
                    return false;
            case ZOMBIEVIRUS:
                if ((amount * coreZombieVirus.getCost()) <= totalResources) {
                    totalResources -= (amount * coreZombieVirus.getCost());
                    coreZombieVirus.addVirus(amount);
                    break;
                } else
                    return false;
            case SUNDAYVIRUS:
                if ((amount * coreSundayVirus.getCost()) <= totalResources) {
                    totalResources -= (amount * coreSundayVirus.getCost());
                    coreSundayVirus.addVirus(amount);
                    break;
                } else
                    return false;
            case ALLPURPOSEWORM:
                if ((amount * coreAllPurposeWorm.getCost()) <= totalResources) {
                    totalResources -= (amount * coreAllPurposeWorm.getCost());
                    coreAllPurposeWorm.addVirus(amount);
                    break;
                } else
                    return false;
            case ZMISTVIRUS:
                if ((amount * coreZmistVirus.getCost()) <= totalResources) {
                    totalResources -= (amount * coreZmistVirus.getCost());
                    coreZmistVirus.addVirus(amount);
                    break;
                } else
                    return false;
            case MILITARYMALWARE:
                if ((amount * coreMilitaryMalware.getCost()) <= totalResources) {
                    totalResources -= (amount * coreMilitaryMalware.getCost());
                    coreMilitaryMalware.addVirus(amount);
                    break;
                } else
                    return false;
            case SUPERTROJAN:
                if ((amount * coreSuperTrojan.getCost()) <= totalResources) {
                    totalResources -= (amount * coreSuperTrojan.getCost());
                    coreSuperTrojan.addVirus(amount);
                    break;
                } else
                    return false;
            case TYRANNICALADWARE:
                if ((amount * coreTyrannicalAdware.getCost()) <= totalResources) {
                    totalResources -= (amount * coreTyrannicalAdware.getCost());
                    coreTyrannicalAdware.addVirus(amount);
                    break;
                } else
                    return false;
            case RANSOMVIRUS:
                if ((amount * coreRansomVirus.getCost()) <= totalResources) {
                    totalResources -= (amount * coreRansomVirus.getCost());
                    coreRansomVirus.addVirus(amount);
                    break;
                } else
                    return false;
            case ILOVEUVIRUS:
                if ((amount * coreILOVEUVirus.getCost()) <= totalResources) {
                    totalResources -= (amount * coreILOVEUVirus.getCost());
                    coreILOVEUVirus.addVirus(amount);
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
            case BOOTINFECTOR:
                return coreBootInfector.getNumOfGenerators();
            case POLYMORPHICMALWARE:
                return corePolymorphicMalware.getNumOfGenerators();
            case FOURKMALWARE:
                return coreFourKMalware.getNumOfGenerators();
            case CODEREDMALWARE:
                return coreCodeRedMalware.getNumOfGenerators();
            case REGROWINGVIRUS:
                return coreRegrowingVirus.getNumOfGenerators();
            case BOTNET:
                return coreBotNet.getNumOfGenerators();
            case ZOMBIEVIRUS:
                return coreZombieVirus.getNumOfGenerators();
            case SUNDAYVIRUS:
                return coreSundayVirus.getNumOfGenerators();
            case ALLPURPOSEWORM:
                return coreAllPurposeWorm.getNumOfGenerators();
            case ZMISTVIRUS:
                return coreZmistVirus.getNumOfGenerators();
            case MILITARYMALWARE:
                return coreMilitaryMalware.getNumOfGenerators();
            case SUPERTROJAN:
                return coreSuperTrojan.getNumOfGenerators();
            case TYRANNICALADWARE:
                return coreTyrannicalAdware.getNumOfGenerators();
            case RANSOMVIRUS:
                return coreRansomVirus.getNumOfGenerators();
            case ILOVEUVIRUS:
                return coreILOVEUVirus.getNumOfGenerators();
            default:
                throw new RuntimeException("GameManager.getNumOfGenerators cannot lookup unknown generator type");
        }
    }

    public String getCostOfGeneratorsString(int type) {
        switch (type) {
            case ADWARE:
                return convertNumToString(coreAdware.getCost());
            case MALWARE:
                return convertNumToString(coreMalware.getCost());
            case WORM:
                return convertNumToString(coreWorm.getCost());
            case TROJAN:
                return convertNumToString(coreTrojan.getCost());
            case ROOTKIT:
                return convertNumToString(coreRootkit.getCost());
            case HIJACKER:
                return convertNumToString(coreHijacker.getCost());
            case BOOTINFECTOR:
                return convertNumToString(coreBootInfector.getCost());
            case POLYMORPHICMALWARE:
                return convertNumToString(corePolymorphicMalware.getCost());
            case FOURKMALWARE:
                return convertNumToString(coreFourKMalware.getCost());
            case CODEREDMALWARE:
                return convertNumToString(coreCodeRedMalware.getCost());
            case REGROWINGVIRUS:
                return convertNumToString(coreRegrowingVirus.getCost());
            case BOTNET:
                return convertNumToString(coreBotNet.getCost());
            case ZOMBIEVIRUS:
                return convertNumToString(coreZombieVirus.getCost());
            case SUNDAYVIRUS:
                return convertNumToString(coreSundayVirus.getCost());
            case ALLPURPOSEWORM:
                return convertNumToString(coreAllPurposeWorm.getCost());
            case ZMISTVIRUS:
                return convertNumToString(coreZmistVirus.getCost());
            case MILITARYMALWARE:
                return convertNumToString(coreMilitaryMalware.getCost());
            case SUPERTROJAN:
                return convertNumToString(coreSuperTrojan.getCost());
            case TYRANNICALADWARE:
                return convertNumToString(coreTyrannicalAdware.getCost());
            case RANSOMVIRUS:
                return convertNumToString(coreRansomVirus.getCost());
            case ILOVEUVIRUS:
                return convertNumToString(coreILOVEUVirus.getCost());
            default:
                throw new RuntimeException("GameManager.getNumOfGenerators cannot lookup unknown generator type");
        }
    }

    public double getGenRate(int type) {
        switch (type) {
            case ADWARE:
                return coreAdware.calcVirusGenPerSec();
            case MALWARE:
                return coreMalware.calcVirusGenPerSec();
            case WORM:
                return coreWorm.calcVirusGenPerSec();
            case TROJAN:
                return coreTrojan.calcVirusGenPerSec();
            case ROOTKIT:
                return coreRootkit.calcVirusGenPerSec();
            case HIJACKER:
                return coreHijacker.calcVirusGenPerSec();
            case BOOTINFECTOR:
                return coreBootInfector.calcVirusGenPerSec();
            case POLYMORPHICMALWARE:
                return corePolymorphicMalware.calcVirusGenPerSec();
            case FOURKMALWARE:
                return coreFourKMalware.calcVirusGenPerSec();
            case CODEREDMALWARE:
                return coreCodeRedMalware.calcVirusGenPerSec();
            case REGROWINGVIRUS:
                return coreRegrowingVirus.calcVirusGenPerSec();
            case BOTNET:
                return coreBotNet.calcVirusGenPerSec();
            case ZOMBIEVIRUS:
                return coreZombieVirus.calcVirusGenPerSec();
            case SUNDAYVIRUS:
                return coreSundayVirus.calcVirusGenPerSec();
            case ALLPURPOSEWORM:
                return coreAllPurposeWorm.calcVirusGenPerSec();
            case ZMISTVIRUS:
                return coreZmistVirus.calcVirusGenPerSec();
            case MILITARYMALWARE:
                return coreMilitaryMalware.calcVirusGenPerSec();
            case SUPERTROJAN:
                return coreSuperTrojan.calcVirusGenPerSec();
            case TYRANNICALADWARE:
                return coreTyrannicalAdware.calcVirusGenPerSec();
            case RANSOMVIRUS:
                return coreRansomVirus.calcVirusGenPerSec();
            case ILOVEUVIRUS:
                return coreILOVEUVirus.calcVirusGenPerSec();
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
                coreHijacker.calcVirusGenPerSec() +
                coreBootInfector.calcVirusGenPerSec() +
                corePolymorphicMalware.calcVirusGenPerSec()+
                coreFourKMalware.calcVirusGenPerSec()+
                coreCodeRedMalware.calcVirusGenPerSec() +
                coreRegrowingVirus.calcVirusGenPerSec()+
                coreBotNet.calcVirusGenPerSec()+
                coreZombieVirus.calcVirusGenPerSec()+
                coreSundayVirus.calcVirusGenPerSec()+
                coreAllPurposeWorm.calcVirusGenPerSec()+
                coreZmistVirus.calcVirusGenPerSec()+
                coreMilitaryMalware.calcVirusGenPerSec()+
                coreSuperTrojan.calcVirusGenPerSec()+
                coreTyrannicalAdware.calcVirusGenPerSec()+
                coreRansomVirus.calcVirusGenPerSec()+
                coreILOVEUVirus.calcVirusGenPerSec())
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
        editor.putInt("num_bootinfector", coreBootInfector.getNumOfGenerators());
        editor.putInt("num_polymorphicmalware", corePolymorphicMalware.getNumOfGenerators());
        editor.putInt("num_fourkmalware", coreFourKMalware.getNumOfGenerators());
        editor.putInt("num_coderedmalware", coreCodeRedMalware.getNumOfGenerators());
        editor.putInt("num_regrowingvirus", coreRegrowingVirus.getNumOfGenerators());
        editor.putInt("num_botnet", coreBotNet.getNumOfGenerators());
        editor.putInt("num_zombievirus", coreZombieVirus.getNumOfGenerators());
        editor.putInt("num_sundayvirus", coreSundayVirus.getNumOfGenerators());
        editor.putInt("num_allpurposeworm", coreAllPurposeWorm.getNumOfGenerators());
        editor.putInt("num_zmistvirus", coreZmistVirus.getNumOfGenerators());
        editor.putInt("num_militarymalware", coreMilitaryMalware.getNumOfGenerators());
        editor.putInt("num_supertrojan", coreSuperTrojan.getNumOfGenerators());
        editor.putInt("num_tyrannicaladware", coreTyrannicalAdware.getNumOfGenerators());
        editor.putInt("num_ransomvirus", coreRansomVirus.getNumOfGenerators());
        editor.putInt("num_iloveuvirus", coreILOVEUVirus.getNumOfGenerators());

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
        editor = putDouble(editor, "bootinfector_upgrade_level", coreBootInfector.getUpgradeLevel());
        editor = putDouble(editor, "polymorphicmalware_upgrade_level", corePolymorphicMalware.getUpgradeLevel());
        editor = putDouble(editor, "fourkmalware_upgrade_level", coreFourKMalware.getUpgradeLevel());
        editor = putDouble(editor, "coderedmalware_upgrade_level", coreCodeRedMalware.getUpgradeLevel());
        editor = putDouble(editor, "regrowingvirus_upgrade_level", coreRegrowingVirus.getUpgradeLevel());
        editor = putDouble(editor, "botnet_upgrade_level", coreBotNet.getUpgradeLevel());
        editor = putDouble(editor, "zombievirus_upgrade_level", coreZombieVirus.getUpgradeLevel());
        editor = putDouble(editor, "sundayvirus_upgrade_level", coreSundayVirus.getUpgradeLevel());
        editor = putDouble(editor, "allpurposeworm_upgrade_level", coreAllPurposeWorm.getUpgradeLevel());
        editor = putDouble(editor, "zmistvirus_upgrade_level", coreZmistVirus.getUpgradeLevel());
        editor = putDouble(editor, "militarymalware_upgrade_level", coreMilitaryMalware.getUpgradeLevel());
        editor = putDouble(editor, "supertrojan_upgrade_level", coreSuperTrojan.getUpgradeLevel());
        editor = putDouble(editor, "tyrannicaladware_upgrade_level", coreTyrannicalAdware.getUpgradeLevel());
        editor = putDouble(editor, "ransomvirus_upgrade_level", coreRansomVirus.getUpgradeLevel());
        editor = putDouble(editor, "iloveuvirus_upgrade_level", coreILOVEUVirus.getUpgradeLevel());

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
        editor.putInt("num_bootinfector", 0);
        editor.putInt("num_polymorphicmalware", 0);
        editor.putInt("num_coderedmalware", 0);
        editor.putInt("num_fourkmalware", 0);
        editor.putInt("num_regrowingvirus", 0);
        editor.putInt("num_botnet", 0);
        editor.putInt("num_zombievirus", 0);
        editor.putInt("num_sundayvirus", 0);
        editor.putInt("num_allpurposeworm", 0);
        editor.putInt("num_zmistvirus", 0);
        editor.putInt("num_militarymalware", 0);
        editor.putInt("num_supertrojan", 0);
        editor.putInt("num_tyrannicaladware", 0);
        editor.putInt("num_ransomvirus", 0);
        editor.putInt("num_iloveuvirus", 0);

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
        editor = putDouble(editor, "bootinfector_upgrade_level", 0);
        editor = putDouble(editor, "polymorphicmalware_upgrade_level", 0);
        editor = putDouble(editor, "fourkmalware_upgrade_level", 0);
        editor = putDouble(editor, "coderedmalware_upgrade_level", 0);
        editor = putDouble(editor, "regrowingvirus_upgrade_level", 0);
        editor = putDouble(editor, "botnet_upgrade_level", 0);
        editor = putDouble(editor, "zombievirus_upgrade_level", 0);
        editor = putDouble(editor, "sundayvirus_upgrade_level", 0);
        editor = putDouble(editor, "allpurposeworm_upgrade_level", 0);
        editor = putDouble(editor, "zmistvirus_upgrade_level", 0);
        editor = putDouble(editor, "militarymalware_upgrade_level", 0);
        editor = putDouble(editor, "supertrojan_upgrade_level", 0);
        editor = putDouble(editor, "tyrannicaladware_upgrade_level", 0);
        editor = putDouble(editor, "ransomvirus_upgrade_level", 0);
        editor = putDouble(editor, "iloveuvirus_upgrade_level", 0);

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
            coreBootInfector.addVirus(sharedPref.getInt("num_bootinfector", 0));
            corePolymorphicMalware.addVirus(sharedPref.getInt("num_polymorphicmalware", 0));
            coreFourKMalware.addVirus(sharedPref.getInt("num_fourkmalware", 0));
            coreCodeRedMalware.addVirus(sharedPref.getInt("num_coderedmalware", 0));
            coreRegrowingVirus.addVirus(sharedPref.getInt("num_regrowingvirus", 0));
            coreBotNet.addVirus(sharedPref.getInt("num_botnet", 0));
            coreZombieVirus.addVirus(sharedPref.getInt("num_zombievirus", 0));
            coreSundayVirus.addVirus(sharedPref.getInt("num_sundayvirus", 0));
            coreAllPurposeWorm.addVirus(sharedPref.getInt("num_allpurposeworm", 0));
            coreZmistVirus.addVirus(sharedPref.getInt("num_zmistvirus", 0));
            coreMilitaryMalware.addVirus(sharedPref.getInt("num_militarymalware", 0));
            coreSuperTrojan.addVirus(sharedPref.getInt("num_supertrojan", 0));
            coreTyrannicalAdware.addVirus(sharedPref.getInt("num_tyrannicaladware", 0));
            coreRansomVirus.addVirus(sharedPref.getInt("num_ransomvirus", 0));
            coreILOVEUVirus.addVirus(sharedPref.getInt("num_iloveuvirus", 0));

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
            coreBootInfector.setUpgradeLevel(getDouble(sharedPref, "bootinfector_upgrade_level", 1d));
            corePolymorphicMalware.setUpgradeLevel(getDouble(sharedPref, "polymorphicmalware_upgrade_level", 1d));
            coreFourKMalware.setUpgradeLevel(getDouble(sharedPref, "fourkmalware_upgrade_level", 1d));
            coreCodeRedMalware.setUpgradeLevel(getDouble(sharedPref, "coderedmalware_upgrade_level", 1d));
            coreRegrowingVirus.setUpgradeLevel(getDouble(sharedPref, "regrowingvirus_upgrade_level", 1d));
            coreBotNet.setUpgradeLevel(getDouble(sharedPref, "botnet_upgrade_level", 1d));
            coreZombieVirus.setUpgradeLevel(getDouble(sharedPref, "zombievirus_upgrade_level", 1d));
            coreSundayVirus.setUpgradeLevel(getDouble(sharedPref, "sundayvirus_upgrade_level", 1d));
            coreAllPurposeWorm.setUpgradeLevel(getDouble(sharedPref, "allpurposeworm_upgrade_level", 1d));
            coreZmistVirus.setUpgradeLevel(getDouble(sharedPref, "zmistvirus_upgrade_level", 1d));
            coreMilitaryMalware.setUpgradeLevel(getDouble(sharedPref, "militarymalware_upgrade_level", 1d));
            coreSuperTrojan.setUpgradeLevel(getDouble(sharedPref, "supertrojan_upgrade_level", 1d));
            coreTyrannicalAdware.setUpgradeLevel(getDouble(sharedPref, "tyrannicaladware_upgrade_level", 1d));
            coreRansomVirus.setUpgradeLevel(getDouble(sharedPref, "ransomvirus_upgrade_level", 1d));
            coreILOVEUVirus.setUpgradeLevel(getDouble(sharedPref, "iloveuvirus_upgrade_level", 1d));

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
