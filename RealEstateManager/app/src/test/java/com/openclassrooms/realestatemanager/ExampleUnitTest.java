package com.openclassrooms.realestatemanager;

import org.junit.Test;

import static org.junit.Assert.*;

import com.openclassrooms.realestatemanager.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void convert_2DollarsToEuro_Return2() {
        assertEquals(2, Utils.convertDollarToEuro(2));
    }

    @Test
    public void convert_5DollarsToEuro_Return4() {
        assertEquals(4, Utils.convertDollarToEuro(5));
    }

    @Test
    public void convert_10DollarsToEuro_Return8() {
        assertEquals(8, Utils.convertDollarToEuro(10));
    }


    @Test
    //TODO bug see format in mainActivity
    public void convert_8EurosToDollars_Return10() {
        assertEquals(10, Utils.convertEuroToDollar(8));
    }

    @Test
    public void check_formatDateIs_DDMMYYYY() {
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        assertEquals(currentDate, Utils.getTodayDate());
    }
}