package fr.esgi.hg.easyrecord;

import android.app.Activity;
import android.media.MediaMetadataEditor;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.nio.*;
import java.util.Date;

/**
 * Created by Damian
 */
public class EditActivity extends Activity {

    private File songFile;
    private EditText editTitle, editCreation, editModification, editComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);
        this.setActionButton();
        this.getFields();
        this.initFields();
    }

    private void getFields() {
        editTitle        = (EditText) findViewById(R.id.editTitle);
        editCreation     = (EditText) findViewById(R.id.editCreation);
        editModification = (EditText) findViewById(R.id.editModification);
        editComment      = (EditText) findViewById(R.id.editComment);
    }

    private void initFields() {
        songFile = new File(this.getIntent().getStringExtra("path"));

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(songFile.getAbsolutePath());

        editTitle.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        //editCreation.setText();
        editModification.setText(new Date(songFile.lastModified()).toString());
        //editComment.setText();
    }

    private void setActionButton() {
        Button save = (Button) findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditActivity.this.saveFields();
                finish();
            }
        });
    }

    private void saveFields() {

    }
}
