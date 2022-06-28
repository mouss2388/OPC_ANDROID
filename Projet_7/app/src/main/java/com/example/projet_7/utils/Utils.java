package com.example.projet_7.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class Utils {

    public static void showSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
