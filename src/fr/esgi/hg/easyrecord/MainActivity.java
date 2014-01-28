package fr.esgi.hg.easyrecord;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import fr.esgi.hg.easyrecord.adapter.TabsPagerAdapter;

import java.io.File;


/**
 * TODO :
 * Recuperer les informations du title dans le listing
 * Mettres des poids sur la vue de Record
 *Mettre l'action de record sur le bouton et sur l'image
 * (Changer le text du bouton "Record" en "Stop" lors du record)
 * Supprimer le record de la base a l'appui sur le bouton Delete
 *
 *
 * Refaire une revue rapide des layouts
 *
 */
public class MainActivity extends FragmentActivity implements
        ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter tabsAdapter;
    private ActionBar actionBar;

    private PlayerFragment player;
    private RecorderFragment recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();

        tabsAdapter = new TabsPagerAdapter(fragmentManager);

        viewPager.setAdapter(tabsAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (int i = 0; i < tabsAdapter.getCount(); ++i) {
            actionBar.addTab(actionBar.newTab().setText(tabsAdapter.getTabsName(i)).setTabListener(this));
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        //retrieve last created fragment instance from the adapter
        //and listen for sound recorded in order to update the player's list
        tabsAdapter.setOnfragmentInstiated(new TabsPagerAdapter.OnFragmentInstantiated() {
            @Override
            public void execute(int index, Fragment frag) {
                switch (index){
                    case 0:
                        recorder = (RecorderFragment) frag;
                        break;
                    case 1:
                        player = (PlayerFragment) frag;
                        break;
                }

                if(null != recorder && null != player){
                    recorder.setOnSoundRecorded(new RecorderFragment.recordHandler() {
                        @Override
                        public void onSoundRecorded(File file) {
                            player.updateRecords();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

}
