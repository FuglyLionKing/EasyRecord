package fr.esgi.hg.easyrecord;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import fr.esgi.hg.easyrecord.tools.FileTools;
import fr.esgi.hg.easyrecord.tools.MicRecorder;

import java.io.File;
import java.io.IOException;

public class RecorderFragment extends Fragment {

    private recordHandler onRecorded;

    public interface recordHandler{
        public void onSoundRecorded(File file);
    }
    
    public void setOnSoundRecorded(recordHandler handler){
        this.onRecorded = handler;
    }

     private boolean recording = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.recorder_view, container, false);

        Button x = (Button) rootView.findViewById(R.id.startrecord);

        final MicRecorder recorder = new MicRecorder(MediaRecorder.OutputFormat.THREE_GPP, MediaRecorder.AudioEncoder.AMR_NB);



        x.setOnClickListener(new View.OnClickListener() {
            File f;

            @Override
            public void onClick(View view) {
                if(recording){
                    recorder.stop();
                    recording = false;
                    if(null != onRecorded)
                        onRecorded.onSoundRecorded(f);
                    Toast.makeText(RecorderFragment.this.getActivity(), "Record stored in "+f.toString(), Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        f = FileTools.getNewRecordFile();
                        recorder.start(f);
                        Toast.makeText(RecorderFragment.this.getActivity(), "Recording", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    recording = true;
                }
            }
        });

		return rootView;
	}
}
