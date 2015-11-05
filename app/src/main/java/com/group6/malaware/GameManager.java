package com.group6.malaware;

import android.util.Log;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * Created by michaeldegraw on 9/22/15.
 * Edited by Michael and Cyril Mathew on 11/4/2015.
 */
public class GameManager
{
    private double totalResources = 0;
    Adware coreAdware = new Adware();
    Malware coreMalware = new Malware();
    Worm coreWorm = new Worm();
    Trojan coreTrojan = new Trojan();
    Rootkit coreRootkit = new Rootkit();
    Hijacker coreHijacker = new Hijacker();

    public double getTotalResources(){
        return totalResources;
    }

    public void addVirus(int type, int amount)
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
            case 6: totalResources++;
            default:
                break;
        }
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

    public double calcTotalResourcesPerSec()
    {
        totalResources = coreAdware.calcVirusGenPerSec()+
                        coreMalware.calcVirusGenPerSec()+
                        coreWorm.calcVirusGenPerSec()+
                        coreTrojan.calcVirusGenPerSec()+
                        coreRootkit.calcVirusGenPerSec()+
                        coreHijacker.calcVirusGenPerSec();

        return totalResources;
    }

    public double getTotalResourcesPerFrame(int FPS){
        return calcTotalResourcesPerSec() / FPS;
    }

    public void attemptBuy(int type, int amount)
    {
        switch(type)
        {
            case 0: if ((amount*coreAdware.getCost()) < totalResources)
                    {
                        totalResources -= (amount*coreAdware.getCost());
                        coreAdware.addVirus(amount);
                    }
                    else
                        Log.i("Error", "Can't buy that many");
                break;
            case 1: if ((amount*coreMalware.getCost()) < totalResources)
                    {
                        totalResources -= (amount*coreMalware.getCost());
                        coreMalware.addVirus(amount);
                    }
                    else
                        Log.i("Error", "Can't buy that many");
                break;
            case 2:if ((amount*coreWorm.getCost()) < totalResources)
                    {
                        totalResources -= (amount*coreWorm.getCost());
                        coreWorm.addVirus(amount);
                    }
                    else
                        Log.i("Error", "Can't buy that many");
                break;
            case 3:if ((amount*coreTrojan.getCost()) < totalResources)
                    {
                        totalResources -= (amount*coreTrojan.getCost());
                        coreTrojan.addVirus(amount);
                    }
                    else
                        Log.i("Error", "Can't buy that many");
                break;
            case 4:if ((amount*coreRootkit.getCost()) < totalResources)
                    {
                        totalResources -= (amount*coreRootkit.getCost());
                        coreRootkit.addVirus(amount);
                    }
                    else
                        Log.i("Error", "Can't buy that many");
                break;
            case 5:if ((amount*coreHijacker.getCost()) < totalResources)
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

}
