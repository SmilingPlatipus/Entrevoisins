package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyNeighbourApiService implements NeighbourApiService
{

    private List<Neighbour> neighbours;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Neighbour> getNeighbours() {
        return neighbours;
    }

    @Override
    public List<Neighbour> getFavorites() {
        Iterator nIterator = neighbours.iterator();
        List<Neighbour> favorites = new ArrayList<>();
        Neighbour currentNeighbour;
        do{
            currentNeighbour = (Neighbour) nIterator.next();
            if (currentNeighbour.isFavorite())  favorites.add(currentNeighbour);
        }while (nIterator.hasNext());
        return favorites;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNeighbour(Neighbour neighbour) { neighbours.remove(neighbour);    }

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
    public void addToFavorite(int ID) {
        getNeighbour(ID).setFavorite(true);
    }

    @Override
    public void removeFromFavorite(int ID) {
        getNeighbour(ID).setFavorite(false);
    }


}



