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
import android.widget.Toast;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import com.openclassrooms.entrevoisins.utils.ItemClickSupport;

import java.util.List;

import static com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity.mApiService;


public class FavoriteFragment extends Fragment
{
    private List<Neighbour> mNeighbours;
    private RecyclerView mRecyclerView;
    private MyNeighbourRecyclerViewAdapter mAdapter;
    private FavoriteFragmentCallback fragmentCaller;

    //neighbour test
    private Neighbour favoriteTest = new Neighbour(1, "Caroline", "http://i.pravatar.cc/150?u=a042581f4e29026704d");


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
        //test

        mApiService.addFavorite(favoriteTest);

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

                        //Toast.makeText(getContext(), "You clicked on user : "+currentNeighbour.getName(), Toast.LENGTH_SHORT).show();

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
