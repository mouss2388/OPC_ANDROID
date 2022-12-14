package com.openclassrooms.realestatemanager.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.SignActivity;
import com.openclassrooms.realestatemanager.databinding.ActivitySignBinding;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Philippe on 21/02/2018.
 */
public class Utils {

    public static final String SIGN_CHOICE = "SIGN_CHOICE";
    public static final String SIGN_IN = "SIGN_IN";
    public static final String SIGN_UP = "SIGN_UP";
    public static final String ERROR_GET_BUNDLE = "ERROR GET BUNDLE";
    public static final String USER_LOGGED_FORMAT_JSON = "USER_LOGGED_FORMAT_JSON";
    public static final boolean DEVELOPMENT_MODE = true;

    public static String FIRSTNAME = "FIRSTNAME";
    public static String LASTNAME = "LASTNAME";
    public static String EMAIL = "EMAIL";
    public static String PASSWORD = "PASSWORD";
    public static String PASSWORD_CONFIRM = "PASSWORD_CONFIRM";

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param dollars - dollars to euros
     * @return euros
     */
    public static int convertDollarToEuro(int dollars) {
        return (int) Math.round(dollars * 0.812);
    }

    /**
     * Conversion d'un prix d'un bien immobilier (Euros vers Dollars)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param euros - euros to dollars
     * @return dollars
     */
    public static int convertEuroToDollar(int euros) {
        return (int) Math.round(euros / 0.812);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @return dateFormat
     */
    public static String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param context - Context of activity
     * @return boolean
     */
    public static Boolean isInternetAvailable(Context context) {

        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifi.isWifiEnabled()) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        }
        return false;
    }


    public static void startSignActivity(Context context, String id) {
        Intent intent = new Intent(context, SignActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SIGN_CHOICE, id);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static String concatStr(String... texts) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String text : texts) {
            stringBuilder.append(text).append(" ");
        }
        return stringBuilder.toString();
    }

    public static void setErrorOnField(ActivitySignBinding binding, String key, String message) {
        switch (key) {
            case "FIRSTNAME":
                binding.txtFieldFirstname.setError(message);
                return;

            case "LASTNAME":
                binding.txtFieldLastname.setError(message);
                return;

            case "EMAIL":
                binding.txtFieldEmail.setError(message);
                return;

            case "PASSWORD":
                binding.txtFieldPsswrd.setError(message);
                return;

            case "PASSWORD_CONFIRM":
                binding.txtFieldPsswrdConfirm.setError(message);
                return;

            default:
                break;
        }
    }


    public static void setErrorOnField(Dialog dialog, String key, String message) {
        switch (key) {
            case "FIRSTNAME":
                TextInputLayout firstname = dialog.findViewById(R.id.txtFieldFirstname);
                firstname.setError(message);
                return;

            case "LASTNAME":
                TextInputLayout lastname = dialog.findViewById(R.id.txtFieldLastname);
                lastname.setError(message);
                return;

            case "EMAIL":
                TextInputLayout email = dialog.findViewById(R.id.txtFieldEmail);
                email.setError(message);
                return;

            case "PASSWORD":
                TextInputLayout password = dialog.findViewById(R.id.txtFieldPsswrd);
                password.setError(message);
                return;

            default:
                break;
        }
    }

    public static void clearErrorOnField(ActivitySignBinding binding, String key) {
        switch (key) {
            case "FIRSTNAME":
                binding.txtFieldFirstname.setError(null);
                return;

            case "LASTNAME":
                binding.txtFieldLastname.setError(null);
                return;

            case "EMAIL":
                binding.txtFieldEmail.setError(null);
                return;

            case "PASSWORD":
                binding.txtFieldPsswrd.setError(null);
                return;

            case "PASSWORD_CONFIRM":
                binding.txtFieldPsswrdConfirm.setError(null);
                return;

            default:
                break;
        }
    }

    public static void clearErrorOnField(Dialog dialog, String key) {
        switch (key) {
            case "FIRSTNAME":
                TextInputLayout firstname = dialog.findViewById(R.id.txtFieldFirstname);
                firstname.setError(null);
                return;

            case "LASTNAME":
                TextInputLayout lastname = dialog.findViewById(R.id.txtFieldLastname);
                lastname.setError(null);
                return;

            case "EMAIL":
                TextInputLayout email = dialog.findViewById(R.id.txtFieldEmail);
                email.setError(null);
                return;

            case "PASSWORD":
                TextInputLayout password = dialog.findViewById(R.id.txtFieldPsswrd);
                password.setError(null);
                return;

            default:
                break;
        }
    }

    public static void showSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
