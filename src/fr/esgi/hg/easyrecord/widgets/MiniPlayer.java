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
    private Button playStop;
    private SeekBar seekbar;

    private File file = null;

    private Player player = new Player();

    private Timer timer = new Timer();

    private TimerTask updater;

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
        playStop = (Button) ((LinearLayout)getChildAt(1)).getChildAt(0);
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

        playStop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player.isPlaying())
                    stop();
                else
                    play();
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean manualChange = false;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(manualChange)
                    player.seekTo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                manualChange = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                manualChange = false;
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


        playStop.setText("stop");
        pauseResume.setText("pause");

        seekbar.setMax(player.getDuration());
        seekbar.setProgress(0);

        startUpdate();
    }

    public void pause(){
        player.pause();


        playStop.setText("stop");
        pauseResume.setText("resume");

        stopUpdate();
    }

    public void resume(){
        player.resume();


        playStop.setText("stop");
        pauseResume.setText("pause");

        startUpdate();
    }

    public void stop(){
        player.stop();
        seekbar.setProgress(0);

        playStop.setText("play");
        pauseResume.setText("pause");

        stopUpdate();

    }

    public void stopUpdate(){
        if(null == timer)
            return;

        timer.cancel();
        updater.cancel();
    }

    public void startUpdate(){
        if(null != timer)
            timer.cancel();

        if(null != updater)
            updater.cancel();

        //I'm not sure this is the 'properest' way but it works well enough !
        updater = getNewUpdater();
        timer = new Timer();

        int updateFreq = player.getDuration()/1000;
        if(0 >= updateFreq)
            return;

        timer.schedule(updater, updateFreq, updateFreq);
    }

    private void updateProgress(){
        //post() runs on Ui's thread
        post(ProgressUpdater);
    }

    public void setFile(File f){
        file = f;
    }

    private TimerTask getNewUpdater(){
        return new TimerTask() {
            @Override
            public void run() {
                //we don't update from this thread
                //because that's UI's business
                updateProgress();
            }
        };
    }

}
