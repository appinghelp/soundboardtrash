package apping.trashsoundboard;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class Preferiti extends Fragment {

    XMLParser xml;
    ArrayList<String> autoripreferiti;
    GridView gridView;
    ImageView sfondo;
    Activity context;
    AdapterGridPreferiti adapter;
    private AdView mAdView;
    ManageFavorite manageFavorite;

    ImageView stop;
    ImageView pause;
    ImageView casual;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_preferiti, container, false);

        context = getActivity();

        manageFavorite = new ManageFavorite(context);
        xml = new XMLParser(getActivity().getApplicationContext());


        autoripreferiti = xml.selectAllPrefAutori();


        Log.d("Pref", "tutti gli autori: "+ autoripreferiti);

        sfondo = rootView.findViewById(R.id.sfondo);
        gridView = rootView.findViewById(R.id.lista_suoni);
        stop = rootView.findViewById(R.id.bn_stop);
        pause = rootView.findViewById(R.id.bn_pause);
        casual = rootView.findViewById(R.id.bn_casuale);

        adapter = new AdapterGridPreferiti(context, autoripreferiti, stop, pause);

        //Admob
        MobileAds.initialize(context,
                getString(R.string.app_id));

        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        gridView.setAdapter(adapter);

        //new CustomActionBar(((AppCompatActivity)getActivity()).getSupportActionBar(), getActivity().getApplicationContext(), getFragmentManager()).setTitle(autore);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Preferiti");


        casual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        return rootView;

    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            new AudioPlay().stopAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
