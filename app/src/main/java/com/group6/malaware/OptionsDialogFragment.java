package com.group6.malaware;

import android.app.Activity;
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
 * Created by Cyril Mathew on 11/23/15.
 */
public class OptionsDialogFragment extends DialogFragment {
    OptionsActivity returnActivity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String optionsName = getArguments().getString("Title");
        returnActivity = (OptionsActivity) getActivity();
        final CharSequence description = getArguments().getString("Description");

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(optionsName);
        builder.setMessage(description)
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        switch (optionsName) {
                            case "Reset Stats":
                                returnActivity.resetStats();
                                Log.i("Info", "Successfully initiated reset");
                                break;
                        }
                    }
                })
                .setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}