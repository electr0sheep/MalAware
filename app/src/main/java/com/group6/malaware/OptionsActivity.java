package com.group6.malaware;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Cyril Mathew on 11/23/15.
 */
public class OptionsActivity extends AppCompatActivity {

    private Bundle bundle;
    private Intent backIntent;
    private OptionsDialogFragment optionsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_main);


        final ListView optionsList = (ListView) findViewById(R.id.options_list);
        backIntent = getIntent();
        optionsDialog = new OptionsDialogFragment();
        bundle = new Bundle();

        String[] values = new String[] { "Reset Stats", "Start Tutorial"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);

        optionsList.setAdapter(adapter);
        optionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch(position)
                {
                    case 0:
                        bundle.putString("Title", "Reset Stats");
                        bundle.putString("Description", "This will wipe out all resources generated but " +
                                "gain a permanent resource generation increase");
                        optionsDialog.setArguments(bundle);
                        optionsDialog.show(getFragmentManager(), "Blah");
                        break;
                    case 1:
                        //start tutorial
                        break;
                }
            }

        });
    }

    public void resetStats()
    {
        backIntent.putExtra("Reset Stats", true);
        setResult(Activity.RESULT_OK, backIntent);
        finish();

    }

}
