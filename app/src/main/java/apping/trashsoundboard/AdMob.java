package apping.trashsoundboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by Luca on 03/09/2017.
 */



public class AdMob {

    private InterstitialAd mInterstitialAd;


    AdMob(Context c){
        MobileAds.initialize(c,
                c.getString(R.string.app_id));
        mInterstitialAd = new InterstitialAd(c);
        mInterstitialAd.setAdUnitId(c.getString(R.string.ads_insterstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

    }

    public void loadInterstitial(SharedPreferences sp){

        SharedPreferences.Editor editor = sp.edit();

        if (mInterstitialAd.isLoaded()) {
            editor.putInt("adcounter", 0);
            Log.d("ADCOUNTER", "contatore = 3");
            Log.d("ADCOUNTER", sp.getInt("adcounter", 0)+"");
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }
}
