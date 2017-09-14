package apping.trashsoundboard;


import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


public class CustomActionBar {

    ActionBar mActionBar;
    FragmentManager fragmentManager;
    Context context;
    TextView mTitle;

    SharedPreferences sp;
    public static final String MyPREFERENCES = "BO2RankingSystem" ;

    XMLParser xml;

    CustomActionBar(ActionBar actionBar, final Context context, FragmentManager fragmentManager){
        mActionBar = actionBar;
        this.context = context;
        this.fragmentManager = fragmentManager;

        xml = new XMLParser(context);
        sp = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater mInflater = LayoutInflater.from(actionBar.getThemedContext());
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);

        mTitle = mCustomView.findViewById(R.id.title_text);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);



    }


    public void setTitle(String title){

        Log.d("CustomActionBar" , context + "ha cambiato il titolo in: " + title);

        mTitle.setText(title);
    }

    public String getTitle(){
        return mTitle.getText().toString();
    }


}
