package fr.esgi.hg.easyrecord.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import fr.esgi.hg.easyrecord.PlayerFragment;
import fr.esgi.hg.easyrecord.RecorderFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {
    //TODO make resource
    private String[] tabsNames = { "Recorder", "Player"};
    private OnFragmentInstantiated onFragmentInstantiated;

    public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

    public String getTabsName(int index){
        return tabsNames[index];
    }

	@Override
	public Fragment getItem(int index) {

        Fragment frag;

        switch (index) {
		case 0:
            frag = new RecorderFragment();
            if(null != onFragmentInstantiated)
                onFragmentInstantiated.execute(index,frag);
			return frag;
		case 1:
            frag = new PlayerFragment();
            if(null != onFragmentInstantiated)
                onFragmentInstantiated.execute(index,frag);
            return frag;
		}

		return null;
	}

	@Override
	public int getCount() {
		return tabsNames.length;
	}

    public void setOnfragmentInstiated(OnFragmentInstantiated fragmentInstantiated){
        this.onFragmentInstantiated = fragmentInstantiated;
    }

    public static interface OnFragmentInstantiated{
        void execute(int index, Fragment frag);
    }

}
