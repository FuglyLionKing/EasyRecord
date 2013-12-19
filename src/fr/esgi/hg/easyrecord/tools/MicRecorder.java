package fr.esgi.hg.easyrecord.tools;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class MicRecorder {
    final MediaRecorder recorder = new MediaRecorder();

   public MicRecorder(int outputFormat, int audioEncoder){
       try{
       recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
       recorder.setOutputFormat(outputFormat);//(MediaRecorder.OutputFormat.THREE_GPP);
       recorder.setAudioEncoder(audioEncoder);//(MediaRecorder.AudioEncoder.AMR_NB);
       }catch (Exception e){
           Log.d("MINE", e.toString());
       }
   }

    /**
     * Starts a new recording.
     */
    public void start(File f) throws  IOException  {

        if(null == f) {
            throw new IllegalArgumentException("The output file to statrt a record cannot be null");
        }

        if (!f.getParentFile().exists() && !f.getParentFile().mkdirs()) {
            throw new IOException("Path to file could not be created.");
        }


        recorder.setOutputFile(f.getAbsolutePath());
        recorder.prepare();
        recorder.start();
    }

    /**
     * Stops a recording that has been previously started.
     */
    public void stop(){
        recorder.stop();
        recorder.release();
    }

}
