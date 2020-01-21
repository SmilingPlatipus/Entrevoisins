package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.model.Neighbour;

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
    public void addNeighbour(Neighbour neighbour) {
        neighbours.add(neighbour);
    }

    @Override
    public void initNeighbours() {
        neighbours = DummyNeighbourGenerator.generateNeighbours();
    }

    @Override
    public void initFavorites() {
        favorites = DummyNeighbourGenerator.generateFavorites();
    }

    @Override
    public Neighbour getNeighbour(int ID) {
        return neighbours.get(ID);
    }

    @Override
    public List<Neighbour> getFavorites() {
        return favorites;
    }

    @Override
    public void deleteFavorite(Neighbour favorite) {
        favorites.remove(favorite);
    }

    @Override
    public void addFavorite(Neighbour favorite) {
        favorites.add(favorite);
    }

    @Override
    public Neighbour getFavorite(int ID) {
        return favorites.get(ID);
    }


}
