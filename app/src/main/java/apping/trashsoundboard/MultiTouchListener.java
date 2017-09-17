package apping.trashsoundboard;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import static android.content.Context.MODE_PRIVATE;

public class MultiTouchListener implements OnTouchListener
{

    private float mPrevX;
    private float mPrevY;


    Activity context;
    int actionBarHeight;

    String PREF = "TrashSoundBoard";

    boolean unlockButton = false;
    long startTime;
    long endTime;

    int newdim = 25;
    int olddim;
    boolean resetDim = false;
    boolean clickCancelled = false;
    SharedPreferences prefs;

    AdapterGridPagina adapter;




    public MultiTouchListener(Activity context, View view, AdapterGridPagina adapterGridPagina) {
        this.context = context;
        this.adapter = adapterGridPagina;

        prefs = this.context.getSharedPreferences(PREF, MODE_PRIVATE);
        int marginleft = prefs.getInt("marginleft", 200);
        int margintop = prefs.getInt("margintop", 200);
        MarginLayoutParams marginParams = new MarginLayoutParams(view.getLayoutParams());
        marginParams.setMargins(
                marginleft,
               margintop,
                0, 0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
        view.setLayoutParams(layoutParams);


    }



    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float currX,currY;
        int action = event.getAction();


        switch (action) {
            case MotionEvent.ACTION_DOWN: {

                this.olddim = view.getWidth();
                Log.d("FFF", olddim+"");


                mPrevX = event.getX();
                mPrevY = event.getY();

                clickCancelled = false;

                startTime = event.getEventTime();

                final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(isViewInBounds(view, (int)event.getRawX(), (int)event.getRawY()) && !clickCancelled) {
                                Log.d("LK", "Dito dentro il bottone");
                                unlockButton = true;
                                Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(50);
                                ViewGroup.LayoutParams params = view.getLayoutParams();
                                params.height = view.getHeight()-newdim;
                                params.width = view.getWidth()-newdim;
                                view.setLayoutParams(params);

                                resetDim = true;

                            }
                            else {
                                Log.d("LK", "Dito fuori il bottone");
                                unlockButton = false;
                            }

                        }
                    }, 500);

                    view.setBackground(context.getResources().getDrawable(R.drawable.bn_circle_pressed));

                    break;
                }

            case MotionEvent.ACTION_MOVE:
            {

                if(unlockButton){

                    //Log.d("LK", "Action Move passato 1 secondo");


                    currX = event.getRawX();
                    currY = event.getRawY();
                    TypedValue tv = new TypedValue();

                    if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
                    {
                        actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
                    }

                    Rect rectangle = new Rect();
                    Window window = context.getWindow();
                    window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
                    int statusBarHeight = rectangle.top;



                    Log.d("MOVEDD","down x: " + mPrevX );
                    Log.d("MOVEDD","down y: " + mPrevY );

                    MarginLayoutParams marginParams = new MarginLayoutParams(view.getLayoutParams());
                    marginParams.setMargins(
                            (int)(currX - mPrevX),
                            (int)(currY - mPrevY - actionBarHeight - statusBarHeight),
                            0, 0);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
                    view.setLayoutParams(layoutParams);

                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();

                    SharedPreferences.Editor editor = context.getSharedPreferences(PREF, MODE_PRIVATE).edit();
                    editor.putInt("marginleft", lp.leftMargin);
                    editor.putInt("margintop", lp.topMargin);
                    editor.apply();


                    return true; //notify that you handled this event (do not propagate)
                } else {
                    //Log.d("LK", "Action Move non passato 1 secondo");

                }

                break;
            }


            case MotionEvent.ACTION_CANCEL:
                clickCancelled = false;
                break;

            case MotionEvent.ACTION_UP:

                Log.d("multitouch","action up");

                endTime = event.getEventTime();

                if (resetDim){
                    ViewGroup.LayoutParams params = view.getLayoutParams();
                    Log.d("FFF", olddim+"");

                    params.height = olddim;
                    params.width = olddim;
                    view.setLayoutParams(params);
                    resetDim = false;
                }

                if (endTime - startTime < 500) clickCancelled = true;

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        unlockButton = false;
                    }
                }, 500);

                unlockButton = false;

                view.setBackground(context.getResources().getDrawable(R.drawable.bn_circle_normal));

                currX = event.getX();
                currY = event.getY();



                Log.d("multitouch", "prev x: " + mPrevX);
                Log.d("multitouch", "prev y: " + mPrevY);
                Log.d("multitouch", "current x: " + currX);
                Log.d("multitouch", "current y: " + currY);
                Log.d("multitouch", "differenza x: " + (currX-mPrevX));
                Log.d("multitouch", "differenza y: " + (currY-mPrevY));

                if (endTime - startTime < 500){
                    Log.d("multitouch","click");
                    new AudioPlay().stopAudio();
                    Snackbar.make(view, "Tieni premuto il bottone stop per spostare", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    if (prefs.getBoolean("firstTime", true)){
                        SharedPreferences.Editor editor = context.getSharedPreferences(PREF, MODE_PRIVATE).edit();
                        editor.putBoolean("firstTime", false);
                        editor.apply();
                    }
                } else {
                    Log.d("multitouch","no click");


                }

                break;
        }

        return true;
    }

    Rect outRect = new Rect();
    int[] location = new int[2];

    private boolean isViewInBounds(View view, int x, int y){
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }

}