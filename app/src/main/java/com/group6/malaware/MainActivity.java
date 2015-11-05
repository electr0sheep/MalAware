package com.group6.malaware;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final int FPS = 30;
    public double RESOURCES_PER_SEC;
    public ResourceManager rManager = new ResourceManager();
    public VirusManager vManager = new VirusManager();
    public SharedPreferences sharedPref;
    public Timer gameLoop = new Timer();
    private Bundle bundle;
    private DialogFragment purchaseDialog;

    // View variables
    TextView txtResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        // set view variables
        txtResources = (TextView) findViewById(R.id.txt_resource);

        // load previous game
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        vManager.loadViruses(sharedPref);
        rManager.loadResources(sharedPref);

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
                    rManager.addResources(RESOURCES_PER_SEC);
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtResources.setText(rManager.getResourcesString());
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
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        bundle = new Bundle();
        purchaseDialog = new PurchaseDialogFragment();

        switch (id){
            case R.id.nav_left_no_upgrades:
                //Toast.makeText(this, "You clicked top item", Toast.LENGTH_SHORT).show();
                bundle.putString("Title", "You clicked top item");
                purchaseDialog.setArguments(bundle);
                purchaseDialog.show(getFragmentManager(), "No upgrades" );
                break;
            case R.id.nav_left_none_purchased:
                Toast.makeText(this, "You clicked bottom item", Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new RuntimeException("How did you even do this?");
        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void imgTerminalOnClick(View view) {
        rManager.addResources(1);
    }
}
