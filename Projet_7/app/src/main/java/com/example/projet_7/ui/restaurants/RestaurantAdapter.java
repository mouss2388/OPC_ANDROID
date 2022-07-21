package com.example.projet_7.ui.restaurants;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_7.R;
import com.example.projet_7.model.Restaurant;
import com.example.projet_7.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private final ArrayList<Restaurant> restaurants;

    public RestaurantAdapter(ArrayList<Restaurant> restaurants) {
        this.restaurants = (ArrayList<Restaurant>) restaurants;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.ViewHolder holder, int position) {

        holder.displayRestaurant(restaurants.get(position));
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView address;
        private final TextView type;
        private final TextView opening_hours;
        private final ImageView photo;
        private final TextView distance;
        private final TextView total_rating;



        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            name =  view.findViewById(R.id.name);
            address= view.findViewById(R.id.address);
            type =  view.findViewById(R.id.type_Of_Restaurant);
            opening_hours =  view.findViewById(R.id.opening_hours);
            distance =  view.findViewById(R.id.distance_to_restaurant);
            total_rating = view.findViewById(R.id.total_rating);
            photo = view.findViewById(R.id.photo_restaurant);

        }

        public void displayRestaurant(Restaurant restaurant) {
            name.setText(restaurant.getName());
            address.setText(restaurant.getAddress());
        }
    }






}
