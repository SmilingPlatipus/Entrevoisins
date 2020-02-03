package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import com.openclassrooms.entrevoisins.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import static com.openclassrooms.entrevoisins.ui.neighbour_list.DetailNeighbourActivity.FAVORITE_NAMES;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.DetailNeighbourActivity.SAVED_FAVORITE_LIST;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity.mApiService;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity.sharedPreferences;


public class NeighbourFragment extends Fragment
{
    private List<Neighbour> mNeighbours = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private MyNeighbourRecyclerViewAdapter mAdapter;
    private NeighbourFragmentCallback fragmentCaller;
    private boolean favoriteFilter;


    /**
     * Create and return a new instance
     * @return @{@link NeighbourFragment}
     */
    public static NeighbourFragment newInstance(boolean fFilter) {
        NeighbourFragment fragment = new NeighbourFragment();
        fragment.favoriteFilter = fFilter;
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //On attache ici une interface de Callback, que l'activité container doit redéfinir
        fragmentCaller = (NeighbourFragmentCallback) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = DI.getNeighbourApiService();
        mApiService.initNeighbours();

        if (favoriteFilter)
        {
            /*
            Chargement des favoris à partir du fichier SAVED_FAVORITE_LIST
         */

            sharedPreferences = getContext().getSharedPreferences(SAVED_FAVORITE_LIST, getContext().MODE_PRIVATE);

            for(int i=0;i<12;i++)
                if (sharedPreferences.contains(FAVORITE_NAMES[i])) {
                    mApiService.addToFavorite(i + 1);
                    mNeighbours.add(mApiService.getNeighbour(i+1));
                }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neighbour_list, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        this.configureOnClickRecyclerView();

        initList();
        return view;
    }

    /**
     * Init the List of neighbours or the list of favorites, depending on favoritefilter
     */
    private void initList() {
        if (!favoriteFilter)
            mNeighbours = mApiService.getNeighbours();
        else
            mNeighbours = mApiService.getFavorites();

        mAdapter = new MyNeighbourRecyclerViewAdapter(mNeighbours);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        initList();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Fired if the user clicks on a delete button
     * @param event
     */


    @Subscribe
    public void onDeleteNeighbour(DeleteNeighbourEvent event) {
        sharedPreferences.edit().remove(FAVORITE_NAMES[event.neighbour.getId()-1]).commit();
        mApiService.deleteNeighbour(event.neighbour);


        initList();
    }


    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_neighbour_list)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        //On récupère le voisin courant par la position cliquée

                        Neighbour currentNeighbour = mAdapter.getNeighbour(position);

                        //Si un des voisins est sélectionné, exécute cette fonction callback implémentée par le container
                        onNeighbourSelected(currentNeighbour);
                    }
                });
    }

//interface de Callback que le container doit obligatoirement redéfinir

    public interface NeighbourFragmentCallback {
        public void onNeighbourSelected(Neighbour cNeighbour);
    }

    //fonction concrète que le container doit implémenter

    public void onNeighbourSelected(Neighbour cNeighbour){
        fragmentCaller.onNeighbourSelected(cNeighbour);
    }

    }
