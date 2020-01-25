package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.AddFavoriteEvent;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourEvent;
import com.openclassrooms.entrevoisins.events.RemoveFavoriteEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import com.openclassrooms.entrevoisins.utils.ItemClickSupport;

import java.util.Iterator;
import java.util.List;

import static com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity.SAVED_FAVORITE_LIST;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity.mApiService;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity.FAVORITE_NAMES;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity.sharedPreferences;


public class FavoriteFragment extends Fragment
{
    private List<Neighbour> mFavorites;
    private RecyclerView mRecyclerView;
    private MyNeighbourRecyclerViewAdapter mAdapter;
    private FavoriteFragmentCallback fragmentCaller;

    /**
     * Create and return a new instance
     * @return @{@link NeighbourFragment}
     */
    public static FavoriteFragment newInstance() {
        FavoriteFragment fragment = new FavoriteFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //On attache ici une interface de Callback, que l'activité container doit redéfinir

        fragmentCaller = (FavoriteFragmentCallback) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = DI.getNeighbourApiService();
        mApiService.initFavorites();

        /*
            Chargement des favoris à partir du fichier SAVED_FAVORITE_LIST
         */

        sharedPreferences = getContext().getSharedPreferences(SAVED_FAVORITE_LIST, getContext().MODE_PRIVATE);

        for(int i=0;i<12;i++)
            if (sharedPreferences.contains(FAVORITE_NAMES[i])) {
                mApiService.addFavorite(mApiService.getNeighbour(sharedPreferences.getInt(FAVORITE_NAMES[i], 0)));
                sharedPreferences.edit().remove(FAVORITE_NAMES[i]).commit();
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        this.configureOnClickRecyclerView();

        initList();
        return view;
    }

    /**
     * Init the List of favorites
     */
    private void initList() {
        mFavorites = mApiService.getFavorites();
        mAdapter = new MyNeighbourRecyclerViewAdapter(mFavorites);
        mRecyclerView.setAdapter(mAdapter);
    }

    //A la création des fragments, on vérifie que DetailNeighbourActivity n'a pas émis une demande d'ajout en favoris

    @Override
    public void onStart() {
        super.onStart();

        /*
            On souscrit à l'EventBus pour écouter AddFavorite et RemoveFavorite émis depuis DetailNeighbourActivity
            On vérifie s'il n'y a pas des event en cache
            On les enlève ensuite de EventBus quand ils ont été traité
         */
        EventBus.getDefault().register(this);
        AddFavoriteEvent addedFavorite = EventBus.getDefault().getStickyEvent(AddFavoriteEvent.class);
        RemoveFavoriteEvent removedFavorite = EventBus.getDefault().getStickyEvent(RemoveFavoriteEvent.class);

        //Traitement des events générés dans DetailActivityNeighbour
        //On vérifie que le voisin ne soit pas déjà en favori, avant de mettre à jour la liste

        if ((addedFavorite != null) && !mApiService.containsFavorite(addedFavorite.neighbour)) {
            onAddFavorite(addedFavorite);
            EventBus.getDefault().removeStickyEvent(addedFavorite);
        }
        if ((removedFavorite != null)) {
           onRemoveFavorite(removedFavorite);
           EventBus.getDefault().removeStickyEvent(removedFavorite);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * Fired if the user clicks on a delete button
     * @param event
     */
    @Subscribe
    public void onDeleteFavorite(DeleteNeighbourEvent event) {
        mApiService.deleteNeighbour(event.neighbour);
        mApiService.removeFavorite(event.neighbour);
        EventBus.getDefault().removeStickyEvent(RemoveFavoriteEvent.class);
        sharedPreferences.edit().remove(FAVORITE_NAMES[event.neighbour.getId()-1]).commit();
        initList();
    }

    //Fonction implémentant l'ajout d'un favori à la liste, en réponse à l'événement émis depuis DetailNeighbourActivity

    @Subscribe
    public void onAddFavorite(AddFavoriteEvent event) {
        mApiService.addFavorite(event.neighbour);
        EventBus.getDefault().removeStickyEvent(AddFavoriteEvent.class);
        initList();
    }

    //Fonction implémentant la sortie d'un favori de la liste, en réponse à l'événement émis depuis DetailNeighbourActivity

    @Subscribe
    public void onRemoveFavorite(RemoveFavoriteEvent event) {
        mApiService.removeFavorite(event.neighbour);
        EventBus.getDefault().removeStickyEvent(RemoveFavoriteEvent.class);
        sharedPreferences.edit().remove(FAVORITE_NAMES[event.neighbour.getId()-1]).commit();
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

    public interface FavoriteFragmentCallback {
        public void onNeighbourSelected(Neighbour cNeighbour);
    }

    //fonction concrète que le container doit implémenter

    public void onNeighbourSelected(Neighbour cNeighbour){
        fragmentCaller.onNeighbourSelected(cNeighbour);
    }

}
