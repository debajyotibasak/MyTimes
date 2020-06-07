package com.news.droiddebo.mytimes;

import android.app.Application;

/**
 * Used for getting the application instance
 */
public class MyTimesApplication extends Application {
    private static MyTimesApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static MyTimesApplication getApplication() {
        return application;
    }
}
