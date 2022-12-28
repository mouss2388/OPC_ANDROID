package com.openclassrooms.realestatemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.database.model.RealEstate;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.List;

public class RealEstateAdapter extends RecyclerView.Adapter<RealEstateAdapter.ViewHolder> {

    private static List<RealEstate> mRealEstates = null;
    //    private static RealEstateListFragment mRealEstateListFragment;
    public OnRealEstateListener onRealEstateListener;


    public RealEstateAdapter(List<RealEstate> realEstates, OnRealEstateListener onRealEstateListener) {
        this.onRealEstateListener = onRealEstateListener;
        mRealEstates = realEstates;
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
        holder.displayRealEstate(mRealEstates.get(position));
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
        //        private final ImageView photo;

        public ViewHolder(View view, OnRealEstateListener onRealEstateListener) {
            super(view);

            this.onRealEstateListener = onRealEstateListener;
            // Define click listener for the ViewHolder's View
            type = view.findViewById(R.id.type_realestate);
            address = view.findViewById(R.id.address_realestate);
            price = view.findViewById(R.id.price_realestate);
            view.setOnClickListener(this);
        }

        public void displayRealEstate(RealEstate realEstate) {
//
            type.setText(realEstate.getTypeRealEstate().toString());
            address.setText(realEstate.getAddress());
            price.setText(String.valueOf(Utils.castDoubleToInt(realEstate.getPrice())));


        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            Context context = v.getContext();
            long id = mRealEstates.get(index).getId();
            onRealEstateListener.onRealEstateClick(id);
            Toast.makeText(context, "id: " + id, Toast.LENGTH_SHORT).show();


        }


    }

    public interface OnRealEstateListener {
        void onRealEstateClick(long id);
    }
}
