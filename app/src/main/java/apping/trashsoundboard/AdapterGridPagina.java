package apping.trashsoundboard;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class AdapterGridPagina extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> nome;
    private final ArrayList<String> filesuono;
    private final String autore;

    public MediaPlayer mp;
    int oldpos = -1;
    int newpos = -1;

    File fileSaved;
    ContentValues values;
    XMLParser xml;
    ManageFavorite manageFavorite;

    int sound;
    int position;

    public AdapterGridPagina( Activity context, ArrayList<String> nome, ArrayList<String> filesuono, String autore) {
        super(context, R.layout.item_grid_pagina, nome);

        this.context = context;
        this.nome = nome;
        this.filesuono = filesuono;
        this.autore = autore;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.item_grid_pagina, parent, false);





        xml = new XMLParser(context);
        manageFavorite = new ManageFavorite(context);

        TextView testo = rowView.findViewById(R.id.testo);
        ImageView share = rowView.findViewById(R.id.share);
        ImageView download = rowView.findViewById(R.id.download);
        ImageView sfondo = rowView.findViewById(R.id.sfondo);
        ImageView ringtone = rowView.findViewById(R.id.ringtone);
        ImageView pref = rowView.findViewById(R.id.pref);

        if (manageFavorite.isFavorite(filesuono.get(position)))
            pref.setImageResource(R.drawable.ic_star);
        else
            pref.setImageResource(R.drawable.ic_star_);

        testo.setText(nome.get(position));

        Glide.with(context).load(xml.selectSfondo(autore)).into(sfondo);

        this.position = position;

        testo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldpos = newpos;
                newpos = position;

                try {

                    if (oldpos == newpos) {
                        if (mp.isPlaying()) {
                            mp.stop();
                            mp.release();

                        } else {
                            int sound = context.getResources().getIdentifier(filesuono.get(position), "raw", context.getPackageName());
                            mp = MediaPlayer.create(context, sound);
                            mp.start();
                        }
                    } else {
                        if (mp.isPlaying()) {
                            mp.stop();
                            mp.release();
                        }
                        int sound = context.getResources().getIdentifier(filesuono.get(position), "raw", context.getPackageName());
                        mp = MediaPlayer.create(context, sound);
                        mp.start();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    int sound = context.getResources().getIdentifier(filesuono.get(position), "raw", context.getPackageName());
                    mp = MediaPlayer.create(context, sound);
                    mp.start();
                }

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!shareSound(context.getResources().getIdentifier(filesuono.get(position), "raw", context.getPackageName()), nome.get(position), filesuono.get(position), autore)) {
                    Snackbar.make(view, "E' stato riscontrato un errore", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveSound(context.getResources().getIdentifier(filesuono.get(position), "raw", context.getPackageName()), nome.get(position), filesuono.get(position), autore))
                    Snackbar.make(view, "Suono salvato correttamente", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                else
                    Snackbar.make(view, "E' stato riscontrato un errore", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

            }
        });

        ringtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (setAsRingtone(context.getResources().getIdentifier(filesuono.get(position), "raw", context.getPackageName()), nome.get(position), filesuono.get(position), autore))
                    Snackbar.make(view, "Suoneria impostata correttamente", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                else
                    Snackbar.make(view, "Si è verificato un errore", Snackbar.LENGTH_SHORT).setAction("Action", null).show();


            }
        });

        pref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Pref", "click");
                if (manageFavorite.isFavorite(filesuono.get(position))) {
                    Log.d("Pref", position + " is favorite");
                    manageFavorite.removeFavorite(filesuono.get(position));
                    pref.setImageResource(R.drawable.ic_star_);
                } else {
                    Log.d("Pref", position + " is not favorite");
                    manageFavorite.insertFavorite(filesuono.get(position));
                    pref.setImageResource(R.drawable.ic_star);
                }
            }
        });


        return rowView;
    }


    public void stopSound(){
        try{
            mp.stop();
            mp.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying(){
        try{
            return mp.isPlaying();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean shareSound(int ressound, String nameName, String soundName, String autorName){

        Log.d("Share", "Position: " + position);
        Log.d("Share", "Resource: " + ressound);
        Log.d("Share", "Resource: " + filesuono.get(position));


        String path = Environment.getExternalStorageDirectory() + "/TrashSoundboard/audio/";
        String filename = "shared.mp3";

        boolean exists = (new File(path)).exists();
        if (!exists) {
            new File(path).mkdirs();
        }

        File dest = new File(path, filename);
        InputStream in = context.getResources().openRawResource(ressound);

        try
        {
            OutputStream out = new FileOutputStream(path + filename);
            byte[] buf = new byte[1024];
            int len;
            while ( (len = in.read(buf, 0, buf.length)) != -1)
            {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
        catch (Exception e) {}

        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(path + filename));
        share.setType("audio/*");
        context.startActivity(Intent.createChooser(share, "Condividi il suono \"" + nameName + "\""));
        return true;
    }

    public boolean saveSound(int ressound, String nameName, String soundName, String autorName) {

        Log.d("Save", "Position: " + position);
        Log.d("Save", "Resource: " + ressound);
        Log.d("Save", "Resource: " + filesuono.get(position));

        byte[] buffer = null;
        InputStream fIn = context.getResources().openRawResource(ressound);
        int size = 0;

        try {
            size = fIn.available();
            buffer = new byte[size];
            fIn.read(buffer);
            fIn.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return false;
        }

        String path = Environment.getExternalStorageDirectory() + "/TrashSoundboard/audio/";
        String filename = soundName + ".mp3";

        boolean exists = (new File(path)).exists();
        if (!exists) {
            new File(path).mkdirs();
        }

        FileOutputStream save;
        try {
            save = new FileOutputStream(path + filename);
            save.write(buffer);
            save.flush();
            save.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return false;
        }

        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+path+filename)));


        fileSaved = new File(path, filename);

        values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, fileSaved.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, nameName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.ARTIST, autorName);
        values.put(MediaStore.Audio.Media.ALBUM, "TrashSoundboard");
        values.put(MediaStore.Audio.Media.COMPOSER, autorName);
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, nameName);
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        values.put(MediaStore.Audio.Media.IS_ALARM, true);
        values.put(MediaStore.Audio.Media.IS_MUSIC, true);

        //Insert it into the database
        context.getContentResolver().insert(MediaStore.Audio.Media.getContentUriForPath(fileSaved.getAbsolutePath()), values);


        return true;
    }

    public void settingPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(context)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intent);
            }
        }
    }


    public boolean setAsRingtone(int ressound, String nameName, String soundName, String autorName){

        settingPermission();

        byte[] buffer = null;
        InputStream fIn = context.getResources().openRawResource(ressound);
        int size = 0;

        try {
            size = fIn.available();
            buffer = new byte[size];
            fIn.read(buffer);
            fIn.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

        String path = Environment.getExternalStorageDirectory() + "/TrashSoundboard/audio/";
        String filename = soundName + ".mp3";
        String filepath = path + "/" + filename;
        File ringtoneFile = new File(filepath);


        boolean exists = (new File(path)).exists();
        if (!exists) {
            new File(path).mkdirs();
        }

        FileOutputStream save;
        try {
            save = new FileOutputStream(path + "/" + filename);
            save.write(buffer);
            save.flush();
            save.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }


        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+path+"/"+filename)));


        ContentValues content = new ContentValues();
        content.put(MediaStore.MediaColumns.DATA, ringtoneFile.getAbsolutePath());
        content.put(MediaStore.MediaColumns.TITLE, nameName);
        content.put(MediaStore.MediaColumns.SIZE, 215454);
        content.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
        content.put(MediaStore.Audio.Media.DURATION, 230);
        content.put(MediaStore.Audio.Media.IS_RINGTONE, false);
        content.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        content.put(MediaStore.Audio.Media.IS_ALARM, false);
        content.put(MediaStore.Audio.Media.IS_MUSIC, false);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(ringtoneFile.getAbsolutePath());
        context.getContentResolver().delete(
                uri,
                MediaStore.MediaColumns.DATA + "=\""
                        + ringtoneFile.getAbsolutePath() + "\"", null);
        Uri newUri = context.getContentResolver().insert(uri, content);

        Log.d("Uri", "newUri: "+newUri);
        Log.d("Uri", "ringtoneFile: "+ringtoneFile.getAbsolutePath());

        try {
            RingtoneManager.setActualDefaultRingtoneUri(getContext(), RingtoneManager.TYPE_NOTIFICATION, newUri);
            Log.d("DEBUG", "newuri custom ring: " + newUri);
            return true;

        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }


}