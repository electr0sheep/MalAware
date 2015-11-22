package com.group6.malaware;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final int FPS = 30;                                          //FPS constant
    private int autoTapCooldown = 0;
    private int increaseResourceGenerationCooldown = 0;
    public GameManager gameManager = new GameManager();
    public SharedPreferences sharedPref;
    public Timer gameLoop;
    private Bundle bundle;                                              //Bundle used to pass data between dialogs
    private DialogFragment purchaseDialog;
    private DialogFragment upgradeDialog;
    private double nextUpgradeDisplay;
    private int currentUpgradeState;
    public Toast myToast;
    public boolean fragmentResult;

    // View variables
    TextView txtResources;
    TextView txtGenRate;
    TextView txtAutoTap;
    TextView txtIncreaseResourceGeneration;
    NavigationView navLeft;
    MenuItem navLeftNoUpgradesAvailable;
    MenuItem navLeftAutoClickASUpgrade;
    MenuItem navLeftResourceGenerationASUpgrade;
    MenuItem navLeftTimeWarpASUpgrade;
    FloatingActionButton fabAutoTap;
    FloatingActionButton fabIncreaseResourceGeneration;
    FloatingActionButton fabTimeWarp;
    FloatingActionButton fabPowerUpAS;

    DrawerLayout dLayout;
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> childCollection;
    ExpandableListView expListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set view variables
        txtResources = (TextView) findViewById(R.id.txt_resource);
        txtGenRate = (TextView) findViewById(R.id.txt_totalGenRate);
        // these text views are for the cooldown of the floating action buttons
        txtAutoTap = (TextView) findViewById(R.id.txt_action_skill_auto_tap);
        txtIncreaseResourceGeneration = (TextView) findViewById(R.id.txt_action_skill_increase_generation);
        // this is the menu
        navLeft = (NavigationView) findViewById(R.id.nav_view_left);
        // these are the items in the menu
        navLeftNoUpgradesAvailable = navLeft.getMenu().findItem(R.id.nav_left_no_upgrades_available);
        navLeftAutoClickASUpgrade = navLeft.getMenu().findItem(R.id.nav_left_auto_click_action_skill_upgrade);
        navLeftResourceGenerationASUpgrade = navLeft.getMenu().findItem(R.id.nav_left_resource_generation_increase_action_skill_upgrade);
        navLeftTimeWarpASUpgrade = navLeft.getMenu().findItem(R.id.nav_left_time_warp_action_skill_upgrade);
        // these are the floating action buttons
        fabAutoTap = (FloatingActionButton) findViewById(R.id.fab_action_skill_auto_tap);
        fabIncreaseResourceGeneration = (FloatingActionButton) findViewById(R.id.fab_action_skill_increase_generation);
        fabTimeWarp = (FloatingActionButton) findViewById(R.id.fab_action_skill_time_warp);
        // this is the toast
        myToast = new Toast(this);
        fragmentResult = false;

        // load previous game
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        gameManager.loadData(sharedPref);
        gameManager.calcTotalResourcesPerSec();

        // load currrentUpgradeState
        currentUpgradeState = sharedPref.getInt("current_upgrade_visibility_level", 0);
        displayUpgradesOnLoad(currentUpgradeState);

        // add appropriate resources if applicable
        if (gameManager.getResourcesPerSec() > 0) {
            double passiveResources = (System.currentTimeMillis()
                    - gameManager.getStoredTime(sharedPref))    // milliseconds that have elapsed
                    / 1000                                      // number of milliseconds per sec
                    * gameManager.getResourcesPerSec();   // number of resources per sec
            gameManager.addResources(passiveResources);         // add that amount to resource pool
            // only show this number if it is significant
            if (passiveResources > 1) {
                Toast.makeText(this, "You have generated " + gameManager.convertNumToString(passiveResources)
                        + " resources while you were away!", Toast.LENGTH_SHORT).show();
            }
        }

        NavigationView navigationViewLeft = (NavigationView) findViewById(R.id.nav_view_left);
        navigationViewLeft.setNavigationItemSelectedListener(this);

        // Initialize game loop
        gameLoop = new Timer();

        gameLoop.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gameManager.addResources(gameManager.getTotalResourcesPerFrame(FPS));
                        txtResources.setText(gameManager.getResourcesString());

                        if (gameManager.getTotalResources() >= nextUpgradeDisplay){
                            currentUpgradeState++;
                            displayUpgrades(currentUpgradeState);
                        }
                    }
                });
            }
        }, 0, 1000 / FPS);

        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        createGroupList();
        createCollection();

        expListView = (ExpandableListView) findViewById(R.id.right_drawer);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(this, groupList, childCollection);
        expListView.setAdapter(expListAdapter);

        View header = (View)getLayoutInflater().inflate(R.layout.nav_header_right, null);
        expListView.addHeaderView(header);

    }

    private void createGroupList() {
        groupList = new ArrayList<String>();
        groupList.add("Adware");
        groupList.add("Malware");
        groupList.add("Worm");
        groupList.add("Trojan");
        groupList.add("Rootkit");
        groupList.add("Hijacker");
    }


    //This definitely needs to go. Rework into working shape
    private void createCollection() {
        // Dummy data
        String[] models = {"Something"};

        childCollection = new LinkedHashMap<String, List<String>>();

        for (String child : groupList) {

                loadChild(models);

            childCollection.put(child, childList);
        }
    }

    private void loadChild(String[] models) {
        childList = new ArrayList<String>();
        for (String model : models)
            childList.add(model);
    }

    @Override
    public void onBackPressed() {
        //Closes the drawer when the Android back button is pressed
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        bundle = new Bundle();
        upgradeDialog = new UpgradeDialogFragment();

        //Currently rudimentary, needs reworking
        switch (id) {
            case R.id.nav_left_auto_click_action_skill_upgrade:
                // check to see if this is the first time they are buying this
                //  if so, display a message box to describe what they are buying
                if (!gameManager.autoTapPurchased()){
                    bundle.putString("Title", "Auto Tap");
                    bundle.putString("Description", "Auto tap allows you to simply \"hold\" the terminal and resources will be continously added to your resource pool");
                    upgradeDialog.setArguments(bundle);
                    upgradeDialog.show(getFragmentManager(), "Blah");
                    // if the user wanted to purchase
                    if (fragmentResult){
                        try {
                            gameManager.subtractResources(10d);
                            fabAutoTap.setVisibility(FloatingActionButton.VISIBLE);
                            if (!gameManager.attemptUpgradeAutoTap()){
                                myToast.cancel();
                                myToast.makeText(this, "Not enough resources", Toast.LENGTH_SHORT).show();
                            }
                        } catch (RuntimeException e) {
                            myToast.cancel();
                            myToast.makeText(this, "Not enough resources", Toast.LENGTH_SHORT).show();
                        }
                        fragmentResult = false;
                    }
                } else {
                    try {
                        gameManager.subtractResources(10d);
                        // increase effectiveness
                    } catch (RuntimeException e) {
                        myToast.cancel();
                        myToast.makeText(this, "Not enough resources", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.nav_left_resource_generation_increase_action_skill_upgrade:
                // check to see if this is the first time they are buying this
                //  if so, display a message box to describe what they are buying
                if (!gameManager.autoTapPurchased()){
                    bundle.putString("Title", "Increase Resource Generation");
                    bundle.putString("Description", "This action skill will increase the amount of resources you passively generate for a short time");
                    upgradeDialog.setArguments(bundle);
                    upgradeDialog.show(getFragmentManager(), "Blah");
                    // if the user wanted to purchase
                    if (fragmentResult){
                        try {
                            gameManager.subtractResources(10d);
                            fabIncreaseResourceGeneration.setVisibility(FloatingActionButton.VISIBLE);
                        } catch (RuntimeException e) {
                            myToast.cancel();
                            myToast.makeText(this, "Not enough resources", Toast.LENGTH_SHORT).show();
                        }
                        fragmentResult = false;
                    }
                } else {
                    try {
                        gameManager.subtractResources(10d);
                        // increase effictiveness
                    } catch (RuntimeException e) {
                        myToast.cancel();
                        myToast.makeText(this, "Not enough resources", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                throw new RuntimeException("How did you even do this?");
        }

        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        gameManager.storeData(sharedPref);
    }

    public void imgTerminalOnClick(View view) {
        gameManager.addResources(1d);
    }                 //Click listener for the Terminal image

    public void fabAutoTapOnClick(View view) {
        autoTapCooldown = 11;
        fabAutoTap.setImageResource(android.R.color.transparent);
        fabAutoTap.setEnabled(false);
        txtAutoTap.setVisibility(TextView.VISIBLE);
        final Timer fabAutoTapActiveTimer = new Timer();
        // begin active timer
        fabAutoTapActiveTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (autoTapCooldown == 1) {
                    autoTapCooldown = 62;
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fabAutoTap.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                        }
                    });
                    fabAutoTapActiveTimer.cancel();
                    // begin cooldown timer
                    final Timer fabAutoTapCooldownTimer = new Timer();
                    fabAutoTapCooldownTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if (autoTapCooldown == 1) {
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        fabAutoTap.setImageResource(R.drawable.auto_tap);
                                        txtAutoTap.setVisibility(TextView.GONE);
                                        fabAutoTap.setEnabled(true);
                                        fabAutoTap.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_green_dark)));
                                    }
                                });
                                fabAutoTapCooldownTimer.cancel();
                            }
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txtAutoTap.setText(Integer.toString(autoTapCooldown));
                                }
                            });
                            autoTapCooldown--;
                        }
                    }, 0, 1000);
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtAutoTap.setText(Integer.toString(autoTapCooldown));
                    }
                });
                autoTapCooldown--;
            }
        }, 0, 1000);
    }

    public void fabResourceGenerationIncreaseOnClick(View view) {
        increaseResourceGenerationCooldown = 11;
        fabIncreaseResourceGeneration.setImageResource(android.R.color.transparent);
        fabIncreaseResourceGeneration.setEnabled(false);
        txtIncreaseResourceGeneration.setVisibility(TextView.VISIBLE);
        final Timer fabIncreaseResourceGenerationActiveTimer = new Timer();
        // begin active timer
        fabIncreaseResourceGenerationActiveTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (increaseResourceGenerationCooldown == 1) {
                    increaseResourceGenerationCooldown = 62;
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fabIncreaseResourceGeneration.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                        }
                    });
                    fabIncreaseResourceGenerationActiveTimer.cancel();
                    // begin cooldown timer
                    final Timer fabIncreaseResourceGenerationCooldownTimer = new Timer();
                    fabIncreaseResourceGenerationCooldownTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if (increaseResourceGenerationCooldown == 1) {
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        fabIncreaseResourceGeneration.setImageResource(android.R.drawable.ic_input_add);
                                        txtIncreaseResourceGeneration.setVisibility(TextView.GONE);
                                        fabIncreaseResourceGeneration.setEnabled(true);
                                        fabIncreaseResourceGeneration.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_green_dark)));
                                    }
                                });
                                fabIncreaseResourceGenerationCooldownTimer.cancel();
                            }
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txtIncreaseResourceGeneration.setText(Integer.toString(increaseResourceGenerationCooldown));
                                }
                            });
                            increaseResourceGenerationCooldown--;
                        }
                    }, 0, 1000);
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtIncreaseResourceGeneration.setText(Integer.toString(increaseResourceGenerationCooldown));
                    }
                });
                increaseResourceGenerationCooldown--;
            }
        }, 0, 1000);
    }

    private void displayUpgrades(int upgradeLevel){
        switch (upgradeLevel){
            case 5:
                myToast.cancel();
                myToast.makeText(this, "You unlocked something", Toast.LENGTH_SHORT).show();
                nextUpgradeDisplay = 60d;
                break;
            case 4:
                myToast.cancel();
                myToast.makeText(this, "You unlocked something", Toast.LENGTH_SHORT).show();
                nextUpgradeDisplay = 50d;
                break;
            case 3:
                navLeftTimeWarpASUpgrade.setVisible(true);
                nextUpgradeDisplay = 40d;
                break;
            case 2:
                navLeftResourceGenerationASUpgrade.setVisible(true);
                nextUpgradeDisplay = 30d;
                break;
            case 1:
                navLeftAutoClickASUpgrade.setVisible(true);
                navLeftNoUpgradesAvailable.setVisible(false);
                nextUpgradeDisplay = 20d;
                break;
            default:
                myToast.cancel();
                myToast.makeText(this, "WARNING: upgrade level has gone beyond table", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayUpgradesOnLoad(int upgradeLevel){
        // start from highest to lowest and go through entire list displaying anything lower
        switch (upgradeLevel){
            case 3:
                navLeftTimeWarpASUpgrade.setVisible(true);
            case 2:
                navLeftResourceGenerationASUpgrade.setVisible(true);
            case 1:
                navLeftNoUpgradesAvailable.setVisible(false);
                navLeftAutoClickASUpgrade.setVisible(true);
        }

        // set the amount of resources for next upgrade
        switch (upgradeLevel){
            case 0:
                nextUpgradeDisplay = 10d;
                break;
            case 1:
                nextUpgradeDisplay = 20d;
                break;
            case 2:
                nextUpgradeDisplay = 30d;
                break;
            default:
                myToast.cancel();
                myToast.makeText(this, "WARNING: upgrade level has gone beyond table", Toast.LENGTH_SHORT).show();
        }
    }

    public void fabTimeWarpOnClick(View view) {
    }
}
