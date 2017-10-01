package apping.trashsoundboard;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Luca on 17/09/2017.
 */

public class AudioPlay implements MediaPlayer.OnCompletionListener{

    public static MediaPlayer mp;
    public static boolean isPlaying = false;
    static ImageView pause;

    public void playAudio(Context c, int id){

        mp = MediaPlayer.create(c,id);
        if(!mp.isPlaying())
        {
            isPlaying=true;
            mp.start();
            if (pause!=null){
                pause.setImageResource(R.drawable.ic_pause);
            }
            mp.setOnCompletionListener(this);
        }
    }

    public void stopAudio(){

        try{
            isPlaying=false;
            mp.stop();
            mp.reset();
            mp.release();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void pauseAudio(){

        try{
            isPlaying=false;
            mp.pause();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void continueAudio(){

        try{
            isPlaying=true;
            mp.start();
        } catch (Exception e){
            e.printStackTrace();
        }

    }


    public void setListener(ImageView pause){

        this.pause = pause;
        Log.d("AUDIOPLAY", "pause setted to: " + pause);

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d("AUDIOPLAY", "pause: " + pause);
        if (pause!=null){
            isPlaying=false;
            pause.setImageResource(R.drawable.ic_play);
        }
    }
}
