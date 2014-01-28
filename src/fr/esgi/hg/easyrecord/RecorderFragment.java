package fr.esgi.hg.easyrecord;

import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import fr.esgi.hg.easyrecord.tools.FileTools;
import fr.esgi.hg.easyrecord.tools.MicRecorder;
import fr.esgi.hg.easyrecord.tools.Record;
import fr.esgi.hg.easyrecord.tools.SqliteController;
import fr.esgi.hg.easyrecord.widgets.RecordTimer;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class RecorderFragment extends Fragment {

    private recordHandler onRecorded;
    private  SqliteController dbController;

    public interface recordHandler{
        public void onSoundRecorded(File file);
    }
    
    public void setOnSoundRecorded(recordHandler handler){
        this.onRecorded = handler;
    }

     private boolean recording = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbController = new SqliteController(getActivity().getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbController.close();
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.recorder_view, container, false);

        final RecordTimer timer = (RecordTimer) rootView.findViewById(R.id.timer);
        final MicRecorder recorder = new MicRecorder(MediaRecorder.OutputFormat.THREE_GPP, MediaRecorder.AudioEncoder.AMR_NB);

        ImageButton button = (ImageButton) rootView.findViewById(R.id.recordButton);
        button.setOnClickListener(new View.OnClickListener() {
            File file;

            @Override
            public void onClick(View view) {
                View parent = (View)view.getParent();
                TextView text = (parent != null) ? (TextView) parent.findViewById(R.id.recordLabel) : null;

                if (recording) {
                    recorder.stop();
                    recording = false;
                    timer.stop();
                    if (null != onRecorded) {
                        onRecorded.onSoundRecorded(file);
                        insertRecord(file);
                    }
                    if(text != null) {
                        text.setTextColor(Color.parseColor("#333333"));
                    }
                    Toast.makeText(RecorderFragment.this.getActivity(), "Record saved", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        file = FileTools.getNewRecordFile();
                        recorder.start(file);
                        timer.start();
                        if(text != null) {
                            text.setTextColor(Color.parseColor("#FF0000"));
                        }
                        //Toast.makeText(RecorderFragment.this.getActivity(), "Recording", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    recording = true;
                }
            }
        });
		return rootView;
	}

    private void insertRecord(File file) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(file.getAbsolutePath());

        Record record = new Record();
        record.path = file.getAbsolutePath();
        record.title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        record.creationDate = new Date().toString();
        record.modificationDate = new Date(file.lastModified()).toString();
        record.comment = "";

        dbController.insertRecord(record);
    }
}
