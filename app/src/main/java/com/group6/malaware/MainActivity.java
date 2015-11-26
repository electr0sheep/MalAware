package com.group6.malaware;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final int FPS = 30;                                          //FPS constant
    public final int AUTO_TAP_ACTIVE_CD = 10;                           //auto tap active period
    public final int AUTO_TAP_CD = 60;                                  //auto tap cd period
    public final int INCREASE_RESOURCE_GEN_CD = 60;                     //increase resource gen period
    public final int TIME_WARP_CD = 60;                                 //time warp action skill cd

    // these are only here because they are the only way we
    //  know how to use an int variable within a TimerTask
    private int autoTapCooldown = 0;
    private int increaseResourceGenerationCooldown = 0;
    private int timeWarpCooldown = 0;
    private double inactiveResources;
    public GameManager gameManager;
    public SharedPreferences sharedPref;
    public Timer gameLoop;
    private Bundle bundle;                                              //Bundle used to pass data between dialogs
    private DialogFragment upgradeDialog;
    public Toast myToast;

    // View variables
    // text views up top
    TextView txtResources;
    TextView txtGenRate;

    // text views over fabs
    TextView txtAutoTap;
    TextView txtIncreaseResourceGeneration;
    NavigationView navigationViewLeft;
    TextView txtTimeWarp;

    // menu items
    MenuItem navLeftNoUpgradesAvailable;
    MenuItem navLeftAutoClickASUpgrade;
    MenuItem navLeftResourceGenerationASUpgrade;
    MenuItem navLeftTimeWarpASUpgrade;
    MenuItem navLeftAdware;
    MenuItem navLeftMalware;
    MenuItem navLeftWorm;
    MenuItem navLeftTrojan;
    MenuItem navLeftRootkit;
    MenuItem navLeftHijacker;

    // floating action buttons
    FloatingActionButton fabAutoTap;
    FloatingActionButton fabIncreaseResourceGeneration;
    FloatingActionButton fabTimeWarp;
    FloatingActionButton fabPowerUpAS;

    //tutorial boolean
    private boolean tutorialActive = false;

    //UI Elements
    ImageView terminal;
    ImageButton btn_leftDrawer;
    ImageButton btn_rightDrawer;
    TourGuide mTourGuideHandler;
    private int stepCount = 0;


    private DrawerLayout dLayout;
    private List<String> groupList;
    private List<String> childList;
    private Map<String, List<String>> childCollection;
    public ExpandableListView expListView;

    final static int PICK_OPTIONS_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set preferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        gameManager = new GameManager(sharedPref);

        // set view variables
        txtResources = (TextView) findViewById(R.id.txt_resource);
        txtGenRate = (TextView) findViewById(R.id.txt_totalGenRate);
        // these text views are for the cooldown of the floating action buttons
        txtAutoTap = (TextView) findViewById(R.id.txt_action_skill_auto_tap);
        txtIncreaseResourceGeneration = (TextView) findViewById(R.id.txt_action_skill_increase_generation);
        txtTimeWarp = (TextView) findViewById(R.id.txt_fab_action_skill_time_warp);
        // this is the menu
        //navigationViewLeft = (NavigationView) findViewById(R.id.nav_view_left);
        navigationViewLeft = (NavigationView) findViewById(R.id.nav_view_left);
        //navLeftNoUpgradesAvailable = navigationViewLeft.getMenu().findItem(R.id.nav_left_no_upgrades_available);
        fabAutoTap = (FloatingActionButton) findViewById(R.id.fab_action_skill_auto_tap);
        fabIncreaseResourceGeneration = (FloatingActionButton) findViewById(R.id.fab_action_skill_increase_generation);

        //set UI Elements
        terminal = (ImageView) findViewById(R.id.img_terminal);
        btn_leftDrawer = (ImageButton) findViewById(R.id.btn_drawer_left);
        btn_rightDrawer = (ImageButton) findViewById(R.id.btn_drawer_right);

        // these are the items in the menu
        //navLeftNoUpgradesAvailable = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_no_upgrades_available);
        navLeftAutoClickASUpgrade = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_auto_click_action_skill_upgrade);
        navLeftResourceGenerationASUpgrade = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_resource_generation_increase_action_skill_upgrade);
        navLeftTimeWarpASUpgrade = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_time_warp_action_skill_upgrade);
        /*navLeftAdware = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_adware);
        navLeftMalware = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_malware);
        navLeftWorm = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_worm);
        navLeftTrojan = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_trojan);
        navLeftRootkit = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_rootkit);
        navLeftHijacker = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_hijacker);*/

        // these are the floating action buttons
        fabAutoTap = (FloatingActionButton) findViewById(R.id.fab_action_skill_auto_tap);
        fabIncreaseResourceGeneration = (FloatingActionButton) findViewById(R.id.fab_action_skill_increase_generation);
        fabTimeWarp = (FloatingActionButton) findViewById(R.id.fab_action_skill_time_warp);
        // this is the toast
        myToast = new Toast(this);

        // set up action skills
        gameManager.coreAutoTap = new AutoTap(MainActivity.this, gameManager, sharedPref, fabAutoTap, txtAutoTap);
        gameManager.coreIncreaseResource = new IncreaseResource(MainActivity.this, gameManager, sharedPref, fabIncreaseResourceGeneration, txtIncreaseResourceGeneration);
        gameManager.coreTimeWarp = new TimeWarp(MainActivity.this, gameManager, sharedPref, fabTimeWarp, txtTimeWarp);

        // display applicable fabs
        displayFABsOnLoad();

        // add appropriate resources if applicable
        inactiveResources = addResourcesForTime(System.currentTimeMillis()
                - gameManager.getStoredTime(sharedPref));

        // display this number, if it is significant
        if (inactiveResources > 1) {
            Toast.makeText(this, "You have generated " + gameManager.convertNumToString(inactiveResources)
                    + " resources while you were away!", Toast.LENGTH_SHORT).show();
        }

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
                        txtGenRate.setText(gameManager.totalGenRateString());
                    }
                });
            }
        }, 0, 1000 / FPS);

        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        createExpList();

        expListView = (ExpandableListView) findViewById(R.id.right_drawer);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(this, groupList, childCollection);
        expListView.setAdapter(expListAdapter);

        View header = (View) getLayoutInflater().inflate(R.layout.nav_header_right, null);
        expListView.addHeaderView(header);
    }

    @Override
    protected void onStop() {
        super.onStop();
        gameManager.end();
    }

    private void createExpList() {
        groupList = new ArrayList<String>();
        groupList.add("Adware");
        groupList.add("Malware");
        groupList.add("Worm");
        groupList.add("Trojan");
        groupList.add("Rootkit");
        groupList.add("Hijacker");

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
        if (dLayout.isDrawerOpen(GravityCompat.START)) {
            dLayout.closeDrawer(GravityCompat.START);
        } else if (dLayout.isDrawerOpen(GravityCompat.END)) {
            dLayout.closeDrawer(GravityCompat.END);
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

        try {
            switch (id) {
                case R.id.nav_left_auto_click_action_skill_upgrade:
                    bundle.putString("Title", gameManager.coreAutoTap.getName());
                    if (!gameManager.coreAutoTap.purchased()) {
                        bundle.putString("Description", gameManager.coreAutoTap.getPurchaseMessage() + "\n\n" +
                                "Cost: " + gameManager.coreAutoTap.getUpgradeCost());
                    } else {
                        bundle.putString("Description", gameManager.coreAutoTap.getUpgradeMessage() + "\n\n" +
                                "Cost: " + gameManager.coreAutoTap.getUpgradeCost() +
                                "\nCurrent Duration: " + (10 + (5 * (gameManager.coreAutoTap.getUpgradeLevel() - 1))) + " seconds" +
                                "\nUpgraded Duration: " + (10 + (5 * gameManager.coreAutoTap.getUpgradeLevel())) + " seconds");
                    }
                    break;
                case R.id.nav_left_resource_generation_increase_action_skill_upgrade:
                    bundle.putString("Title", gameManager.coreIncreaseResource.getName());
                    if (!gameManager.coreIncreaseResource.purchased()) {
                        bundle.putString("Description", gameManager.coreIncreaseResource.getPurchaseMessage() + "\n\n" +
                                "Cost: " + gameManager.coreIncreaseResource.getUpgradeCost());
                    } else {
                        bundle.putString("Description", gameManager.coreIncreaseResource.getUpgradeMessage() + "\n\n " +
                                "Cost: " + gameManager.coreAutoTap.getUpgradeCost() +
                                "\nCurrent Modifier: " + (1 + (gameManager.coreIncreaseResource.getUpgradeLevel() - 1) / 2d) +
                                "\nUpgraded Modifier: " + (1 + gameManager.coreIncreaseResource.getUpgradeLevel() / 2d));
                    }
                    break;
                case R.id.nav_left_time_warp_action_skill_upgrade:
                    bundle.putString("Title", gameManager.coreTimeWarp.getName());
                    if (!gameManager.coreTimeWarp.purchased()) {
                        bundle.putString("Description", gameManager.coreTimeWarp.getPurchaseMessage() + "\n\n" +
                                "Cost: " + gameManager.coreTimeWarp.getUpgradeCost());
                    } else {
                        bundle.putString("Description", "Each upgrade to time warp will provide an additional minute " +
                                "worth of resources\n\n" +
                                "Cost: " + gameManager.coreTimeWarp.getUpgradeCost() +
                                "\nCurrent time warped ahead: " + (4 + gameManager.coreTimeWarp.getUpgradeLevel()) + " minutes" +
                                "\nUpgraded time warped ahead: " + ( 5 + gameManager.coreTimeWarp.getUpgradeLevel()) + " minutes");
                    }
                    break;
                default:
                    throw new RuntimeException("How did you even do this?");
            }
        } catch (RuntimeException e) {
            myToast.cancel();
            myToast = Toast.makeText(this, "Not enough resources", Toast.LENGTH_SHORT);
            myToast.show();
        }

        upgradeDialog.setArguments(bundle);
        upgradeDialog.show(getFragmentManager(), "Blah");

        return true;
    }

    public void imgTerminalOnClick(View view) {
        //Click listener for the Terminal image
        gameManager.addResources(1d);

        if(tutorialActive)
        {
            stepCount = 1;
            tutorialStep(stepCount);
        }
    }

    public void drawerBtnLeftOnClick(View view) {
        dLayout.openDrawer(GravityCompat.START);

        if(tutorialActive)
        {
            stepCount = 3;
            tutorialStep(stepCount);
        }
    }

    public void drawerBtnRightOnClick(View view) {
        dLayout.openDrawer(GravityCompat.END);

        if(tutorialActive)
        {
            stepCount = 2;
            tutorialStep(stepCount);
        }
    }

    public void fabAutoTapOnClick(View view) {
        gameManager.coreAutoTap.activate();
    }

    public void fabResourceGenerationIncreaseOnClick(View view) {
        gameManager.coreIncreaseResource.activate();
    }

    public void fabTimeWarpOnClick(View view) {
        gameManager.coreTimeWarp.activate();
    }

    private void displayFABsOnLoad() {
        if (gameManager.coreAutoTap.purchased()) {
            fabAutoTap.setVisibility(FloatingActionButton.VISIBLE);
        }

        if (gameManager.coreIncreaseResource.purchased()) {
            fabIncreaseResourceGeneration.setVisibility(FloatingActionButton.VISIBLE);
        }

        if (gameManager.coreTimeWarp.purchased()) {
            fabTimeWarp.setVisibility(FloatingActionButton.VISIBLE);
        }
    }

    private double addResourcesForTime(double timeInMillis) {
        double resources = 0;
        if (gameManager.getResourcesPerSec() > 0) {
            resources = timeInMillis                        // milliseconds that we want to add for
                    / 1000                                  // number of milliseconds per sec
                    * gameManager.getResourcesPerSec();     // number of resources per sec
            gameManager.addResources(resources);            // add that amount to resource pool
        }
        return resources;
    }

    public void onOptionsClick(View view) {
        Intent optionsIntent = new Intent(this, OptionsActivity.class);
        optionsIntent.putExtra("Reset Level", gameManager.getResetLevel());
        startActivityForResult(optionsIntent, PICK_OPTIONS_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_OPTIONS_CODE)
            if(resultCode == RESULT_OK) {
                if (data.getExtras().getBoolean("Reset Stats")) {
                    gameManager.resetData();
                    gameManager.resetLevelInc();
                }
                else if(data.getExtras().getBoolean("Start Tutorial"))
                {
                    Log.i("Info", "Starting Tutorial");
                    dLayout.closeDrawer(GravityCompat.START);
                    tutorialStep(stepCount);
                }
            }
    }

    private void tutorialStep(int step)
    {
        tutorialActive = true;
        switch(step)
        {
            case 0:
                mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click);
                mTourGuideHandler.setPointer(new Pointer())
                        .setToolTip(new ToolTip().setTitle("Welcome!").setDescription("Tap the terminal to generate a resource"))
                        .setOverlay(new Overlay())
                        .playOn(terminal);
                break;
            case 1:
                mTourGuideHandler.cleanUp();
                mTourGuideHandler.setToolTip(new ToolTip().setTitle("Generate Resources").setDescription("Tap this button to open the Generator drawer or swipe left").setGravity(Gravity.TOP | Gravity.LEFT))
                        .setOverlay(new Overlay())
                        .playOn(btn_rightDrawer);
                break;
            case 2:
                mTourGuideHandler.cleanUp();
                mTourGuideHandler.setToolTip(new ToolTip().setTitle("Purchase Action Skills").setDescription("Tap this button to open the Upgrades drawer or swipe right").setGravity(Gravity.TOP | Gravity.RIGHT))
                        .setOverlay(new Overlay())
                        .playOn(btn_leftDrawer);
                break;
            case 3:
                mTourGuideHandler.cleanUp();
                tutorialActive = false;
                stepCount = 0;
                break;
        }
    }
}