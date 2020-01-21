package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class ListNeighbourPagerAdapter extends FragmentPagerAdapter {


    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String>  mFragmentTitleList = new ArrayList<>();

    public ListNeighbourPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment mFragment,String title){
        mFragmentList.add(mFragment);
        mFragmentTitleList.add(title);
    }

    /**
     * getItem is called to instantiate the fragment for the given page.
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    /**
     * get the number of pages
     * @return
     */
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}