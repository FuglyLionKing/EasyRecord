package fr.esgi.hg.easyrecord;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import fr.esgi.hg.easyrecord.tools.MicRecorder;
import fr.esgi.hg.easyrecord.widgets.MiniPlayer;

import java.io.File;
import java.io.IOException;

public class RecorderFragment extends Fragment {

     boolean recoring = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.recorder_view, container, false);

        Button x = (Button) rootView.findViewById(R.id.startrecord);
        MiniPlayer player = (MiniPlayer) rootView.findViewById(R.id.player);

        final File f = new File(Environment.getDataDirectory().getAbsolutePath()+File.pathSeparator+"myrecTmp");
        final MicRecorder recorder = new MicRecorder(MediaRecorder.OutputFormat.THREE_GPP, MediaRecorder.AudioEncoder.AMR_NB);



        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recoring){
                    recorder.stop();
                    recoring = false;
                }else{
                    try {
                        recorder.start(f);
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    recoring = true;
                }
            }
        });

        player.setFile(f);
		
		return rootView;
	}
}
