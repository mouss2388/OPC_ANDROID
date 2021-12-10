package com.openclassrooms.entrevoisins.service;

import static com.openclassrooms.entrevoisins.activity.ListNeighbourActivity.FLAG_LOG;

import android.util.Log;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyNeighbourApiService implements  NeighbourApiService {

    private List<Neighbour> neighbours = DummyNeighbourGenerator.generateNeighbours();
    private List<Neighbour> neighboursFav = DummyNeighbourGenerator.generateNeighboursFav();


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Neighbour> getNeighbours() {
        return neighbours;
    }
    @Override
    public List<Neighbour> getNeighboursFav() {
        return neighboursFav;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNeighbour(Neighbour neighbour) {
        if(FLAG_LOG){
            Log.i("deleteNeighbour : ", neighbour.toString());}

        deleteFavNeighbour(neighbour);
        neighbours.remove(neighbour);
    }

    @Override
    public void deleteFavNeighbour(Neighbour neighbour) {
        if(FLAG_LOG){
            Log.i("deleteFavNeighbour : ", neighbour.toString());};
        neighboursFav.remove(neighbour);
    }

    /**
     * {@inheritDoc}
     * @param neighbour
     */
    @Override
    public void createNeighbour(Neighbour neighbour) {
        neighbours.add(neighbour);
    }
    public void addNeighbourToFav(Neighbour neighbour) {
        neighboursFav.add(neighbour);
    }

}
