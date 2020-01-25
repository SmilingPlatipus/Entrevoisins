package com.openclassrooms.entrevoisins.events;

import com.openclassrooms.entrevoisins.model.Neighbour;

public class RemoveFavoriteEvent
{

    /**
     * Neighbour to remove from favorites
     */
    public Neighbour neighbour;

    /**
     * Constructor.
     * @param neighbour
     */
    public RemoveFavoriteEvent(Neighbour neighbour) {
        this.neighbour = neighbour;
    }
}