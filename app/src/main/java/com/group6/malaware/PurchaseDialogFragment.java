package com.group6.malaware;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Cyril Mathew on 11/4/15.
 */
public class PurchaseDialogFragment extends DialogFragment
{
    MainActivity callingActivity;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        callingActivity = (MainActivity) getActivity();
        final CharSequence count = getArguments().getString("Count");
        final CharSequence cost = getArguments().getString("Cost");

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString("Title"));
        builder.setMessage("Count of Generators: "+ count + "\n" +
                            "Cost of Each: " + cost + "\n")
                .setPositiveButton("x25", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Log.i("Info", "You clicked x25");
                        //Log.i("Stuff", count.toString());
                        callingActivity.addVirus(getArguments().getInt("Type"), 25);
                    }
                })
                .setNegativeButton("x10", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Log.i("Info", "You clicked x10");
                        callingActivity.addVirus(getArguments().getInt("Type"), 10);

                    }
                })
                .setNeutralButton("x1", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Info", "You clicked x1");
                        callingActivity.addVirus(getArguments().getInt("Type"), 1);
                    }
                });
                    // Create the AlertDialog object and return it
        return builder.create();

    }
}