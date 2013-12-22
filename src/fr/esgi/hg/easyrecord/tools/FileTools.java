package fr.esgi.hg.easyrecord.tools;

import android.media.AudioFormat;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileTools {

    public static final String DIRECTORY = "EasyRecorder" ;
    public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    public static File getAppDirectory(){
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+DIRECTORY);
    }

    public static String getNewRecordName(){
        return formatter.format(new Date())+"_Record";
    }

    public static File getNewRecordFile(){
        File dir = getAppDirectory();

        File f = new File(dir, getNewRecordName());

        if(!dir.exists())
            dir.mkdirs();

        return f;
    }

    public static ArrayList<File> getAllRecords(){
        ArrayList<File> records = new ArrayList<File>();

        File dir = getAppDirectory();

        if(!dir.exists()) return records;

        for(File f : dir.listFiles()){
            if(!f.isDirectory())
                records.add(f);
        }

        return records;
    }

    public static void delete(File f){
        if(null == f || !f.exists() || f.isDirectory())
            return;

        f.delete();
    }
}
