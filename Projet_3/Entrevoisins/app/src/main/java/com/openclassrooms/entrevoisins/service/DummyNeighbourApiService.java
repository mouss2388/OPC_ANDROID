package com.openclassrooms.entrevoisins.service;

import static com.openclassrooms.entrevoisins.controller.ListNeighbourActivity.FLAG_LOG;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dummy mock for the Api
 */
public class DummyNeighbourApiService implements NeighbourApiService {

    private final List<Neighbour> neighbours = DummyNeighbourGenerator.generateNeighbours();


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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            // only for gingerbread and newer versions
            return getNeighbours().stream().filter(Neighbour::isFavorite).collect(Collectors.toList());
        } else {
            ArrayList<Neighbour> neighboursFav = new ArrayList<>();
            for (Neighbour neighbour : getNeighbours()) {
                if (neighbour.isFavorite()) {
                    neighboursFav.add(neighbour);
                }
            }
            return neighboursFav;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Neighbour getNeighbourById(long id) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return getNeighbours().stream()
                    .filter(n -> n.getId() == id).findFirst().orElse(null);
        } else {
            for (Neighbour neighbour : getNeighbours()) {
                if (neighbour.getId() == id) {
                    return neighbour;
                }
            }
        }
        return null;
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
