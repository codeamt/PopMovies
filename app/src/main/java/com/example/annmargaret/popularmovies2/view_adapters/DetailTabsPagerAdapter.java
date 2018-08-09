package com.example.annmargaret.popularmovies2.view_adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.annmargaret.popularmovies2.views_ui.FragmentInfoTab;
import com.example.annmargaret.popularmovies2.views_ui.FragmentReviewsTab;
import com.example.annmargaret.popularmovies2.views_ui.FragmentTrailerTab;

public class DetailTabsPagerAdapter extends FragmentStatePagerAdapter {
    /* Instantiate Vars */
    private int mNumOfTabs;


    /* Adapter Constructor */
    public DetailTabsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }



    /* Overrides */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentInfoTab tab1 = new FragmentInfoTab();
                return tab1;
            case 1:
                FragmentTrailerTab tab2 = new FragmentTrailerTab();
                return tab2;
            case 2:
                FragmentReviewsTab tab3 = new FragmentReviewsTab();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }


}
