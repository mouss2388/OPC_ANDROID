package com.openclassrooms.entrevoisins.service;

import static com.openclassrooms.entrevoisins.controller.ListNeighbourActivity.FLAG_LOG;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Dummy mock for the Api
 */
public class DummyNeighbourApiService implements NeighbourApiService {

    private List<Neighbour> neighbours = DummyNeighbourGenerator.generateNeighbours();


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Neighbour> getNeighbours() {
        return neighbours;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Neighbour> getNeighboursFav() {
        return getNeighbours().stream().filter(Neighbour::isFavorite).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Neighbour getNeighbourById(long id) {
        Neighbour neighbour = getNeighbours().stream()
                .filter(n -> n.getId() == id).findFirst().get();
        return neighbour;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNeighbour(Neighbour neighbour) {
        if (FLAG_LOG) {
            Log.i("deleteNeighbour : ", neighbour.toString());
        }

        neighbours.remove(neighbour);
    }


    /**
     * {@inheritDoc}
     *
     * @param neighbour
     */
    @Override
    public void createNeighbour(Neighbour neighbour) {
        neighbours.add(neighbour);
    }
}
