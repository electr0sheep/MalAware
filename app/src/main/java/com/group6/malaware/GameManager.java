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
public class GameManager {
    private double totalResources = 0;
    private double resourcesPerSec = 0;
    Generator coreAdware = new Generator(10, 1.0);
    Generator coreMalware = new Generator(10, 1.0);
    Generator coreWorm = new Generator(10, 1.0);
    Generator coreTrojan = new Generator(10, 1.0);
    Generator coreRootkit = new Generator(10, 1.0);
    Generator coreHijacker = new Generator(10, 1.0);

    // Generator constants
    public static final int ADWARE = 0x00000000;
    public static final int MALWARE = 0x00000001;
    public static final int WORM = 0x00000010;
    public static final int TROJAN = 0x00000011;
    public static final int ROOTKIT = 0x00000100;
    public static final int HIJACKER = 0x00000101;

    public double getTotalResources() {
        return totalResources;
    }

    public void subtractResources(double amount) {
        if (amount > totalResources){
            throw new RuntimeException("GameManager.subtractResources cannot subtract more resources than available");
        } else {
            totalResources -= amount;
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

    // reworked some faulty logic
    public void calcTotalResourcesPerSec() {
        resourcesPerSec = (coreAdware.calcVirusGenPerSec() +
                coreMalware.calcVirusGenPerSec() +
                coreWorm.calcVirusGenPerSec() +
                coreTrojan.calcVirusGenPerSec() +
                coreRootkit.calcVirusGenPerSec() +
                coreHijacker.calcVirusGenPerSec());
    }

    public double getResourcesPerSec() {
        return resourcesPerSec;
    }

    public void addResources(double amount) {
        totalResources += amount;
    }

    public double getTotalResourcesPerFrame(int FPS) {
        return resourcesPerSec / FPS;
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

    public String getResourcesString() {
        return convertNumToString(totalResources);
    }

    public String convertNumToString(double number) {
        DecimalFormat df = new DecimalFormat("###.000");
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

        text = df.format(number).replaceAll(".000", "");

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

        // store data for time
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
