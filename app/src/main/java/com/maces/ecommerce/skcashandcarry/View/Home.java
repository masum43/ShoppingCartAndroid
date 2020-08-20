package com.maces.ecommerce.skcashandcarry.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.maces.ecommerce.skcashandcarry.BuildConfig;
import com.maces.ecommerce.skcashandcarry.Converter;
import com.maces.ecommerce.skcashandcarry.Fragments.Change_Language;
import com.maces.ecommerce.skcashandcarry.Fragments.Logout;
import com.maces.ecommerce.skcashandcarry.Fragments.Order_History;
import com.maces.ecommerce.skcashandcarry.Fragments.Product_Home;
import com.maces.ecommerce.skcashandcarry.Fragments.Share;
import com.maces.ecommerce.skcashandcarry.Fragments.Update_Profile;
import com.maces.ecommerce.skcashandcarry.Fragments.update_password;
import com.maces.ecommerce.skcashandcarry.Model.Cart_Class;
import com.maces.ecommerce.skcashandcarry.R;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Home extends AppCompatActivity {
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawer;
    SharedPreferences sharedPreferences_Language;
    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.
    private ActionBarDrawerToggle drawerToggle;
    public static int cart_count = 0;
    public static String WHO = "";
    String language;
    Locale locale;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        navigationView = (NavigationView) findViewById(R.id.nvView);
        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.status));
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);
        WHO = Home.class.getSimpleName();
        sharedPreferences_Language = getSharedPreferences("Language", MODE_PRIVATE);
        // Set a Toolbar to replace the ActionBar.
        // This will display an Up icon (<-), we will replace it with hamburger later
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + getResources().getString(R.string.menu_home) + "</font>")));

        String Lang = sharedPreferences_Language.getString("Selected", "");
        {
            if (Lang.equals("en")) {
                setAppLocal("en");
            } else {
                setAppLocal("es");
            }
        }
        // Find our drawer view

        loadFragment(new Product_Home());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    Product_Home.lastPagReached = false;
                    loadFragment(new Product_Home());
                    WHO = "Home";
                    mDrawer.closeDrawers();
                } else if (id == R.id.nav_updateprofile) {
                    loadFragment(new Update_Profile());
                    WHO = "Update_Profile";
                    mDrawer.closeDrawers();
                } else if (id == R.id.nav_myorder) {
                    loadFragment(new Order_History());
                    WHO = "Order_History";
                    mDrawer.closeDrawers();
                } else if (id == R.id.nav_changeLanguage) {
                    loadFragment(new Change_Language());
                    WHO = "Change_Language";
                    mDrawer.closeDrawers();
                } else if (id == R.id.nav_share) {
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "SK Cash And Carry ");
                        String shareMessage = "\nLet me recommend you this application\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                    } catch (Exception e) {
                        //e.toString();
                    }
                    WHO = "Share";
                    mDrawer.closeDrawers();
                } else if (id == R.id.nav_logout) {
                    loadFragment(new Logout());
                    WHO = "Logout";
                    mDrawer.closeDrawers();
                } else if (id == R.id.nav_changepassword) {
                    loadFragment(new update_password());
                    WHO = "Update_Password";
                    mDrawer.closeDrawers();
                }
                return true;
            }
        });
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(Home.this, cart_count, R.drawable.ic_shopping_cart_white_24dp));
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.cart_action:
                if (cart_count < 1) {
                    Toast.makeText(this, "there is no item in cart", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(this, CartActivity.class));
                }
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;

    }


    @Override
    protected void onStart() {
        super.onStart();
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        if (Home.class.getSimpleName().equals(WHO)) {
            new AlertDialog.Builder(Home.this)
                    .setIcon(R.drawable.ic_error)
                    .setTitle("Close Application")
                    .setMessage("Are you sure you want to close application ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                                    "MyApp::MyWakelockTag");
                            wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
                            wakeLock.release();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            loadFragment(new Product_Home());
            WHO = "Home";
            Objects.requireNonNull(getSupportActionBar()).setTitle((Html.fromHtml("<font color=\"#000000\">" + getResources().getString(R.string.menu_home) + "</font>")));
            toolbar.setTitle((Html.fromHtml("<font color=\"#000000\">" + getResources().getString(R.string.menu_home) + "</font>")));
        }
    }

    private void setAppLocal(String Language) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(Language.toLowerCase()));
        resources.updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = sharedPreferences_Language.edit();
        editor.putString("Selected", Language);
        editor.apply();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    public void FinishFun() {
        this.finish();
    }
}