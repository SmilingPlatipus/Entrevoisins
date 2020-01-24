package com.openclassrooms.entrevoisins.events;

import com.openclassrooms.entrevoisins.model.Neighbour;

public class AddFavoriteEvent
{

    /**
     * Neighbour to add to favorites
     */
    public Neighbour neighbour;

    /**
     * Constructor.
     * @param neighbour
     */
    public AddFavoriteEvent(Neighbour neighbour) {
        this.neighbour = neighbour;
    }
}
