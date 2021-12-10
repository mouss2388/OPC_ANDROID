package com.openclassrooms.entrevoisins.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.openclassrooms.entrevoisins.activity.DetailNeighbourActivity;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class NeighbourFavServiceTest {

    private NeighbourApiService service;

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
    }


    @Test
    public void addNeighbourToFavList() {
        Neighbour neighbour = service.getNeighbours().get(0);
        service.addNeighbourToFav(neighbour);
        assertEquals(service.getNeighboursFav().size(), 1);
    }

    @Test
    public void getNeighbourFavorite() {
        Neighbour neighbour = service.getNeighbours().get(0);
        service.addNeighbourToFav(neighbour);
        assertEquals(service.getNeighboursFav().get(0), neighbour);

    }

    @Test
    public void deleteNeighbourFavList() {
        assertTrue(service.getNeighboursFav().isEmpty());

        Neighbour neighbour = service.getNeighbours().get(0);
        service.addNeighbourToFav(neighbour);

        assertEquals(service.getNeighboursFav().size(), 1);

        service.deleteFavNeighbour(neighbour);
        assertEquals(service.getNeighboursFav().size(), 0);
    }

}
