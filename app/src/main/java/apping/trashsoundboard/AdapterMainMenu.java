package apping.trashsoundboard;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class AdapterMainMenu extends BaseAdapter {

    Context context;
    String[] result;
    String[] imageId;

    private static LayoutInflater inflater=null;
    public AdapterMainMenu(Context context, String[] prgmNameList, String[] imageId) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        this.imageId=imageId;
        this.context = context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_mainmenu, null);

        // Set the font
        //Typeface font = Typeface.createFromAsset(context.getAssets(), "bofont.ttf");
        //holder.tv.setTypeface(font);

        holder.tv=(TextView) rowView.findViewById(R.id.list_title);
        holder.tv.setText(result[position]);

        holder.img=(ImageView) rowView.findViewById(R.id.list_image);

        int res = context.getResources().getIdentifier(imageId[position], "drawable", context.getPackageName());

        Glide.with(context).load(res).into(holder.img);



        /*Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res);
        int height = (bitmap.getHeight() * 50 / bitmap.getWidth());
        Bitmap scale = Bitmap.createScaledBitmap(bitmap, 50, height, true);
        holder.img.setImageBitmap(scale);*/

        return rowView;
    }
}