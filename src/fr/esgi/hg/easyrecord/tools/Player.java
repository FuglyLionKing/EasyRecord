package fr.esgi.hg.easyrecord.tools;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Voodoo
 * Date: 19/12/13
 * Time: 18:24
 * To change this template use File | Settings | File Templates.
 */
public class Player {
    private MediaPlayer mp = new MediaPlayer();
    private boolean prepared = false;


    public void start(File f){
        if(null == f || !f.exists())
            throw new IllegalArgumentException("In order to play a file it needs to exist");
        try {
            mp.setDataSource(f.getAbsolutePath());
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();
        prepared = true;
    }

    public void pause(){
        if(!prepared)
            return;

        if(mp.isPlaying())
            mp.pause();
    }

    public void resume(){
        if(!prepared)
            return;

        mp.start();
    }

    public void stop(){
        if(!prepared)
            return;
        mp.stop();
        mp.release();
        mp.reset();
        prepared = false;
    }

    public void seekTo(float wherePct){
        if(!prepared)
            return;

        if(0f > wherePct)
            wherePct = 0f;
        else if(1f < wherePct)
            wherePct = 1f;

        mp.seekTo((int)(mp.getDuration() * wherePct));

    }

    public float getCurrentPositionPct(){
        return prepared  && 0 != mp.getDuration() ? ((1f / mp.getDuration()) * mp.getCurrentPosition()) : 0;
    }

    public void setVolume(float vol){
        if(!prepared)
            return;

        mp.setVolume(vol, vol);
    }

    public boolean isPlaying(){
        return prepared ? mp.isPlaying() : false;
    }


    public int getCurrentPosition(){
        return prepared ? mp.getCurrentPosition() : 0;
    }

    public int getDuration(){
        return prepared ? mp.getDuration() : 0;
    }

}
