package com.news.droiddebo.mytimes.util;

import android.content.Context;
import android.net.ConnectivityManager;

import com.news.droiddebo.mytimes.MyTimesApplication;

public class UtilityMethods {
    /**
     * Method to detect network connection on the device
     */
    public static boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) MyTimesApplication.getMyTimesApplicationInstance()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
