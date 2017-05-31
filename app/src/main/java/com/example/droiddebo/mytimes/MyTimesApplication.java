package com.example.droiddebo.mytimes;

import android.app.Application;

/*
** Used for getting the application instance
**/
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
