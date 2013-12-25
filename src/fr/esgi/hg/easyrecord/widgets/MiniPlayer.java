package fr.esgi.hg.easyrecord.widgets;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import fr.esgi.hg.easyrecord.R;
import fr.esgi.hg.easyrecord.tools.Player;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: Voodoo
 * Date: 19/12/13
 * Time: 18:56
 * To change this template use File | Settings | File Templates.
 */
public class MiniPlayer extends LinearLayout {

    private Button pauseResume;
    private Button back;
    private SeekBar seekbar;

    private File file = null;

//    private AudioManager am;

    private Player player = new Player();

    Timer timer = new Timer();

    TimerTask updater = new TimerTask() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    private Runnable ProgressUpdater = new Runnable() {
        public void run() {
            seekbar.setProgress(player.getCurrentPosition());
        }
    };

    public MiniPlayer(Context context) {
        super(context);
        initializer(context,null);
    }
    public MiniPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializer(context,attrs);
    }

    public MiniPlayer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializer(context,attrs);
    }

    private void initializer(Context context, AttributeSet attrs){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.widget_mini_player, this, true);

        this.setOrientation(LinearLayout.VERTICAL);

        seekbar = (SeekBar) getChildAt(0);
        back = (Button) ((LinearLayout)getChildAt(1)).getChildAt(0);
        pauseResume = (Button) ((LinearLayout)getChildAt(1)).getChildAt(1);


        pauseResume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player.isPlaying())
                    pause();
                else
                    resume();
            }
        });

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player.isPlaying())
                    stop();
                else
                    play();
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //To change body of implemented methods use File | Settings | File Templates.
                player.seekTo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        player.setCompletitionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopUpdate();
            }
        });

    }


    public void play(){
        if(null != file && file.exists())
            player.start(file);

        seekbar.setMax(player.getDuration());
        seekbar.setProgress(0);

        startUpdate();
    }

    public void pause(){
        player.pause();

        stopUpdate();
    }

    public void resume(){
        player.resume();

        startUpdate();
    }

    public void stop(){
        player.stop();
        seekbar.setProgress(0);

        stopUpdate();

    }

    public void stopUpdate(){
        timer.cancel();
    }

    public void startUpdate(){
        if(null != timer)
            timer.cancel();

        timer = new Timer();
        timer.schedule(updater, 1000, player.getDuration() - player.getCurrentPosition());
    }

    private void updateProgress(){
        post(ProgressUpdater);
    }

    public void setFile(File f){
        file = f;
    }


}
