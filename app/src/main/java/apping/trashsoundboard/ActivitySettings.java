package apping.trashsoundboard;

import android.Manifest;
import android.app.Notification;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.media.RingtoneManager.getDefaultUri;
import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class ActivitySettings extends AppCompatActivity {

    Switch switch_opencall;
    RelativeLayout action_suggest;
    RelativeLayout action_reset_ringtone;
    AppCompatActivity activity;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Log.d("Settings","OK");
        setTitle("Impostazioni");

        //Dichiarazione
        switch_opencall = findViewById(R.id.switch_opencall);
        action_suggest = findViewById(R.id.box_2);
        action_reset_ringtone = findViewById(R.id.box_3);

        //Variabili
        activity = this;
        sp = getSharedPreferences("TrashSoundBoard", MODE_PRIVATE);

        //Design
        switch_opencall.setChecked(sp.getBoolean("opencall", false));

        //Listener
        switch_opencall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (checkCallingOrSelfPermission(Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED) {
                        SharedPreferences.Editor ed = sp.edit();
                        ed.putBoolean("opencall", isChecked);
                        ed.commit();
                    } else {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                    }

                } else {
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putBoolean("opencall", isChecked);
                    ed.commit();
                }


            }
        });


        action_suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setData(Uri.parse("mailto:"));
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"appinghelp.tsb@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Suggerimento per Trash SoundBoard");
                i.putExtra(Intent.EXTRA_TEXT   , "");
                try {
                    startActivity(Intent.createChooser(i, "Invia email..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Non ci sono client email installati!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        action_reset_ringtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri existingRingtonePath = getDefaultUri(RingtoneManager.TYPE_RINGTONE);

                RingtoneManager.setActualDefaultRingtoneUri(
                        getApplicationContext(),
                        RingtoneManager.TYPE_RINGTONE, existingRingtonePath);

                if (setDefaultRing()){
                    Snackbar.make(view, "Suoneria reimpostata correttamente", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                } else{
                    Snackbar.make(view, "Si è verificato un errore", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i=0; i<grantResults.length; i++){
            Log.d("Perm", "premission: " + permissions[i] + "result:" + grantResults);
            if (grantResults[i]==PERMISSION_GRANTED){
                Log.d("Perm", "permesso accettato");
                SharedPreferences.Editor ed = sp.edit();
                ed.putBoolean("opencall", true);
                ed.commit();
            } else {
                Log.d("Perm", "permesso rifiutato");
                SharedPreferences.Editor ed = sp.edit();
                ed.putBoolean("opencall", false);
                ed.commit();
                switch_opencall.setChecked(false);
                Snackbar.make(getWindow().getDecorView().getRootView(), "Questo permesso è indispensabile per usare questa funzione", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }


        }

    }


    public boolean setDefaultRing(){

        settingPermission();
        Uri existingRingtonePath = getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        try {
            RingtoneManager.setActualDefaultRingtoneUri(
                    getApplicationContext(),
                    RingtoneManager.TYPE_NOTIFICATION, existingRingtonePath);


            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }

    }


    public void settingPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }
}
