package fr.esgi.hg.easyrecord.tools;

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
    private MediaPlayer mediaPlayer;
    private boolean prepared = false;


    public void start(File f){
        if(null == f || !f.exists())
            throw new IllegalArgumentException("In order to play a file it needs to exist");

        //played something before already
        if(null != mediaPlayer){
           stop();
        }

        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(f.getAbsolutePath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        prepared = true;
    }

    public void pause(){
        if(!prepared)
            return;

        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    public void resume(){
        if(!prepared)
            return;

        mediaPlayer.start();
    }

    public void stop(){
        if(!prepared)
            return;
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        prepared = false;
    }

    public void seekToPct(float wherePct){
        if(!prepared)
            return;

        if(0f > wherePct)
            wherePct = 0f;
        else if(1f < wherePct)
            wherePct = 1f;

        mediaPlayer.seekTo((int) (mediaPlayer.getDuration() * wherePct));

    }

    public void seekTo(int where){
        if(!prepared)
            return;

        if(where < 0)
            where = 0;
        if(where > mediaPlayer.getDuration())
            where = mediaPlayer.getDuration();

        mediaPlayer.seekTo(where);
    }

    public float getCurrentPositionPct(){
        return prepared  && 0 != mediaPlayer.getDuration() ? ((1f / mediaPlayer.getDuration()) * mediaPlayer.getCurrentPosition()) : 0;
    }

    public void setVolume(float vol){
        if(!prepared)
            return;

        mediaPlayer.setVolume(vol, vol);
    }

    public boolean isPlaying(){
        return prepared ? mediaPlayer.isPlaying() : false;
    }


    public int getCurrentPosition(){
        return prepared ? mediaPlayer.getCurrentPosition() : 0;
    }

    public int getDuration(){
        return prepared ? mediaPlayer.getDuration() : 0;

    }

}
