package com.example.projet_7.ui.adapters;

import static com.example.projet_7.utils.Utils.convertPurcentageToRating;
import static com.example.projet_7.utils.Utils.convertRatingToPurcentage;
import static com.example.projet_7.utils.Utils.getBitmapWithGlide;
import static com.example.projet_7.utils.Utils.getIndexDayOfWeek;
import static com.example.projet_7.utils.Utils.startDetailActivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_7.R;
import com.example.projet_7.model.Restaurant;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private final ArrayList<Restaurant> restaurants;

    public RestaurantAdapter(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;

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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView address;
        private final AppCompatRatingBar stars_rating;
        private final TextView total_rating;
        private final TextView opening_hours;
        private final ImageView photo;
        private final TextView distance;


        public ViewHolder(View view) {
            super(view);

            // Define click listener for the ViewHolder's View
            name = view.findViewById(R.id.name_Restaurant);
            address = view.findViewById(R.id.address_Restaurant);
            stars_rating = view.findViewById(R.id.stars_rating_restaurant);
            total_rating = view.findViewById(R.id.total_rating_restaurant);
            opening_hours = view.findViewById(R.id.opening_hour_Restaurant);
            photo = view.findViewById(R.id.photo_restaurant);
            distance = view.findViewById(R.id.distance_to_restaurant);
            view.setOnClickListener(v -> {
                int index = getAbsoluteAdapterPosition();
                Context context = v.getContext();
                String id = restaurants.get(index).getId();
                startDetailActivity(context, id);

            });
        }

        public void displayRestaurant(Restaurant restaurant) {

            name.setText(restaurant.getName());
            address.setText(restaurant.getAddress());

            double percentageRating = convertRatingToPurcentage(restaurant.getRating());
            float rating = convertPurcentageToRating(percentageRating);

            stars_rating.setRating(rating);

            String userRatingTotal = "(" + restaurant.getUserRatingTotal() + ")";
            total_rating.setText(userRatingTotal);

            setAndFormattingOpeningHours(restaurant);
            setPictureOfRestaurant(restaurant);

            String distance_str = restaurant.getDistance() != null ? restaurant.getDistance() : "";
            distance.setText(distance_str);
        }


        private void setAndFormattingOpeningHours(Restaurant restaurant) {

            int day = getIndexDayOfWeek();
            if (restaurant.getOpeningHours() == null) {
                opening_hours.setText("");
            } else {
                String[] hours = restaurant.getOpeningHours().getWeekdayText().get(day).split(": ");
                StringBuilder opening_hours_without_day = new StringBuilder();
                for (int i = 1; i < hours.length; i++) {
                    opening_hours_without_day.append(hours[i]);
                }
                opening_hours.setText(opening_hours_without_day);
            }
        }


        private void setPictureOfRestaurant(Restaurant restaurant) {
            if (restaurant.getPhoto() != null) {
                getBitmapWithGlide(itemView.getContext(), restaurant.getPhoto(), photo);
            } else {
                Drawable photoByDefault = ResourcesCompat.getDrawable(this.itemView.getResources(), R.drawable.ic_no_image_available, null);
                photo.setImageDrawable(photoByDefault);
            }
        }
    }
}
