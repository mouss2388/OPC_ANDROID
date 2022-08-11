package com.example.projet_7.ui.workmates;

import static com.example.projet_7.utils.Utils.concatFirstnameAndSentence;
import static com.example.projet_7.utils.Utils.getPictureCroppedWithGlide;
import static com.example.projet_7.utils.Utils.startDetailActivity;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_7.R;
import com.example.projet_7.model.Restaurant;
import com.example.projet_7.model.User;

import java.util.ArrayList;

public class WorkMateAdapter extends RecyclerView.Adapter<WorkMateAdapter.ViewHolder> {

    private final ArrayList<User> workMates;
    private final ArrayList<Restaurant> restaurantsBooked;

    public WorkMateAdapter(ArrayList<User> workMates, ArrayList<Restaurant> restaurantsBooked) {
        this.workMates = workMates;
        this.restaurantsBooked = restaurantsBooked;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workmate, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkMateAdapter.ViewHolder holder, int position) {
        holder.displayWorkwates(workMates.get(position), restaurantsBooked);

    }

    @Override
    public int getItemCount() {
        return workMates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView sentence;
        private final ImageView picture;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            sentence = view.findViewById(R.id.sentence);
            picture = view.findViewById(R.id.picture);
            view.setOnClickListener(v -> {
                int index = getAdapterPosition();
                Context context = v.getContext();
                String id = workMates.get(index).getRestaurantBookedId();
                if (!id.isEmpty()) {
                    startDetailActivity(context, id);
                }
            });
        }

        public void displayWorkwates(User workmate, ArrayList<Restaurant> restaurantsBooked) {

            String str = itemView.getResources().getString(R.string.eating_workmates);
            if (workmate.getRestaurantBookedId().isEmpty()) {

                sentence.setText(itemView.getResources().getString(R.string.workmates_not_decided));
                sentence.setTextColor(itemView.getResources().getColor(R.color.grey));
                sentence.setTypeface(null,Typeface.ITALIC);
            } else {

                for (Restaurant restaurant : restaurantsBooked) {
                    if (workmate.getRestaurantBookedId().equals(restaurant.getId())) {
                        StringBuilder textComplete = concatFirstnameAndSentence(workmate, str);
                        textComplete = textComplete.append(" ").append(restaurant.getName());
                        sentence.setText(textComplete);
                        sentence.setTextColor(itemView.getResources().getColor(R.color.black));
                        sentence.setTypeface(null,Typeface.NORMAL);

                    }
                }
            }
            getPictureCroppedWithGlide(itemView.getContext(), workmate.getUrlPicture(), picture);
        }
    }
}
