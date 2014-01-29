package fr.esgi.hg.easyrecord;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import fr.esgi.hg.easyrecord.tools.FileTools;
import fr.esgi.hg.easyrecord.tools.Record;
import fr.esgi.hg.easyrecord.tools.SqliteController;
import fr.esgi.hg.easyrecord.widgets.MiniPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerFragment extends Fragment {

    private MiniPlayer player;
    private ListView recordsList;

    private ArrayAdapter<File> recordsAdapter;
    private ArrayList<File> records;

    private HashMap<File, Record> fileToRecordMap;
    private SqliteController dbController;

    //TODO make resource
    //TODO ajouter l'option de tri !
    private static String[] menuItems = {"Edit", "View information", "Delete"};


    private File selectedRecord;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        dbController = new SqliteController(getActivity().getApplicationContext());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.    super.onDestroy();
        dbController.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.player_view, container, false);

        player = (MiniPlayer) rootView.findViewById(R.id.player);
        recordsList = (ListView) rootView.findViewById(R.id.recordsList);

        records = FileTools.getAllRecords();
        feedFileToRecord();

        recordsAdapter = customAdapter();
        recordsList.setAdapter(recordsAdapter);
        recordsList.setClickable(true);
        recordsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                player.setFile(records.get(i));
                player.play();
            }
        });

        //makes this fragment handle the menu on long press
        //of the record list (prevent from subclassing the listview)
        registerForContextMenu(recordsList);

        return rootView;
    }

    private void feedFileToRecord(){
        fileToRecordMap = new HashMap<File, Record>();

        for(File f : records){
            fileToRecordMap.put(f,dbController.selectRecord(f.getAbsolutePath()));
        }
    }

    private ArrayAdapter<File> customAdapter(){
        return new ArrayAdapter<File>(getActivity(), android.R.layout.simple_list_item_1, records) {

            LayoutInflater mInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                TextView text;
                if (convertView == null) {

                    view = mInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                } else {
                    view = convertView;
                }

                text = (TextView) view;

                Record record = fileToRecordMap.get(getItem(position));
                text.setText(null == record ? getItem(position).toString() : record.title );

                return view;
            }
        };
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (R.id.recordsList == v.getId()) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle((selectedRecord = records.get(info.position)).getName());

            for (int i = 0; i < menuItems.length; ++i) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                //edit
                Intent edit = new Intent(getActivity(), EditActivity.class);
                edit.putExtra("path", selectedRecord.getAbsolutePath());
                startActivity(edit);
                break;
            case 1:
                //info
                Intent info = new Intent(getActivity(), InformationActivity.class);
                info.putExtra("path", selectedRecord.getAbsolutePath());
                startActivity(info);
                break;
            case 2:
                //delete
                FileTools.delete(selectedRecord);
                recordsAdapter.remove(selectedRecord);
                dbController.deleteRecord(selectedRecord.getAbsolutePath());
                break;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRecords();
    }

    public void updateRecords() {
        recordsAdapter.clear();
        recordsAdapter.addAll(FileTools.getAllRecords());
        feedFileToRecord();
    }
}