package apping.trashsoundboard;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;


public class MyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<String> autori;
    ArrayList<String> sfondi;


    String[] menutitles;
    String[] menuimages;
    XMLParser xml;
    ListView listNav;
    LinearLayout action_home;
    LinearLayout action_settings;
    LinearLayout action_pref;




    private CharSequence mTitle;
    CustomActionBar customActionBar;

    public InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        xml = new XMLParser(this.getApplicationContext());

        ArrayList<String> autori = xml.selectAllAutore();
        ArrayList<String> sfondi = xml.selectAllSfondi();
        menutitles = new String[autori.size()];
        menuimages = new String[sfondi.size()];
        menutitles = autori.toArray(menutitles);
        menuimages = sfondi.toArray(menuimages);

        //customActionBar = new CustomActionBar(getSupportActionBar(), this, getFragmentManager());
        //mTitle = customActionBar.getTitle();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        listNav = (ListView) findViewById(R.id.list_navigation);
        NavigationView navigationView = findViewById(R.id.nav_view);

        action_home = findViewById(R.id.action_home);
        action_settings = findViewById(R.id.action_settings);
        action_pref = findViewById(R.id.action_pref);


        //Admob
        MobileAds.initialize(this,
                getResources().getString(R.string.app_id));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.ads_insterstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        action_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MyActivity.this, ActivitySettings.class));
            }
        });

        action_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);

                getFragmentManager().beginTransaction()
                        .replace(R.id.container, newInstancePrima(autori))
                        .commit();
            }
        });

        action_pref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);

                getFragmentManager().beginTransaction()
                        .replace(R.id.container, newInstancePreferiti(autori))
                        .commit();
            }
        });


        listNav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateDisplay(position);
            }

            private void updateDisplay(int position) {


                Fragment fragment = new Pagina();

                Bundle bundle = new Bundle();
                bundle.putString("autore", menutitles[position]);
                // set Fragmentclass Arguments

                fragment.setArguments(bundle);

                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    // error in creating fragment
                    Log.e("MainActivity", "Error in creating fragment");
                }

                SharedPreferences settings = getSharedPreferences("TrashSoundBoard", 0);

                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("adcounter", settings.getInt("adcounter", 0) + 1);

                if (settings.getInt("adcounter", 0)>2){
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                    editor.putInt("adcounter", 0);
                    Log.d("ADCOUNTER", "contatore = 3");
                    Log.d("ADCOUNTER", settings.getInt("adcounter", 0)+"");

                }

                editor.commit();

                Log.d("ADCOUNTER", settings.getInt("adcounter", 0)+"");
            }

        });

        AdapterMainMenu adapter = new AdapterMainMenu(this, menutitles, menuimages);
        listNav.setAdapter(adapter);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, newInstancePrima(autori))
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        switch(id)
        {
            case R.id.nav_impostazioni:
                startActivity(new Intent(MyActivity.this, ActivitySettings.class));
        }
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            Fragment currentFragment = getFragmentManager().findFragmentById(R.id.container);

            if (currentFragment instanceof Pagina) {

                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, newInstancePrima(autori))
                        .commit();

            } else if (currentFragment instanceof Preferiti) {

                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, newInstancePrima(autori))
                        .commit();

            } else {
                super.onBackPressed();

            }
        }
    }

    public static Preferiti newInstancePreferiti(ArrayList<String> autori) {
        Preferiti fragment = new Preferiti();
        Bundle args = new Bundle();
        args.putStringArrayList("autori", autori);
        fragment.setArguments(args);
        return fragment;
    }

    public static Prima newInstancePrima(ArrayList<String> autori) {
        Prima fragment = new Prima();
        Bundle args = new Bundle();
        args.putStringArrayList("autori", autori);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }
}
