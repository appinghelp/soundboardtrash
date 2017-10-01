package apping.trashsoundboard;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterGridPrima extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> autori;
    private final ArrayList<String> sfondi;


    public AdapterGridPrima(Activity context, ArrayList<String> autori, ArrayList<String> sfondi) {
        super(context, R.layout.item_grid_pagina, autori);

        this.context = context;
        this.autori = autori;
        this.sfondi = sfondi;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.item_grid_prima, parent, false);


        TextView testo = rowView.findViewById(R.id.testo);
        ImageView img = rowView.findViewById(R.id.img_autore);

        testo.setText(autori.get(position));
        int res = context.getResources().getIdentifier(
                sfondi.get(position),
                "drawable", context.getPackageName());

        Glide.with(context).load(res).into(img);

        /*Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res);
        int width = (bitmap.getWidth() * 70 / bitmap.getHeight());
        Bitmap scale = Bitmap.createScaledBitmap(bitmap, width, 70, true);
        img.setImageBitmap(scale);*/




        return rowView;
    }

}