package com.openclassrooms.entrevoisins.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

/**
 * Unit test on Neighbour service
 */
@RunWith(JUnit4.class)
public class NeighbourServiceTest {

    private NeighbourApiService service;

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
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

    ////My Unit Tests//////

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
