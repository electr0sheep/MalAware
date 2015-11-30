package com.group6.malaware;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Cyril Mathew on 11/4/15.
 * Edited by Adrian Palmerola 11/26/15
 */
public class UpgradeDialogFragment extends DialogFragment {
    MainActivity callingActivity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String upgradeName = getArguments().getString("Title");
        callingActivity = (MainActivity) getActivity();
        final CharSequence description = getArguments().getString("Description");

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(upgradeName);
        builder.setMessage(description)
                .setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            switch (upgradeName) {
                                case "Auto Tap":
                                    callingActivity.gameManager.coreAutoTap.upgrade();
                                    callingActivity.fabAutoTap.setVisibility(FloatingActionButton.VISIBLE);
                                    break;
                                case "Increase Resource Generation":
                                    callingActivity.gameManager.coreIncreaseResource.upgrade();
                                    callingActivity.fabIncreaseResourceGeneration.setVisibility(FloatingActionButton.VISIBLE);
                                    break;
                                case "Time Warp":
                                    callingActivity.gameManager.coreTimeWarp.upgrade();
                                    callingActivity.fabTimeWarp.setVisibility(FloatingActionButton.VISIBLE);
                                    break;
                                case "Adware":
                                    callingActivity.gameManager.attemptUpgradeGenerator(0);
                                    break;
                                case "Malware":
                                    callingActivity.gameManager.attemptUpgradeGenerator(1);
                                    break;
                                case "Worm":
                                    callingActivity.gameManager.attemptUpgradeGenerator(2);
                                    break;
                                case "Trojan":
                                    callingActivity.gameManager.attemptUpgradeGenerator(3);
                                    break;
                                case "Rootkit":
                                    callingActivity.gameManager.attemptUpgradeGenerator(4);
                                    break;
                                case "Hijacker":
                                    callingActivity.gameManager.attemptUpgradeGenerator(5);
                                    break;
                                case "Boot Infector":
                                    callingActivity.gameManager.attemptUpgradeGenerator(6);
                                    break;
                                case "Polymorphic Malware":
                                    callingActivity.gameManager.attemptUpgradeGenerator(7);
                                    break;
                                case "4K Malware":
                                    callingActivity.gameManager.attemptUpgradeGenerator(8);
                                    break;
                                case "Code Red Malware":
                                    callingActivity.gameManager.attemptUpgradeGenerator(9);
                                    break;
                                case "Regrowing Virus":
                                    callingActivity.gameManager.attemptUpgradeGenerator(10);
                                    break;
                                case "Bot Net":
                                    callingActivity.gameManager.attemptUpgradeGenerator(11);
                                    break;
                                case "Zombie Virus":
                                    callingActivity.gameManager.attemptUpgradeGenerator(12);
                                    break;
                                case "Sunday Virus":
                                    callingActivity.gameManager.attemptUpgradeGenerator(13);
                                    break;
                                case "All-Purpose Worm":
                                    callingActivity.gameManager.attemptUpgradeGenerator(14);
                                    break;
                                case "Zmist Virus":
                                    callingActivity.gameManager.attemptUpgradeGenerator(15);
                                    break;
                                case "Military Malware":
                                    callingActivity.gameManager.attemptUpgradeGenerator(16);
                                    break;
                                case "Super Trojan":
                                    callingActivity.gameManager.attemptUpgradeGenerator(17);
                                    break;
                                case "Tyrannical Adware":
                                    callingActivity.gameManager.attemptUpgradeGenerator(18);
                                    break;
                                case "Ransom Virus":
                                    callingActivity.gameManager.attemptUpgradeGenerator(19);
                                    break;
                                case "ILOVEU Virus":
                                    callingActivity.gameManager.attemptUpgradeGenerator(20);
                                    break;
                            }
                        } catch (RuntimeException e){
                            callingActivity.myToast.cancel();
                            callingActivity.myToast = Toast.makeText(callingActivity, "Not enough resources", Toast.LENGTH_SHORT);
                            callingActivity.myToast.show();
                        }
                    }
                })
                .setNegativeButton("Don't Buy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
