package fr.esgi.hg.easyrecord;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import fr.esgi.hg.easyrecord.tools.Record;
import fr.esgi.hg.easyrecord.tools.SqliteController;

import java.io.File;

/**
 * Created by Damian
 */
public class EditActivity extends Activity {

    private File recordFile;
    private EditText editTitle, editCreation, editModification, editComment;
    private Record record;
    private SqliteController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);
        dbController = new SqliteController(getApplicationContext());
        this.setActionButton();
        this.getFields();
        this.initFields();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbController.close();
    }

    private void getFields() {
        editTitle        = (EditText) findViewById(R.id.editTitle);
        editCreation     = (EditText) findViewById(R.id.editCreation);
        editModification = (EditText) findViewById(R.id.editModification);
        editComment      = (EditText) findViewById(R.id.editComment);
    }

    private void initFields() {
        String filePath = this.getIntent().getStringExtra("path");
        recordFile = new File(filePath);

        record = dbController.selectRecord(filePath);
        if(record!=null) {
            editTitle.setText(record.title);
            editCreation.setText(record.creationDate);
            editModification.setText(record.modificationDate);
            editComment.setText(record.comment);
        } else {
            editTitle.setText("");
            editCreation.setText("");
            editModification.setText("");
            editComment.setText("");
        }
    }

    private void setActionButton() {
        Button save = (Button) findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditActivity.this.saveRecord();
                finish();
            }
        });
    }

    private void saveRecord() {
        record.title = editTitle.getText().toString();
        record.creationDate = editCreation.getText().toString();
        record.modificationDate = editModification.getText().toString();
        record.comment = editComment.getText().toString();

        dbController.updateRecord(record);
    }
}
