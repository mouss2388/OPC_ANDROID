package com.openclassrooms.realestatemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.test.InstrumentationRegistry;

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
        // Setup
        ConnectivityManager connectivityManager = Mockito.mock(ConnectivityManager.class);
        NetworkInfo networkInfo = Mockito.mock(NetworkInfo.class);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo);
        Mockito.when(networkInfo.isAvailable()).thenReturn(true);
        Mockito.when(networkInfo.isConnected()).thenReturn(true);

        assertTrue(networkInfo.isAvailable());
        assertTrue(networkInfo.isConnected());

//        Mockito.verify(connectivityManager).getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        Mockito.verify(networkInfo).isAvailable();
//        Mockito.verify(networkInfo).isConnected();
    }
}
