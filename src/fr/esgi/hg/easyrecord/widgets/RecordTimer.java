package fr.esgi.hg.easyrecord.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: FuglyLionKing
 * Date: 25/12/13
 * Time: 15:24
 * To change this template use File | Settings | File Templates.
 */
public class RecordTimer extends TextView{

    private class TimeTrackKeeper{

        int hours = 0;
        int mins = 0;
        int secs = 0;
        int mil = 0;



        void reset(){
            hours = 0;
            mins = 0;
            secs = 0;
            mil = 0;
        }

        void addMiliseconds(int toAdd){
            mil+=toAdd;

            if(1000 > mil)
                return;
            secs+= (mil - (mil = (mil%1000)))/1000;

            if(60 > secs)
                return;
            mins+= (secs - (secs = (secs%60)))/60;

            if(60 > mins)
                return;
            hours+= (mins - (mins = (mins%60)))/60;
        }

        @Override
        public String toString() {
           return String.format("%d:%02d:%02d", hours, mins, secs);
        }

    }

    private Timer timer = new Timer();
    private TimerTask updater;
    private Runnable ProgressUpdater = new Runnable() {
        public void run() {
            RecordTimer.this.setText(current.toString());
        }
    };

    private TimeTrackKeeper current = new TimeTrackKeeper();

    public int interval = 1000;

    public RecordTimer(Context context) {
        super(context);
        initializer(context,null);
    }

    public RecordTimer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializer(context,attrs);
    }

    public RecordTimer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializer(context,attrs);
    }

    private void initializer(Context context, AttributeSet attrs){
        setText(current.toString());
    }

    public void start(){
        if(null != timer)
            timer.cancel();

        if(null != updater)
            updater.cancel();

        //I'm not sure this is the 'properest' way but it works well enough !
        updater = getNewUpdater();
        timer = new Timer();

        timer.schedule(updater, interval, interval);
    }

    public void stop(){
        timer.cancel();
        updater.cancel();
        reset();
    }

    public void reset(){
        current.reset();
        setText(current.toString());
    }

    private void updateProgress(){
        current.addMiliseconds(interval);
        //post() runs on Ui's thread
        post(ProgressUpdater);
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
