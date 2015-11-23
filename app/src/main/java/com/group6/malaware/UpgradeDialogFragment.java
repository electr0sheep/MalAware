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
 * No longer used. To be deleted.
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
                                    callingActivity.gameManager.attemptUpgrade(0);
                                    callingActivity.fabAutoTap.setVisibility(FloatingActionButton.VISIBLE);
                                    break;
                                case "Increase Resource Generation":
                                    callingActivity.gameManager.attemptUpgrade(1);
                                    callingActivity.fabIncreaseResourceGeneration.setVisibility(FloatingActionButton.VISIBLE);
                                    break;
                                case "Time Warp":
                                    callingActivity.gameManager.attemptUpgrade(2);
                                    callingActivity.fabTimeWarp.setVisibility(FloatingActionButton.VISIBLE);
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
