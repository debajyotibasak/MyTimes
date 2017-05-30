package com.example.droiddebo.mytimes;

import android.app.Application;

/**
 * Created by DROID DEBO on 30-05-2017.
 */

public class MyTimesApplication extends Application {
    private static MyTimesApplication myTimesApplicationInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        myTimesApplicationInstance = this;
    }

    public static MyTimesApplication getMyTimesApplicationInstance(){
        return myTimesApplicationInstance;
    }
}
