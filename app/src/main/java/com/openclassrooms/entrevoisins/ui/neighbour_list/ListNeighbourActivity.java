package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.model.Neighbour;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListNeighbourActivity extends AppCompatActivity implements NeighbourFragment.NeighbourFragmentCallback
{

    // UI Components
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.container)
    ViewPager mViewPager;


    ListNeighbourPagerAdapter mPagerAdapter;
    public final static String MESSAGE_TO_DETAIL_ACTIVITY = "com.openclassrooms.entrevoisins.ui.neighbour_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_neighbour);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mPagerAdapter = new ListNeighbourPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public void onNeighbourSelected(Neighbour cNeighbour) {
        Intent detailNeighbour = new Intent(this, DetailNeighbourActivity.class);

        // On transmet l'Id du neighbour sélectionné dans la liste, à l'activité DetailNeighbourActivity, sous forme de String
        detailNeighbour.putExtra(MESSAGE_TO_DETAIL_ACTIVITY,Integer.toString(cNeighbour.getId()));
        startActivity(detailNeighbour);
    }
}
