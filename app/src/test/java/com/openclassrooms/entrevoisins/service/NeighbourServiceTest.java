package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Unit test on Neighbour service
 */
@RunWith(JUnit4.class)
public class NeighbourServiceTest
{

    private NeighbourApiService service;

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
        service.initNeighbours();
        service.initFavorites();
    }

    @Test
    public void getNeighboursWithSuccess() {
        List<Neighbour> neighbours = service.getNeighbours();
        List<Neighbour> expectedNeighbours = DummyNeighbourGenerator.DUMMY_NEIGHBOURS;
        assertThat(neighbours, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedNeighbours.toArray()));
    }

    @Test
    public void deleteNeighbourWithSuccess() {
        Neighbour neighbourToDelete = service.getNeighbours().get(0);
        service.deleteNeighbour(neighbourToDelete);
        assertFalse(service.getNeighbours().contains(neighbourToDelete));
    }

    @Test
    public void getNeighbourWithSuccess(){
        Neighbour neighbourToGet = DummyNeighbourGenerator.DUMMY_NEIGHBOURS.get(0);
        Neighbour neighbourThatWeGet = service.getNeighbour(0+1);
        assertEquals(neighbourToGet,neighbourThatWeGet);
    }

    @Test
    public void removeFavoriteWithSuccess() {
        Neighbour favoriteToRemove = DummyNeighbourGenerator.DUMMY_NEIGHBOURS.get(0);
        service.addFavorite(favoriteToRemove);
        service.removeFavorite(favoriteToRemove);
        List<Neighbour> favorites = service.getFavorites();
        assertFalse(favorites.contains(favoriteToRemove));
    }

    @Test
    public void containsFavoriteWorks(){
        Neighbour neighbourToGet = DummyNeighbourGenerator.DUMMY_NEIGHBOURS.get(0);
        assertTrue(!service.containsFavorite(neighbourToGet));
        service.addFavorite(neighbourToGet);
        assertTrue(service.containsFavorite(neighbourToGet));
    }

    @Test
    public void getFavoritesWithSuccess() {
        Neighbour favoriteToAdd = DummyNeighbourGenerator.DUMMY_NEIGHBOURS.get(0);
        List<Neighbour> fakeFavorites = new ArrayList<>();
        fakeFavorites.add(favoriteToAdd);
        service.addFavorite(favoriteToAdd);
        List<Neighbour> favorites = service.getFavorites();
        assertThat(favorites,IsIterableContainingInAnyOrder.containsInAnyOrder(fakeFavorites.toArray()));
        service.removeFavorite(favoriteToAdd);

    }


}
