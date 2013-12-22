package fr.esgi.hg.easyrecord;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import fr.esgi.hg.easyrecord.tools.FileTools;
import fr.esgi.hg.easyrecord.widgets.MiniPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayerFragment extends Fragment {

    private MiniPlayer player;
    private ListView recordsList;

    private ArrayAdapter<File> recordsAdapter;
    private ArrayList<File> records;

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

        recordsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });



		return rootView;
	}

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    private void initialize(){
        records.clear();
        records.addAll(FileTools.getAllRecords());

    }

    public void uddateRecords(){
        initialize();
    }
}
