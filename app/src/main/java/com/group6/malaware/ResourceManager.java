package com.group6.malaware;

import android.content.SharedPreferences;

import java.text.DecimalFormat;

/**
 * Created by michaeldegraw on 9/22/15.
 */

public class ResourceManager {
   /* private double resources;

    public static final int MALWARE = 0x00000000;
    public static final int WORM = 0x00000001;
    public static final int ADWARE = 0x00000010;
    public static final int ROOTKIT = 0x00000011;
    public static final int TROJAN = 0x00000100;
    public static final int HIJACKER = 0x00000101;
    public static final int POLYMORPHIC = 0x00000110;

    public ResourceManager(){
        resources = 0;
    }

    public ResourceManager(double initResources){
        resources = initResources;
    }

    public String getResourcesString() {
        DecimalFormat df = new DecimalFormat("###.000");
        String text = "";
        double temp = resources;
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

    public double getResourcesDouble(){
        return resources;
    }

    public void addResources(double numberOfResources){
        resources += numberOfResources;
    }

    public boolean tryFor(int virusType, int number){
        double temp = resources;
        double sub = 0.0;

        // fill sub with the appropriate number of resources
        switch (virusType){
            case MALWARE:
                sub = 10.0;
                break;
            case WORM:
                sub = 100.0;
                break;
            case ADWARE:
                sub = 1000.0;
                break;
            case ROOTKIT:
                sub = 10000.0;
                break;
            case TROJAN:
                sub = 100000.0;
                break;
            case HIJACKER:
                sub = 1000000.0;
                break;
            case POLYMORPHIC:
                sub = 10000000.0;
                break;
        }

        // multiply base cost by number we are attempting for
        sub *= number;

        // subtract resources
        temp -= sub;

        // see if that put us in the negative
        if (temp >= 0) {
            resources = temp;
            return true;
        } else {
            return false;
        }
    }

    /*public void storeResoures(SharedPreferences sharedPref){
        // set up editor
        SharedPreferences.Editor editor = sharedPref.edit();

        editor = putDouble(editor, "resources", resources);
        editor.apply();
    }

    public void resetResources(SharedPreferences sharedPref){
        // set up editor
        SharedPreferences.Editor editor = sharedPref.edit();

        editor = putDouble(editor, "resources", 0d);
        editor.apply();
    }

    public void loadResources(SharedPreferences sharedPref){
        if (sharedPref == null){
            throw new RuntimeException("Attempted to load resources from a null SharedPreferences pointer");
        } else {
            resources = getDouble(sharedPref, "resources", 0.0);
        }
    }

    // Some SharedPreferences wizardy since SharedPreferences can't store doubles natively
    // CODE TAKEN FROM http://stackoverflow.com/questions/16319237/cant-put-double-sharedpreferences
    private SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value){
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    private double getDouble(final SharedPreferences prefs, final String key, final double defaultValue){
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }*/
}
