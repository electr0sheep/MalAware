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
    MenuItem navLeftBootInfector;
    MenuItem navLeftPolymorphicMalware;
    MenuItem navLeftFourKMalware;
    MenuItem navLeftCodeRedMalware;
    MenuItem navLeftRegrowingVirus;
    MenuItem navLeftBotNet;
    MenuItem navLeftZombieVirus;
    MenuItem navLeftSundayVirus;
    MenuItem navLeftAllPurposeWorm;
    MenuItem navLeftZmistVirus;
    MenuItem navLeftMilitaryMalware;
    MenuItem navLeftSuperTrojan;
    MenuItem navLeftTyrannicalAdware;
    MenuItem navLeftRansomVirus;
    MenuItem navLeftILOVEUVirus;



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
    private int stepCount = 0;      //State information variable to allow for traversal through tutorial
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
        navLeftAdware = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_adware);
        navLeftMalware = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_malware);
        navLeftWorm = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_worm);
        navLeftTrojan = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_trojan);
        navLeftRootkit = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_rootkit);
        navLeftHijacker = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_hijacker);
        navLeftBootInfector = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_boot_infector);
        navLeftPolymorphicMalware = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_polymorphic_malware);
        navLeftFourKMalware = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_fourk_malware);
        navLeftCodeRedMalware = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_code_red_malware);
        navLeftRegrowingVirus = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_regrowing_virus);
        navLeftBotNet = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_bot_net);
        navLeftZombieVirus = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_zombie_virus);
        navLeftSundayVirus = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_sunday_virus);
        navLeftAllPurposeWorm = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_all_purpose_worm);
        navLeftZmistVirus = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_zmist_virus);
        navLeftMilitaryMalware = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_military_malware);
        navLeftSuperTrojan = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_super_trojan);
        navLeftTyrannicalAdware = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_tyrannical_adware);
        navLeftRansomVirus = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_ransom_virus);
        navLeftILOVEUVirus = this.navigationViewLeft.getMenu().findItem(R.id.nav_left_iloveu_virus);


        // these are the floating action buttons
        fabAutoTap = (FloatingActionButton) findViewById(R.id.fab_action_skill_auto_tap);
        fabIncreaseResourceGeneration = (FloatingActionButton) findViewById(R.id.fab_action_skill_increase_generation);
        fabTimeWarp = (FloatingActionButton) findViewById(R.id.fab_action_skill_time_warp);
        // this is the toast
        myToast = new Toast(this);

        // set up action skills
        gameManager.coreAutoTap = new AutoTap(MainActivity.this, gameManager, sharedPref, fabAutoTap, R.drawable.auto_tap, txtAutoTap);
        gameManager.coreIncreaseResource = new IncreaseResource(MainActivity.this, gameManager, sharedPref, fabIncreaseResourceGeneration, R.drawable.plus, txtIncreaseResourceGeneration);
        gameManager.coreTimeWarp = new TimeWarp(MainActivity.this, gameManager, sharedPref, fabTimeWarp, R.drawable.time_warp, txtTimeWarp);

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

	//Creates the list of generators for the UI
    private void createExpList() {
        groupList = new ArrayList<String>();
        groupList.add("Adware");
        groupList.add("Malware");
        groupList.add("Worm");
        groupList.add("Trojan");
        groupList.add("Rootkit");
        groupList.add("Hijacker");
        groupList.add("Boot Infector");
        groupList.add("Polymorphic Malware");
        groupList.add("4K Malware");
        groupList.add("Code Red Malware");
        groupList.add("Regrowing Virus");
        groupList.add("Bot Net");
        groupList.add("Zombie Virus");
        groupList.add("Sunday Virus");
        groupList.add("All-Purpose Worm");
        groupList.add("Zmist Virus");
        groupList.add("Military Malware");
        groupList.add("Super Trojan");
        groupList.add("Tyrannical Adware");
        groupList.add("Ransom Virus");
        groupList.add("ILOVEU Virus");


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
	// This try statement checks to see what the user selected from the list of upgrades
	//  then displays information about the upgrade and cost
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
                                "\nUpgraded time warped ahead: " + (5 + gameManager.coreTimeWarp.getUpgradeLevel()) + " minutes");
                    }
                    break;
				case R.id.nav_left_adware:
                    bundle.putString("Title", "Adware");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 1750% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.coreAdware.getModifierLevel() * 17.5) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.coreAdware.getModifierLevel()+1)*17.5) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.coreAdware.getCostOfUpgradeModifier()));
                    break;
                case R.id.nav_left_malware:
                    bundle.putString("Title", "Malware");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 1250% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.coreMalware.getModifierLevel() * 12.5) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.coreMalware.getModifierLevel()+1)*12.5) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.coreMalware.getCostOfUpgradeModifier()));
                    break;
                case R.id.nav_left_worm:
                    bundle.putString("Title", "Worm");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 1000% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.coreWorm.getModifierLevel() * 10) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.coreWorm.getModifierLevel()+1)*10) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.coreWorm.getCostOfUpgradeModifier()));
                    break;
                case R.id.nav_left_trojan:
                    bundle.putString("Title", "Trojan");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 750% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.coreTrojan.getModifierLevel() * 7.5) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.coreTrojan.getModifierLevel()+1)*7.5) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.coreTrojan.getCostOfUpgradeModifier()));
                    break;
                case R.id.nav_left_rootkit:
                    bundle.putString("Title", "Rootkit");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 500% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.coreRootkit.getModifierLevel() * 5) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.coreRootkit.getModifierLevel()+1)*5) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.coreRootkit.getCostOfUpgradeModifier()));
                    break;
                case R.id.nav_left_hijacker:
                    bundle.putString("Title", "Hijacker");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 300% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.coreHijacker.getModifierLevel()*3) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.coreHijacker.getModifierLevel()+1)*3) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.coreHijacker.getCostOfUpgradeModifier()));
                    break;
                case R.id.nav_left_boot_infector:
                    bundle.putString("Title", "Boot Infector");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 200% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.coreBootInfector.getModifierLevel()*2) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.coreBootInfector.getModifierLevel()+1)*2) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.coreBootInfector.getCostOfUpgradeModifier()));
                    break;
                case R.id.nav_left_polymorphic_malware:
                    bundle.putString("Title", "Polymorphic Malware");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 200% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.corePolymorphicMalware.getModifierLevel()*2) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.corePolymorphicMalware.getModifierLevel()+1)*2) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.corePolymorphicMalware.getCostOfUpgradeModifier()));
                    break;
                case R.id.nav_left_fourk_malware:
                    bundle.putString("Title", "4K Malware");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 100% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.coreFourKMalware.getModifierLevel()) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.coreFourKMalware.getModifierLevel()+1)) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.coreFourKMalware.getCostOfUpgradeModifier()));
                    break;
                case R.id.nav_left_code_red_malware:
                    bundle.putString("Title", "Code Red Malware");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 100% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.coreCodeRedMalware.getModifierLevel()) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.coreCodeRedMalware.getModifierLevel()+1)) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.coreCodeRedMalware.getCostOfUpgradeModifier()));
                    break;
                case R.id.nav_left_regrowing_virus:
                    bundle.putString("Title", "Regrowing Virus");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 100% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.coreRegrowingVirus.getModifierLevel()) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.coreRegrowingVirus.getModifierLevel()+1)) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.coreRegrowingVirus.getCostOfUpgradeModifier()));
                    break;
                case R.id.nav_left_bot_net:
                    bundle.putString("Title", "Bot Net");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 75% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.coreBotNet.getModifierLevel() * .75) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.coreBotNet.getModifierLevel()+1)*.75) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.coreBotNet.getCostOfUpgradeModifier()));
                    break;
                case R.id.nav_left_zombie_virus:
                    bundle.putString("Title", "Zombie Virus");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 50% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.coreZombieVirus.getModifierLevel() * .5) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.coreZombieVirus.getModifierLevel()+1)*.5) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.coreZombieVirus.getCostOfUpgradeModifier()));
                    break;
                case R.id.nav_left_sunday_virus:
                    bundle.putString("Title", "Sunday Virus");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 50% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.coreSundayVirus.getModifierLevel() * .5) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.coreSundayVirus.getModifierLevel()+1)*.5) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.coreSundayVirus.getCostOfUpgradeModifier()));
                    break;
                case R.id.nav_left_all_purpose_worm:
                    bundle.putString("Title", "All-Purpose Worm");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 40% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.coreAllPurposeWorm.getModifierLevel() * .40) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.coreAllPurposeWorm.getModifierLevel()+1)*.40) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.coreAllPurposeWorm.getCostOfUpgradeModifier()));
                    break;
                case R.id.nav_left_zmist_virus:
                    bundle.putString("Title", "Zmist Virus");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 40% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.coreZmistVirus.getModifierLevel()*.4) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.coreZmistVirus.getModifierLevel()+1)*.4) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.coreZmistVirus.getCostOfUpgradeModifier()));
                    break;
                case R.id.nav_left_military_malware:
                    bundle.putString("Title", "Military Malware");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 30% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.coreMilitaryMalware.getModifierLevel()*.30) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.coreMilitaryMalware.getModifierLevel()+1)*.30) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.coreMilitaryMalware.getCostOfUpgradeModifier()));
                    break;
                case R.id.nav_left_super_trojan:
                    bundle.putString("Title", "Super Trojan");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 30% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.coreSuperTrojan.getModifierLevel()*.30) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.coreSuperTrojan.getModifierLevel()+1)*.30) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.coreSuperTrojan.getCostOfUpgradeModifier()));
                    break;
                case R.id.nav_left_tyrannical_adware:
                    bundle.putString("Title", "Tyrannical Adware");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 30% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.coreTyrannicalAdware.getModifierLevel()*.30) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.coreTyrannicalAdware.getModifierLevel()+1)*.30) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.coreTyrannicalAdware.getCostOfUpgradeModifier()));
                    break;
                case R.id.nav_left_ransom_virus:
                    bundle.putString("Title", "Ransom Virus");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 30% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.coreRansomVirus.getModifierLevel()*.30) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.coreRansomVirus.getModifierLevel()+1)*.30) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.coreRansomVirus.getCostOfUpgradeModifier()));
                    break;
                case R.id.nav_left_iloveu_virus:
                    bundle.putString("Title", "ILOVEU Virus");
                    bundle.putString("Description", "Improves the rate at which this generator produces viruses by 30% per upgrade." +
                            "\n\nCurrent Bonus Multiplier: " + (gameManager.coreILOVEUVirus.getModifierLevel()*.30) +
                            "\nUpgraded Bonus Multiplier : " +((gameManager.coreILOVEUVirus.getModifierLevel()+1)*.30) +
                            "\nCost: " + gameManager.convertNumToString(gameManager.coreILOVEUVirus.getCostOfUpgradeModifier()));
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

        if (tutorialActive) {
            stepCount = 1;
            tutorialStep(stepCount);
        }
    }

    public void drawerBtnLeftOnClick(View view) {
        dLayout.openDrawer(GravityCompat.START);

        if (tutorialActive) {
            stepCount = 3;
            tutorialStep(stepCount);
        }
    }

    public void drawerBtnRightOnClick(View view) {
        dLayout.openDrawer(GravityCompat.END);

        if (tutorialActive) {
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
        if (requestCode == PICK_OPTIONS_CODE)
            if (resultCode == RESULT_OK) {
                if (data.getExtras().getBoolean("Reset Stats")) {
                    gameManager.resetData();
                    gameManager.resetLevelInc();
                } else if (data.getExtras().getBoolean("Start Tutorial")) {
                    Log.i("Info", "Starting Tutorial");
                    dLayout.closeDrawer(GravityCompat.START);
                    tutorialStep(stepCount);
                }
            }
    }

    /*
        Tutorial Overlay was found at: https://github.com/worker8/TourGuide
        Edited and setup by: Cyril Mathew
     */

    private void tutorialStep(int step) {
        tutorialActive = true;
        switch (step) {
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
