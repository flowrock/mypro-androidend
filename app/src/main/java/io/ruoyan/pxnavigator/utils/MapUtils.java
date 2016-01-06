package io.ruoyan.pxnavigator.utils;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by ruoyan on 12/30/15.
 */
public class MapUtils {
    private static GoogleMap sGoogleMap;
    private static boolean sAppInitialized = false;

    public static GoogleMap getGoogleMap() {
        return sGoogleMap;
    }

    public static void setupGoogleMap(GoogleMap googleMap) {
        sGoogleMap = googleMap;
    }

    public static boolean getAppStatus() {
        return sAppInitialized;
    }

    public static void setAppStatus(boolean flag) {
        sAppInitialized = flag;
    }

}
