package com.example.droiddebo.mytimes.Util;

import android.content.Context;
import android.net.ConnectivityManager;

import com.example.droiddebo.mytimes.MyTimesApplication;

public class UtilityMethods {
    /**
     * Method to detect network connection on the device
     * @return
     */
    public static boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) MyTimesApplication.getMyTimesApplicationInstance()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
