package fr.esgi.hg.easyrecord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import fr.esgi.hg.easyrecord.tools.Record;
import fr.esgi.hg.easyrecord.tools.SqliteController;

import java.io.File;

/**
 * Created by Damian
 */
public class InformationActivity extends Activity {

    private File recordFile;
    private TextView textTitle, textCreation, textModification, textComment;
    private Record record;
    private SqliteController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_view);
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
        textTitle = (TextView) findViewById(R.id.textTitleValue);
        textCreation = (TextView) findViewById(R.id.textCreationValue);
        textModification = (TextView) findViewById(R.id.textModificationValue);
        textComment      = (TextView) findViewById(R.id.textCommentValue);
    }

    private void initFields() {
        String filePath = this.getIntent().getStringExtra("path");
        recordFile = new File(filePath);

        record = dbController.selectRecord(filePath);
        if(record!=null) {
            textTitle.setText(record.title);
            textCreation.setText(record.creationDate);
            textModification.setText(record.modificationDate);
            textComment.setText(record.comment);
        } else {
            textTitle.setText("");
            textCreation.setText("");
            textModification.setText("");
            textComment.setText("");
        }
    }

    private void setActionButton() {
        Button modify = (Button) findViewById(R.id.modifyButton);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(InformationActivity.this, EditActivity.class);
                edit.putExtra("path", recordFile.getAbsolutePath());
                startActivity(edit);
                finish();
            }
        });
    }
}
