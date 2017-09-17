package apping.trashsoundboard;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class AdapterGridPreferiti extends ArrayAdapter<String> {

    Activity context;
    ArrayList<String> autori;
    String autore;
    ArrayList<String> nomipref;
    ArrayList<String> suonipref;

    XMLParser xml;
    AdapterGridPagina adapter;

    ImageView stop;
    ImageView pause;

    public AdapterGridPreferiti(Activity context, ArrayList<String> autori, ImageView stop, ImageView pause) {
            super(context, R.layout.item_grid_preferiti, autori);
        this.autori = autori;
        this.context = context;
        this.stop = stop;
        this.pause = pause;

        xml = new XMLParser(context);


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        DataHolder holder = null;



        if(holder == null) {
            v = inflater.inflate(R.layout.item_grid_preferiti, parent, false);
            holder = new DataHolder();

            holder.titolo = v.findViewById(R.id.tv_autore);
            holder.gridView = v.findViewById(R.id.lista_suoni);
        } else{
            holder = (DataHolder) v.getTag();
        }

        v.setTag(holder);

        if (!xml.selectAllPrefSuoniFrom(autori.get(position)).isEmpty()) {
            nomipref = xml.selectAllPrefNome(autori.get(position));
            suonipref = xml.selectAllPrefSuoniFrom(autori.get(position));
            autore = autori.get(position);

            xml.reset();


            adapter = new AdapterGridPagina(context, nomipref, suonipref, autore);

            AudioPlay audioPlay = new AudioPlay();

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    audioPlay.stopAudio();
                }
            });

            pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (audioPlay.isPlaying){
                        audioPlay.pauseAudio();
                        pause.setImageResource(R.drawable.ic_play);
                    } else{
                        audioPlay.continueAudio();
                        pause.setImageResource(R.drawable.ic_pause);
                    }
                }
            });


            Log.d("Pref", "Autore: " + autore);
            Log.d("Pref", "Nomi autore: " + nomipref);
            Log.d("Pref", "Suoni autore: " + suonipref);

            Log.d("Pref", "size: " + suonipref.size());

            holder.titolo.setText(autore);
            holder.gridView.setAdapter(adapter);
        }else{
            Log.d("Pref", "suoni non presenti");

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
            params.height = 0;
            v.setLayoutParams(params);
        }
        return v;
    }

    static class DataHolder{
        TextView titolo;
        GridViewItem gridView;
    }


}