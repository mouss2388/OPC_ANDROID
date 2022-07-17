package com.example.projet_7.utils;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.projet_7.model.matrix_api.Distance;
import com.example.projet_7.model.matrix_api.Duration;
import com.example.projet_7.model.matrix_api.ElementsItem;
import com.example.projet_7.model.matrix_api.Response;
import com.example.projet_7.model.matrix_api.RowsItem;
import com.example.projet_7.service.matrix_api.MatrixApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Utils {

    public static void showSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public static void getDistanceBetween(Context contex, StringBuilder currentLocationLatLng, StringBuilder destinationLatLng) {
        Call<Response> call = MatrixApiClient.matrixApiService().getResult(currentLocationLatLng, destinationLatLng);

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(@NonNull Call<Response> call, @NonNull retrofit2.Response<Response> response) {
                if (response.isSuccessful()) {

                    assert response.body() != null;
                    List<RowsItem> rowsItem = response.body().getRows();
                    List<ElementsItem> elementsItems = rowsItem.get(0).getElements();
                    Distance distance = elementsItems.get(0).getDistance();
                    Duration duration = elementsItems.get(0).getDuration();

                    Toast.makeText(contex, "distance:" + distance.getText(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(contex, "duration:" + duration.getText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Response> call, @NonNull Throwable t) {
                Toast.makeText(contex, "Failure distance request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static StringBuilder getLatLngForMatrixApi(Location position) {
        return new StringBuilder().append(position.getLatitude()).append(",").append(position.getLongitude());

    }

    public static StringBuilder getLatLngForMatrixApi(LatLng position) {
        return new StringBuilder().append(position.latitude).append(",").append(position.longitude);
    }
}
