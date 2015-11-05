package com.group6.malaware;

import android.content.SharedPreferences;

/**
 * Created by michaeldegraw on 10/5/15.
 */

// THIS CLASS NEEDS TO BE OVERHAULED
public class VirusManager {
    private int malware;
    private int worms;
    private int adware;
    private int rootkits;
    private int trojans;
    private int hijackers;
    private int polymorphic;

    // constants
    private static final int MALWARE_PER_SEC = 1;
    private static final int WORMS_PER_SEC = 5;
    private static final int ADWARE_PER_SEC = 10;
    private static final int ROOTKITS_PER_SEC = 15;
    private static final int TROJANS_PER_SEC = 20;
    private static final int HIJACKERS_PER_SEC = 25;
    private static final int POLYMORPHIC_PER_SEC = 30;

    // these would most likely be better of contained in an seperate
    //  manager (or definition) class since both VirusManager and
    //  ResourceManager make use of these constants
    public static final int MALWARE = 0x00000000;
    public static final int WORM = 0x00000001;
    public static final int ADWARE = 0x00000010;
    public static final int ROOTKIT = 0x00000011;
    public static final int TROJAN = 0x00000100;
    public static final int HIJACKER = 0x00000101;
    public static final int POLYMORPHIC = 0x00000110;

    VirusManager(){

    }

    VirusManager(int virusType, int amount){
        addViruses(virusType, amount);
    }

    public void addViruses(int virusType, int number){
        switch (virusType){
            case MALWARE:
                malware += number;
                break;
            case WORM:
                worms += number;
                break;
            case ADWARE:
                adware += number;
                break;
            case ROOTKIT:
                rootkits += number;
                break;
            case TROJAN:
                trojans += number;
                break;
            case HIJACKER:
                hijackers += number;
                break;
            case POLYMORPHIC:
                polymorphic += number;
                break;
            default:
                throw new RuntimeException("Unknown virus type passed to VirusManager.addViruses");
        }
    }

    public int getViruses(int virusType){
        switch (virusType){
            case MALWARE:
                return malware;
            case WORM:
                return worms;
            case ADWARE:
                return adware;
            case ROOTKIT:
                return rootkits;
            case TROJAN:
                return trojans;
            case HIJACKER:
                return hijackers;
            case POLYMORPHIC:
                return polymorphic;
            default:
                throw new RuntimeException("Unknown virus type passed to VirusManager.getViruses");
        }
    }

    public double getTotalResourceGenerationPerSec(){
        double temp = 0.0;

        temp += (malware * MALWARE_PER_SEC);
        temp += (worms * WORMS_PER_SEC);
        temp += (adware * ADWARE_PER_SEC);
        temp += (rootkits * ROOTKITS_PER_SEC);
        temp += (trojans * TROJANS_PER_SEC);
        temp += (hijackers * HIJACKERS_PER_SEC);
        temp += (polymorphic * POLYMORPHIC_PER_SEC);

        return temp;
    }

    public double getTotalResourceGenerationPerFrame(int FPS){
        return getTotalResourceGenerationPerSec() / FPS;
    }

    public void loadViruses(SharedPreferences sharedPref){
        malware = sharedPref.getInt("num_malware", 0);
        worms = sharedPref.getInt("num_worms", 0);
        adware = sharedPref.getInt("num_adware", 0);
        rootkits = sharedPref.getInt("num_rootkits", 0);
        trojans = sharedPref.getInt("num_trojans", 0);
        hijackers = sharedPref.getInt("num_hijackers", 0);
        polymorphic = sharedPref.getInt("num_polymorphic", 0);
    }

    public void storeViruses(SharedPreferences sharedPref){
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("num_malware", malware);
        editor.putInt("num_worms", worms);
        editor.putInt("num_adware", adware);
        editor.putInt("num_rootkits", rootkits);
        editor.putInt("num_trojans", trojans);
        editor.putInt("num_hijackers", hijackers);
        editor.putInt("num_polymorphic", polymorphic);
        editor.apply();
    }
}