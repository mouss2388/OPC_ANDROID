package com.openclassrooms.realestatemanager.adapters;

import static com.openclassrooms.realestatemanager.utils.Utils.castDoubleToInt;
import static com.openclassrooms.realestatemanager.utils.Utils.concatStrWithoutSpace;
import static com.openclassrooms.realestatemanager.utils.Utils.convertToString;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.database.model.RealEstate;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewModel.RealEstateViewModel;

import java.util.List;

public class RealEstateAdapter extends RecyclerView.Adapter<RealEstateAdapter.ViewHolder> {

    private static List<RealEstate> mRealEstates = null;
    public OnRealEstateListener onRealEstateListener;
    private final RealEstateViewModel realEstateViewModel;
    private final LifecycleOwner lifecycleOwner;


    public RealEstateAdapter(List<RealEstate> realEstates, OnRealEstateListener onRealEstateListener, LifecycleOwner lifecycleOwner, RealEstateViewModel realEstateViewModel) {
        this.onRealEstateListener = onRealEstateListener;
        mRealEstates = realEstates;
        this.realEstateViewModel = realEstateViewModel;
        this.lifecycleOwner = lifecycleOwner;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_real_estate, parent, false);

        return new ViewHolder(view, onRealEstateListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RealEstateAdapter.ViewHolder holder, int position) {
        holder.displayRealEstate(mRealEstates.get(position), this.lifecycleOwner, this.realEstateViewModel);
    }

    @Override
    public int getItemCount() {
        if (mRealEstates != null) {
            return mRealEstates.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnRealEstateListener onRealEstateListener;
        private final TextView type;
        private final TextView address;
        private final TextView price;
        private final ImageView photo;

        public ViewHolder(View view, OnRealEstateListener onRealEstateListener) {
            super(view);

            this.onRealEstateListener = onRealEstateListener;
            // Define click listener for the ViewHolder's View
            type = view.findViewById(R.id.type_realestate);
            address = view.findViewById(R.id.address_realestate);
            price = view.findViewById(R.id.price_realestate);
            photo = view.findViewById(R.id.image);
            view.setOnClickListener(this);
        }

        public void displayRealEstate(RealEstate realEstate, LifecycleOwner lifecycleOwner, RealEstateViewModel realEstateViewModel) {

            type.setText(realEstate.getTypeRealEstate());
            address.setText(realEstate.getAddress());

            String priceStr = convertToString(castDoubleToInt(realEstate.getPrice()));
            price.setText(concatStrWithoutSpace(priceStr, "$"));

            realEstateViewModel.getRealEstateImages(realEstate).observe(lifecycleOwner, images -> {
                if (images.size() > 0) {
                    Utils.setRealEstatePicture(itemView.getContext(), images.get(0).getUrl(), photo);
                }
            });


        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            long id = mRealEstates.get(index).getId();
            onRealEstateListener.onRealEstateClick(id);
        }
    }

    public interface OnRealEstateListener {
        void onRealEstateClick(long id);
    }
}
