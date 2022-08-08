package com.example.projet_7.ui.workmates;

import static com.example.projet_7.utils.Utils.concatFirstnameAndSentence;
import static com.example.projet_7.utils.Utils.getPictureCroppedWithGlide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_7.R;
import com.example.projet_7.model.User;

import java.util.ArrayList;

public class WorkMateAdapter extends RecyclerView.Adapter<WorkMateAdapter.ViewHolder> {

    private final ArrayList<User> workMates;

    public WorkMateAdapter(ArrayList<User> workMates) {
        this.workMates = workMates;
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
        holder.displayWorkwates(workMates.get(position));

    }

    @Override
    public int getItemCount() {
        return workMates.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView sentence;
        private final ImageView picture;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            sentence = view.findViewById(R.id.sentence);
            picture = view.findViewById(R.id.picture);
        }

        public void displayWorkwates(User workmate) {

            //TODO Update sentenceStr with restaurantBookedId if != null and inverse (see ternary condition)
            String str = itemView.getResources().getString(R.string.eating_workmates);
            StringBuilder textComplete = concatFirstnameAndSentence(workmate, str);
            sentence.setText(textComplete);

            getPictureCroppedWithGlide(itemView.getContext(), workmate.getUrlPicture(), picture);
        }
    }
}
