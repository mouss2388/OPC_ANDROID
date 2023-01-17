package com.openclassrooms.realestatemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.openclassrooms.realestatemanager", appContext.getPackageName());
    }


    @Test
    public void check_appConnectedToWifi_ReturnTrue() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals(true, Utils.isInternetAvailable(appContext));
    }


    @Test
    public void check_appConnectedToWifi() {

        ConnectivityManager connectivityManager = Mockito.mock(ConnectivityManager.class);

        WifiManager wifiManager = Mockito.mock(WifiManager.class);
        Mockito.when(wifiManager.isWifiEnabled()).thenReturn(true);


        Context context = Mockito.mock(Context.class);
        Context applicationContext = Mockito.mock(Context.class);

        Mockito.when(context.getApplicationContext()).thenReturn(applicationContext);
        Mockito.when(applicationContext.getSystemService(any(String.class))).thenReturn(wifiManager);

        assertEquals(applicationContext.getSystemService(Context.WIFI_SERVICE), wifiManager);

        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);

        assertEquals(context.getApplicationContext().getSystemService(Context.WIFI_SERVICE), wifiManager);
        NetworkInfo networkInfo = Mockito.mock(NetworkInfo.class);

        Mockito.when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);

        Mockito.when(networkInfo.isConnected()).thenReturn(true);
        assertTrue(Utils.isInternetAvailable(context));

        Mockito.when(networkInfo.isConnected()).thenReturn(false);
        assertFalse(Utils.isInternetAvailable(context));

    }
}
