package com.example.mehdi.esp_wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * @name    CheckNetwork
 * @brief   The class check the network state and return proper response according to network state
 *              false: if network is not available
 *              true: if network is available
 * @author  Mehdi
 */

public class CheckNetwork {

    private static final String TAG = CheckNetwork.class.getSimpleName();

    public static boolean isInternetAvailable(Context context)
    {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null) {

            Log.d(TAG,"No Internet Connection");
            return false;

        } else {

            if(info.isConnected()) {

                Log.d(TAG," Internet Connection Available...");
                return true;

            } else {

                Log.d(TAG," Internet Connection");
                return true;

            }
        }
    }
}
