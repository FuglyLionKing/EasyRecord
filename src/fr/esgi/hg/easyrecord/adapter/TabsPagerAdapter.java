package fr.esgi.hg.easyrecord.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import fr.esgi.hg.easyrecord.PlayerFragment;
import fr.esgi.hg.easyrecord.RecorderFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {
    private String[] tabsNames = { "Recorder", "Player"};

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

    public String getTabsName(int index){
        return tabsNames[index];
    }

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new RecorderFragment();
		case 1:
			return new PlayerFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		return tabsNames.length;
	}

}
