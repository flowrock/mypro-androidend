package io.ruoyan.pxnavigator;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import timber.log.Timber;

/**
 * Created by froger_mcs on 05.11.14.
 */
public class MyApp extends Application {

    private static Context sContext;

    private static int MEM_CACHE_SIZE = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        MEM_CACHE_SIZE = 1024 * 1024 * ((ActivityManager)
                sContext.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass() / 8;

        Timber.plant(new Timber.DebugTree());
    }

    public static Context getContext() {
        return sContext;
    }

}
