package apping.trashsoundboard;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class Pagina extends Fragment {

    XMLParser xml;
    GridView gridView;
    int position;
    String autore;
    MultiTouchListener touchListener;
    ImageView sfondo;
    Activity context;
    AdapterGridPagina adapter;
    private AdView mAdView;
    ImageButton bn_stop;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pagina, container, false);

        context = getActivity();


        position = getArguments().getInt("pos");
        autore = getArguments().getString("autore");

        xml = new XMLParser(getActivity().getApplicationContext());
        adapter = new AdapterGridPagina(getActivity(), xml.selectAllNome(autore), xml.selectAllFilesuono(autore), autore);

        sfondo = rootView.findViewById(R.id.sfondo);
        gridView = rootView.findViewById(R.id.lista_suoni);
        bn_stop = rootView.findViewById(R.id.bn_stop);

        ViewCompat.setElevation(bn_stop, 50);


        //Admob
        MobileAds.initialize(context,
                getString(R.string.app_id));

        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        touchListener = new MultiTouchListener(context, bn_stop, adapter);

        bn_stop.setImageResource(R.drawable.ic_stop);
        bn_stop.setOnTouchListener(touchListener);


        gridView.setAdapter(adapter);

        //new CustomActionBar(((AppCompatActivity)getActivity()).getSupportActionBar(), getActivity().getApplicationContext(), getFragmentManager()).setTitle(autore);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(autore);



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
