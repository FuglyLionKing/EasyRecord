package fr.esgi.hg.easyrecord;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import fr.esgi.hg.easyrecord.tools.FileTools;
import fr.esgi.hg.easyrecord.widgets.MiniPlayer;

import java.io.File;
import java.util.ArrayList;

public class PlayerFragment extends Fragment {

    private MiniPlayer player;
    private ListView recordsList;

    private ArrayAdapter<File> recordsAdapter;
    private ArrayList<File> records;

    //TODO make resource
    private static String[] menuItems = {"Edit", "View information", "Delete"};


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.player_view, container, false);

        player = (MiniPlayer) rootView.findViewById(R.id.player);
        recordsList = (ListView) rootView.findViewById(R.id.recordsList);

        records = FileTools.getAllRecords();

        //activity, view for items of list, list to display/react to
        recordsAdapter = new ArrayAdapter<File>(getActivity(),android.R.layout.simple_list_item_1, records);
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

private     File selectedRecord;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(R.id.recordsList == v.getId()){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle((selectedRecord = records.get(info.position)).getName());

            for(int i = 0; i < menuItems.length; ++i){
                menu.add(Menu.NONE, i,i,menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                //edit
                break;
            case 1:
                //info
                break;
            case 2:
                //delete
                FileTools.delete(selectedRecord);
//                records.remove(selectedRecord);
                recordsAdapter.remove(selectedRecord);
                break;

        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRecords();
    }


    public void updateRecords(){
//        records.clear();
//        records.addAll(FileTools.getAllRecords());
        recordsAdapter.clear();
        recordsAdapter.addAll(FileTools.getAllRecords());
    }


}
