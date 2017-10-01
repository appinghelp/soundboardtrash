package apping.trashsoundboard;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;


public class Prima extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    Context context;
    XMLParser xml;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;

    GridView gridView;
    CustomActionBar customActionBar;
    AdapterGridPrima adapterGridPrima;

    ArrayList<String> autori;
    ArrayList<String> sfondi;
    SharedPreferences settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_prima, container, false);



        gridView = rootView.findViewById(R.id.lista_autori);

        context = getActivity().getApplicationContext();
        xml = new XMLParser(getActivity().getApplicationContext());

        autori = xml.selectAllAutore();
        sfondi = xml.selectAllSfondi();

        settings = getActivity().getSharedPreferences("TrashSoundBoard", 0);

        //Admob
        MobileAds.initialize(context,
                context.getResources().getString(R.string.app_id));
        mInterstitialAd = new InterstitialAd(getActivity().getApplicationContext());
        mInterstitialAd.setAdUnitId(getString(R.string.ads_insterstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("AdMobTest", "Interstitial caricato");

                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("adcounter", settings.getInt("adcounter", 0) + 1);

                Log.d("AdMobTest", settings.getInt("adcounter", 0)+"");

                if (settings.getInt("adcounter", 0)>2){
                    if (mInterstitialAd.isLoaded()) {
                        Log.d("AdMobTest", "Interstitial caricato");
                        mInterstitialAd.show();
                        editor.putInt("adcounter", 0);
                    } else {
                        Log.d("AdMobTest", "Interstitial ancora non caricato");
                        while(mInterstitialAd.isLoaded()){
                            Log.d("AdMobTest", "Interstitial caricato");
                            mInterstitialAd.show();
                            editor.putInt("adcounter", 0);
                        }
                    }


                }

                editor.commit();
            }
        });

        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //customActionBar = new CustomActionBar(((AppCompatActivity)getActivity()).getSupportActionBar(), getActivity().getApplicationContext(), getFragmentManager());

        adapterGridPrima = new AdapterGridPrima(getActivity(), autori, sfondi);


        gridView.setAdapter(adapterGridPrima);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Autori");


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                updateDisplay(pos);
            }
        });

        context = getActivity().getApplicationContext();

        return rootView;
    }

    private void updateDisplay(int position) {

        Fragment fragment = new Pagina();

        Bundle bundle = new Bundle();
        bundle.putString("autore", xml.selectAllAutore().get(position));
        // set Fragmentclass Arguments

        fragment.setArguments(bundle);

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            // update selected item and title, then close the drawer
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }



    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}