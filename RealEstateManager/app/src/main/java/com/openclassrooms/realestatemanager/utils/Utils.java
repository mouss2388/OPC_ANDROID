package com.openclassrooms.realestatemanager.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.SignActivity;
import com.openclassrooms.realestatemanager.database.enumeration.Currency;
import com.openclassrooms.realestatemanager.databinding.ActivitySignBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

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

    public static String PICTURE_USER = "PICTURE_USER";
    public static String PICTURE_GALLERY = "PICTURE_GALLERY";

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param dollars - dollars to euros
     * @return euros
     */
    public static double convertDollarToEuro(double dollars) {
        return Math.round(dollars * 0.812);
    }

    /**
     * Conversion d'un prix d'un bien immobilier (Euros vers Dollars)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param euros - euros to dollars
     * @return dollars
     */
    public static double convertEuroToDollar(double euros) {
        return Math.round(euros / 0.812);
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

    public static boolean atLeastOneFieldToUpdateIsEmpty(Dialog customDialogRealEstate, TextInputLayout[] fields, String msgRequired) {

        for (TextInputLayout field : fields) {
            String value = Objects.requireNonNull(field.getEditText()).getText().toString();
            if (value.equals("")) {
                setErrorOnField(customDialogRealEstate, field.getId(), msgRequired);
                return true;
            } else {
                clearErrorOnField(customDialogRealEstate, field.getId());
            }
        }
        return false;
    }

    public static void setErrorOnField(Dialog dialog, int id, String msgRequiered) {

        TextInputLayout textInputLayout = null;

        if (id == R.id.txtFieldPrice) {
            textInputLayout = dialog.findViewById(R.id.txtFieldPrice);

        } else if (id == R.id.txtFieldDescription) {
            textInputLayout = dialog.findViewById(R.id.txtFieldDescription);

        } else if (id == R.id.txtFieldDate) {
            textInputLayout = dialog.findViewById(R.id.txtFieldDate);

        } else if (id == R.id.txtFieldLocation) {
            textInputLayout = dialog.findViewById(R.id.txtFieldLocation);

        } else if (id == R.id.txtFieldSurface) {
            textInputLayout = dialog.findViewById(R.id.txtFieldSurface);

        } else if (id == R.id.txtFieldRooms) {
            textInputLayout = dialog.findViewById(R.id.txtFieldRooms);

        } else if (id == R.id.txtFieldBathrooms) {
            textInputLayout = dialog.findViewById(R.id.txtFieldBathrooms);

        } else if (id == R.id.txtFieldBedrooms) {
            textInputLayout = dialog.findViewById(R.id.txtFieldBedrooms);

        } else  if (id == R.id.txtFieldInterestPoint){
            textInputLayout = dialog.findViewById(R.id.txtFieldInterestPoint);
        }
        textInputLayout.setError(msgRequiered);
    }

    public static void clearErrorOnField(Dialog dialog, int id) {

        TextInputLayout textInputLayout;

        if (id == R.id.txtFieldPrice) {
            textInputLayout = dialog.findViewById(R.id.txtFieldPrice);

        } else if (id == R.id.txtFieldDescription) {
            textInputLayout = dialog.findViewById(R.id.txtFieldDescription);

        } else if (id == R.id.txtFieldDate) {
            textInputLayout = dialog.findViewById(R.id.txtFieldDate);

        } else if (id == R.id.txtFieldLocation) {
            textInputLayout = dialog.findViewById(R.id.txtFieldLocation);

        } else if (id == R.id.txtFieldSurface) {
            textInputLayout = dialog.findViewById(R.id.txtFieldSurface);

        } else if (id == R.id.txtFieldRooms) {
            textInputLayout = dialog.findViewById(R.id.txtFieldRooms);

        } else if (id == R.id.txtFieldBathrooms) {
            textInputLayout = dialog.findViewById(R.id.txtFieldBathrooms);

        } else if (id == R.id.txtFieldBedrooms) {
            textInputLayout = dialog.findViewById(R.id.txtFieldBedrooms);

        } else {
            textInputLayout = dialog.findViewById(R.id.txtFieldInterestPoint);
        }
        textInputLayout.setError(null);
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

    public static int castDoubleToInt(double numberDouble) {
        return (int) numberDouble;
    }

    public static String convertToString(int value) {
        return String.valueOf(value);
    }

    public static String convertToString(double value) {
        return String.valueOf(value);
    }

    public static void setProfilePicture(Context context, String url, ImageView picture) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .apply(RequestOptions.circleCropTransform())
                .into(picture);
    }

    public static void setProfilePicture(Context context, int drawable, ImageView picture) {
        Glide.with(context)
                .load(drawable)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .apply(RequestOptions.circleCropTransform())
                .into(picture);
    }

    public static void setRealEstatePicture(Context context, String url, ImageView picture) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(picture);
    }

    public static String convertCurrency(String currency, Currency currencyType) {
        double value = Double.parseDouble(currency);
        if (currencyType.equals(Currency.dollar)) {
            double euros = convertDollarToEuro(value);
            return convertToString(castDoubleToInt(euros));
        } else {
            double euros = convertEuroToDollar(value);
            return convertToString(castDoubleToInt(euros));
        }
    }

    public static void setupListenerCloseBtn(Dialog dialog) {
        ImageButton close = dialog.findViewById(R.id.close_Settings);
        close.setOnClickListener(v -> dialog.dismiss());
    }

    public static Dialog getDialog(Context context, int layoutId) {

        Dialog dialog = new Dialog(context);

        int width = layoutId==  R.layout.image_zoomed_layout ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutId);
        dialog.setCancelable(true);

        dialog.getWindow().setLayout(width,height);
        return dialog;
    }

    public static void showToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static boolean DoesOneInputHasValueToZero(Dialog dialog, TextInputLayout[] fields, Context context) {
        for (TextInputLayout field : fields) {
            EditText editText = field.getEditText();
            if (Objects.requireNonNull(editText).getInputType() == InputType.TYPE_CLASS_NUMBER) {
                if (editText.getText().toString().equals("0")) {
                    setErrorOnField(dialog, field.getId(), context.getString(R.string.zero_value));
                    return true;
                }else{
                    clearErrorOnField(dialog, field.getId());

                }
            }
        }
        return false;
    }
}
