package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import java.util.Iterator;
import java.util.List;

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
    public final static String SELECTED_NEIGHBOUR = "Selected neighbour to DetailActivity";
    public static NeighbourApiService mApiService;
    public static SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_neighbour);
        ButterKnife.bind(this);


        setSupportActionBar(mToolbar);
        mPagerAdapter = new ListNeighbourPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(NeighbourFragment.newInstance(false), "Neighbours");
        mPagerAdapter.addFragment(NeighbourFragment.newInstance(true),"Favorites");
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onNeighbourSelected(Neighbour cNeighbour) {
        Intent detailNeighbour = new Intent(ListNeighbourActivity.this, DetailNeighbourActivity.class);

        // On transmet l'ID du neighbour sélectionné dans la liste, à l'activité DetailNeighbourActivity

        detailNeighbour.putExtra(SELECTED_NEIGHBOUR,cNeighbour.getId());
        startActivity(detailNeighbour);
    }
}
