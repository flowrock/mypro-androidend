package io.ruoyan.pxnavigator;

import android.app.Application;
import android.content.Context;

/**
 * Created by froger_mcs on 05.11.14.
 */
public class MyApp extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }

}
