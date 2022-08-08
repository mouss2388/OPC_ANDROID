package com.example.projet_7.ui.adapter;

import static com.example.projet_7.utils.Utils.getPictureCroppedWithGlide;

import android.content.res.Resources;
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

public class DetailWorkmateAdapter extends RecyclerView.Adapter<DetailWorkmateAdapter.ViewHolder> {

    private final ArrayList<User> users;

    public DetailWorkmateAdapter(ArrayList<User> user) {
        this.users = user;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.displayWorkmates(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView picture;
        private final TextView sentence;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.picture);
            sentence = itemView.findViewById(R.id.sentence);


        }

        public void displayWorkmates(User user) {

            getPictureCroppedWithGlide(itemView.getContext(), user.getUrlPicture(), picture);
            StringBuilder text = new StringBuilder().append(user.getUsername().split(" ")[0]).append(" ").append( itemView.getResources().getString(R.string.joining));
            sentence.setText(text);
        }

    }
}
