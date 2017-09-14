package apping.trashsoundboard;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Luca on 08/09/2017.
 */

public class ManageFavorite {

    Context c;

    public ManageFavorite(Context context){
        this.c = context;

    }

    public void insertFavorite(String id){
        ArrayList<String> list = new ArrayList<>(50);
        if (getSavedArrayList()==null)
            list.add(id);
        else {
            list = getSavedArrayList();
            list.add(id);
        }
        saveArrayList(list);

    }

    public void removeFavorite(String id){
        ArrayList<String> list = null;
        list = getSavedArrayList();
        if (list!=null)
            list.remove(new String(id));

        saveArrayList(list);

    }


    public boolean isFavorite(String id){
        ArrayList<String> list = null;

        list = getSavedArrayList();

        if (list!=null)

            if (list.contains(id))
                return true;
            else
                return false;
        else
            return false;

    }

    private ArrayList<String> getSavedArrayList() {
        ArrayList<String> savedArrayList = null;

        try {
            FileInputStream inputStream = c.openFileInput("favoritelist");
            ObjectInputStream in = new ObjectInputStream(inputStream);
            savedArrayList = (ArrayList<String>) in.readObject();
            in.close();
            inputStream.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return savedArrayList;
    }



    private void saveArrayList(ArrayList<String> arrayList) {
        try {
            FileOutputStream fileOutputStream = c.openFileOutput("favoritelist", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(arrayList);
            out.close();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
