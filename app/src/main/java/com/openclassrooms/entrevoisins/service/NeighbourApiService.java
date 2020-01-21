package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.List;


/**
 * Neighbour API client
 */
public interface NeighbourApiService
{


    /**
     * Get all my Neighbours
     *
     * @return {@link List}
     */
    List<Neighbour> getNeighbours();

    /**
     * Deletes a neighbour
     *
     * @param neighbour
     */
    void deleteNeighbour(Neighbour neighbour);

    void addNeighbour(Neighbour neighbour);

    void initNeighbours();

    void initFavorites();

    Neighbour getNeighbour(int ID);

    List<Neighbour> getFavorites();


    void deleteFavorite(Neighbour favorite);

    void addFavorite(Neighbour favorite);


    Neighbour getFavorite(int ID);
}
