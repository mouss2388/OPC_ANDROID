package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Philippe on 21/02/2018.
 */
@SuppressWarnings("ALL")
public class Utils {

    public static final String  SIGN_CHOICE = "SIGN_CHOICE";
    public static final String  SIGN_IN = "SIGN_IN";
    public static final String  SIGN_UP = "SIGN_UP";
    public static final String  ERROR_GET_BUNDLE = "ERROR GET BUNDLE";


    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.812);
    }

    /**
     * Conversion d'un prix d'un bien immobilier (Euros vers Dollars)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param euros
     * @return
     */
    public static int convertEuroToDollar(int euros) {
        RoundingMode mode = RoundingMode.HALF_EVEN;
        BigDecimal dollarBigDecimal = new BigDecimal(Double.toString(euros / 0.812)).setScale(0, mode);
        return dollarBigDecimal.intValue();

    }
    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param context
     * @return
     */
    public static Boolean isInternetAvailable(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled()) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        }
        {
            return false;
        }
    }


    public static void startSignActivity(Context context, String id) {
        Intent intent = new Intent(context, SignActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SIGN_CHOICE, id);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
