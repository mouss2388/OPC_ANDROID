package com.openclassrooms.entrevoisins.activity;

import static com.openclassrooms.entrevoisins.activity.ListNeighbourActivity.FLAG_LOG;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.databinding.ActivityDetailNeighbourBinding;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

public class DetailNeighbourActivity extends AppCompatActivity {

    private NeighbourApiService mApiService;
    private String TAG = DetailNeighbourActivity.class.getSimpleName();
    private ActivityDetailNeighbourBinding binding;
    private Neighbour neighbour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailNeighbourBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();


        setContentView(view);

        mApiService = DI.getNeighbourApiService();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            int indexNeighbour = bundle.getInt("index_neighbour");
            boolean isFavorite = bundle.getBoolean("favorite_neighbour");
            Log.i(TAG, "NEIGHBOURS IS_FAVORITE: " + isFavorite);

            if (isFavorite) {
                if (FLAG_LOG) {
                    Log.i(TAG, "LIST FAV NEIGHBOURS: " + mApiService.getNeighboursFav());
                }
                //GET A NEIGHBOUR AT THE INDEX POSITION OF THE FAVORITE NEIGHBOUR LIST
                neighbour = mApiService.getNeighboursFav().get(indexNeighbour);
            } else {
                Log.i(TAG, "LIST NEIGHBOURS: " + mApiService.getNeighbours());
                //EITHER GET  A NEIGHBOUR AT THE INDEX POSITION  OF THE NEIGHBOUR LIST
                neighbour = mApiService.getNeighbours().get(indexNeighbour);
            }
            if (FLAG_LOG) {
                Log.i(TAG, "NEIGHBOUR INDEX: " + indexNeighbour);
                Log.i(TAG, "NEIGHBOUR DATA: " + neighbour);
            }
            setUserDetails();
            updateButtonFav();
            //ARROW BACK CLICKED
            binding.backArrow.setOnClickListener(v -> finish());

            binding.addFavorite.setOnClickListener(v -> {
                //CHANGE FAVORITE STATE
                neighbour.setFavorite(!neighbour.isFavorite());
                Log.i(TAG, "NEIGHBOUR DATA: " + neighbour);
                if (neighbour.isFavorite()) {
                    mApiService.addNeighbourToFav(neighbour);
                } else {
                    mApiService.deleteFavNeighbour(neighbour);
                }
                //MARK/UNMARK FAVORITE BUTTON
                updateButtonFav();
            });
        }

    }

    private void updateButtonFav() {
        if (neighbour.isFavorite()) {
            binding.addFavorite.setImageResource(R.drawable.ic_star_yellow_24);
        } else {
            binding.addFavorite.setImageResource(R.drawable.ic_star_border_yellow_24);
        }
    }

    private String selectedImg(String urlImg) {
        //GET DIMENSION DEVICE
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //CHOICE NEW WIDTH
        int newWidth = metrics.widthPixels / 3;
        //PARSE URL WITH GOOD WIDTH
        String newUrlImg = urlImg.replace("/150", "/" + newWidth);
        if (FLAG_LOG) {
            Log.i(TAG, "Img url origin: " + urlImg);
            Log.i(TAG, "Img url changes: " + newUrlImg);
        }
        return newUrlImg;
    }

    private void setUserDetails() {
        //GET IMG
        String urlImg = selectedImg(neighbour.getAvatarUrl());
        // SET IMAGE
        Glide.with(this).load(urlImg).into(binding.neighbourImg);

        //PARSE DATA
        String urlFacebook = getResources().getString(R.string.facebook);
        urlFacebook += neighbour.getName().toLowerCase();
        String address = neighbour.getAddress().replace(";", "à");

        //SET DATA
        binding.firstname.setText(neighbour.getName());
        binding.headerContact.setText(neighbour.getName());
        binding.address.setText(address);
        binding.tel.setText(neighbour.getPhoneNumber());
        binding.linkNetwork.setText(urlFacebook);
        binding.aboutMe.setText(neighbour.getAboutMe());

    }
}