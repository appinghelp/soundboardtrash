package apping.trashsoundboard;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Luca on 17/09/2017.
 */

public class AudioPlay {

    public static MediaPlayer mp;
    public static boolean isPlaying = false;

    public void playAudio(Context c, int id){

        mp = MediaPlayer.create(c,id);
        if(!mp.isPlaying())
        {
            isPlaying=true;
            mp.start();
        }
    }

    public void stopAudio(){

        try{
            isPlaying=false;
            mp.stop();
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

}
