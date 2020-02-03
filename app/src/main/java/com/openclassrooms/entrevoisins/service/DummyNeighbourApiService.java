package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.Iterator;
import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyNeighbourApiService implements NeighbourApiService
{

    private List<Neighbour> neighbours;
    private List<Neighbour> favorites;


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Neighbour> getNeighbours() {
        return neighbours;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNeighbour(Neighbour neighbour) {
        neighbours.remove(neighbour);
    }

    @Override
    public Neighbour getNeighbour(int ID) {
        if (neighbours.isEmpty()) return null;
        for (int i=0;i<neighbours.size();i++)
            if (neighbours.get(i).getId() == ID)
                return neighbours.get(i);

        return null;
    }

    @Override
    public void initNeighbours() {
            neighbours = DummyNeighbourGenerator.generateNeighbours();
    }

    @Override
    public void initFavorites() {
        favorites = DummyNeighbourGenerator.generateFavorites();    }

    @Override
    public List<Neighbour> getFavorites() {
        return favorites;
    }

    @Override
    public void removeFavorite(Neighbour favorite) {
        favorites.remove(favorite);
    }

    @Override
    public void addFavorite(Neighbour favorite) {
        favorites.add(favorite);
    }

    @Override
    public boolean containsFavorite(Neighbour neighbour) {
        if (favorites.contains(neighbour))
        return true;
        else return false;
    }

}



