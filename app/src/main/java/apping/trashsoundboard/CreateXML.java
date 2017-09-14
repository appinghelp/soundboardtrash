package apping.trashsoundboard;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Luca on 19/11/2015.
 */

public class CreateXML {


    String sBody;
    File gpxfile;
    Context context;
    int ndatabase;
    ArrayList<Integer> database = new ArrayList<Integer>(20);


    public CreateXML(Context context){
        this.context = context;
        Log.d("CreateXML", String.valueOf(database));
    }


    public File createFile() {


        sBody = readFile();

        Log.d("CreateXML", "Create file");


        try {
            File root = new File(Environment.getExternalStorageDirectory(), "TrashSoundBoard");
            if (!root.exists()) {
                root.mkdirs();
            }
            gpxfile = new File(root, "tb.xml");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gpxfile;
    }

    public String readFile(){

        StringBuilder text;
        try {

            InputStream inputStream = context.getResources().openRawResource(R.raw.tabella);

            InputStreamReader inputreader = new InputStreamReader(inputStream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            String line;
            text = new StringBuilder();

            while (( line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return text.toString();

    }

   /* String syslanguage;
    String customlanguage;

    public ArrayList<Integer> selectDatabase(){

        syslanguage = Locale.getDefault().getLanguage();
        customlanguage = new Settings().language;

        Log.d("CreateXML", "Language System: " + syslanguage);
        Log.d("CreateXML", "Language Changed: " + customlanguage);

        // SYSTEM LANGUAGE
        if (customlanguage.equals("")) {
            Log.e("CreateXML", "customlanguage null");
            if (syslanguage.contains("en")) {
                Log.e("CreateXML", "language selected: English");
                database.add(0, R.raw.armi_en);
                database.add(1, R.raw.mappe_en);
                database.add(2, R.raw.perks_en);
                database.add(3, R.raw.wildcard_en);
                database.add(4, R.raw.score_en);
            }
            else if (syslanguage.contains("it")) {
                Log.e("CreateXML", "language selected: Italian");
                database.add(0, R.raw.armi_it);
                database.add(1, R.raw.mappe_it);
                database.add(2, R.raw.perks_it);
                database.add(3, R.raw.wildcard_it);
                database.add(4, R.raw.score_it);

            }
            else if (syslanguage.contains("es")) {
                Log.e("CreateXML", "language selected: Spanish");
                database.add(0, R.raw.armi_es);
                database.add(1, R.raw.mappe_es);
                database.add(2, R.raw.perks_es);
                database.add(3, R.raw.wildcard_es);
                database.add(4, R.raw.score_es);

            } else {
                Log.e("CreateXML", "language selected: English");
                database.add(0, R.raw.armi_en);
                database.add(1, R.raw.mappe_en);
                database.add(2, R.raw.perks_en);
                database.add(3, R.raw.wildcard_en);
                database.add(4, R.raw.score_en);

            }
        }
        // CUSTOM LANGUAGE
        else {
            Log.e("CreateXML", "customlanguage not null");
            if (customlanguage.equals("en")) {
                Log.e("CreateXML", "language selected: English");
                database.add(0, R.raw.armi_en);
                database.add(1, R.raw.mappe_en);
                database.add(2, R.raw.perks_en);
                database.add(3, R.raw.wildcard_en);
                database.add(4, R.raw.score_en);

            }
            if (customlanguage.equals("it")) {
                Log.e("CreateXML", "language selected: Italian");
                database.add(0, R.raw.armi_it);
                database.add(1, R.raw.mappe_it);
                database.add(2, R.raw.perks_it);
                database.add(3, R.raw.wildcard_it);
                database.add(4, R.raw.score_it);

            }
            if (customlanguage.equals("es")) {
                Log.e("CreateXML", "language selected: Spanish");
                database.add(0, R.raw.armi_es);
                database.add(1, R.raw.mappe_es);
                database.add(2, R.raw.perks_es);
                database.add(3, R.raw.wildcard_es);
                database.add(4, R.raw.score_es);

            }
        }


        return database;
    }
    */

}
