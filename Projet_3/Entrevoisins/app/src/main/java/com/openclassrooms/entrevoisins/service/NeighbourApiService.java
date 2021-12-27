package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.List;


/**
 * Neighbour API client
 */
public interface NeighbourApiService {

    /**
     * Get all my Neighbours
     *
     * @return {@link List}
     */
    List<Neighbour> getNeighbours();

    List<Neighbour> getNeighboursFav();

    /**
     * Get Neighbour by Id
     *
     * @param id
     * @return {Neighbour}
     */
    Neighbour getNeighbourById(long id);


    /**
     * Deletes a neighbour
     *
     * @param neighbour
     */
    void deleteNeighbour(Neighbour neighbour);

    void deleteFavNeighbour(Neighbour neighbour);


    /**
     * Create a neighbour
     *
     * @param neighbour
     */
    void createNeighbour(Neighbour neighbour);

    void addNeighbourToFav(Neighbour neighbour);

}
