package com.group6.malaware;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final int FPS = 30;
    public double RESOURCES_PER_FRAME = 0;
    public ResourceManager rManager = new ResourceManager();
    public VirusManager vManager = new VirusManager();
    public GameManager gameManager = new GameManager();
    public SharedPreferences sharedPref;
    public Timer gameLoop = new Timer();
    private Bundle bundle;
    private DialogFragment purchaseDialog;
    private DialogFragment upgradeDialog;

    // View variables
    TextView txtResources;
    NavigationView navLeft;
    NavigationView navRight;
    MenuItem navLeftNoUpgradesPurchased;
    MenuItem navLeftNoUpgradesAvailable;
    MenuItem navLeftAutoClickUpgrade;
    MenuItem navLeftResourceGenerationUpgrade;
    MenuItem navLeftAutoClickUpgradePurchased;
    MenuItem navLeftResourceGenerationUpgradePurchased;
    MenuItem navRightNoGeneratorsAvailable;
    FloatingActionButton fabAutoClick;
    FloatingActionButton fabIncreaseResourceGeneration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        // set view variables
        txtResources = (TextView) findViewById(R.id.txt_resource);
        navLeft = (NavigationView) findViewById(R.id.nav_view_left);
        navRight = (NavigationView) findViewById(R.id.nav_view_right);
        navLeftNoUpgradesPurchased = navLeft.getMenu().findItem(R.id.nav_left_no_upgrades_purchased);
        navLeftNoUpgradesAvailable = navLeft.getMenu().findItem(R.id.nav_left_no_upgrades_available);
        navLeftAutoClickUpgrade = navLeft.getMenu().findItem(R.id.nav_left_auto_click_upgrade);
        navLeftAutoClickUpgradePurchased = navLeft.getMenu().findItem(R.id.nav_left_auto_click_upgrade_purchased);
        navLeftResourceGenerationUpgrade = navLeft.getMenu().findItem(R.id.nav_left_resource_generation_increase);
        navLeftResourceGenerationUpgradePurchased = navLeft.getMenu().findItem(R.id.nav_left_resource_generation_increase_purchased);
        navRightNoGeneratorsAvailable = navRight.getMenu().findItem(R.id.nav_right_no_generators_available);
        fabAutoClick = (FloatingActionButton) findViewById(R.id.fab_action_skill_auto_tap);
        fabIncreaseResourceGeneration = (FloatingActionButton) findViewById(R.id.fab_action_skill_increase_generation);

        // load previous game
        /*sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        vManager.loadViruses(sharedPref);
        rManager.loadResources(sharedPref);*/

        // reset resources
        //rManager.resetResources(sharedPref);

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        //        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        //toggle.syncState();

        NavigationView navigationViewLeft = (NavigationView) findViewById(R.id.nav_view_left);
        NavigationView navigationViewRight = (NavigationView) findViewById(R.id.nav_view_right);
        navigationViewLeft.setNavigationItemSelectedListener(this);
        navigationViewRight.setNavigationItemSelectedListener(this);

        // Initialize game loop
        gameLoop = new Timer();

        gameLoop.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (sharedPref != null) {
                    //rManager.addResources(RESOURCES_PER_SEC);
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //gameManager.getTotalResourcesPerFrame(FPS);
                        gameManager.calcTotalResourcesPerSec();
                        txtResources.setText(gameManager.getResourcesString());
                        if (gameManager.getTotalResources() > 9 && !navLeftAutoClickUpgradePurchased.isVisible()){
                            if (navLeftNoUpgradesAvailable.isVisible()){
                                navLeftNoUpgradesAvailable.setVisible(false);
                            }
                            navLeftAutoClickUpgrade.setVisible(true);
                        }
                        if (gameManager.getTotalResources() > 19  && !navLeftResourceGenerationUpgradePurchased.isVisible()){
                            if (navLeftNoUpgradesAvailable.isVisible()){
                                navLeftNoUpgradesAvailable.setVisible(false);
                            }
                            navLeftResourceGenerationUpgrade.setVisible(true);
                            //navLeftNoUpgradesAvailable.setVisible(true);
                            //navLeftNoUpgradesPurchased.setVisible(false);
                            //navLeftNoUpgradesAvailable.setVisible(true);
                            //navLeftNoUpgradesAvailable.setVisible(false);
                        }
                        //if (gameManager.getTotalResources() > 29){
                            //navRightNoGeneratorsAvailable.setVisible(false);
                        //}
                        /*
                        txtThousandsModifier.setText(rManager.getThousandsModifier());
                        if (rManager.getResourcesDouble() > 9 && imgMalware != null) {
                            imgMalware.setVisibility(ImageView.VISIBLE);
                        }
                        if (rManager.getResourcesDouble() > 19 && imgWorm != null) {
                            imgWorm.setVisibility(ImageView.VISIBLE);
                        }
                        if (rManager.getResourcesDouble() > 29 && imgAdware != null) {
                            imgAdware.setVisibility(ImageView.VISIBLE);
                        }
                        if (rManager.getResourcesDouble() > 39 && imgHijacker != null) {
                            imgHijacker.setVisibility(ImageView.VISIBLE);
                        }*/
                    }
                });
            }
        }, 0, 1000 / FPS);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)){
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
        purchaseDialog = new PurchaseDialogFragment();
        upgradeDialog = new UpgradeDialogFragment();

        switch (id){
            case R.id.nav_left_no_upgrades_available:
                bundle.putString("Title", "You clicked top item");
                upgradeDialog.setArguments(bundle);
                upgradeDialog.show(getFragmentManager(), "No upgrades" );
                break;
            case R.id.nav_left_no_upgrades_purchased:
                Toast.makeText(this, "You clicked bottom item", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_right_no_generators_available:
                /*bundle.putString("Title", "You clicked top item");
                purchaseDialog.setArguments(bundle);
                purchaseDialog.show(getFragmentManager(), "No purchases" );*/
                bundle.putString("Title", "Buying Adware");
                bundle.putString("Cost", Integer.toString(gameManager.coreAdware.getCost()));
                bundle.putString("Count", Integer.toString(gameManager.coreAdware.getNumOfGenerators()));
                bundle.putInt("Type", 0);
                purchaseDialog.setArguments(bundle);
                purchaseDialog.show(getFragmentManager(), "Adware");
                break;
            case R.id.nav_left_auto_click_upgrade:
                if (gameManager.subtractResources(10)){
                    navLeftAutoClickUpgradePurchased.setVisible(true);
                    navLeftNoUpgradesPurchased.setVisible(false);
                    navLeftAutoClickUpgrade.setVisible(false);
                    fabAutoClick.setVisibility(FloatingActionButton.VISIBLE);
                    if (!navLeftResourceGenerationUpgrade.isVisible()){
                        navLeftNoUpgradesAvailable.setVisible(true);
                    }
                } else {
                    Toast.makeText(this, "Not enough resources", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_left_resource_generation_increase:
                if (gameManager.subtractResources(10)){
                    navLeftResourceGenerationUpgradePurchased.setVisible(true);
                    navLeftResourceGenerationUpgrade.setVisible(false);
                    fabIncreaseResourceGeneration.setVisibility(FloatingActionButton.VISIBLE);
                    if (!navLeftAutoClickUpgrade.isVisible()){
                        navLeftNoUpgradesAvailable.setVisible(true);
                    }
                } else {
                    Toast.makeText(this, "Not enough resources", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                throw new RuntimeException("How did you even do this?");
        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void imgTerminalOnClick(View view) {
        gameManager.addVirus(6, 1);
    }

    public void addVirus(int type, int amount)
    {
        gameManager.attemptBuy(type, amount);
    }

    public void fabAutoTapOnClick(View view) {
        Toast.makeText(this, "Auto tap engage!", Toast.LENGTH_SHORT).show();
    }

    public void fabResourceGenerationIncrease(View view) {
        Toast.makeText(this, "Go go resource generation increase!", Toast.LENGTH_SHORT).show();
    }
}
