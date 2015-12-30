package io.ruoyan.pxnavigator.utils;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by ruoyan on 12/30/15.
 */
public class MapUtils {
    private static GoogleMap sGoogleMap;

    public static GoogleMap getGoogleMap() {
        return sGoogleMap;
    }

    public static void setupGoogleMap(GoogleMap googleMap) {
        sGoogleMap = googleMap;
    }
}
