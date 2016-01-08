package io.ruoyan.pxnavigator.model;

import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by ruoyan on 12/31/15.
 */
public class MapItem implements ClusterItem {
    private static final float INITIAL_ZOOM_LEVEL = 2f;
    private static final float ZOOM_INCREMENT = 3.5f;

    private final LatLng mPosition;
    private final Drawable mImageDrawable;
    private float mZoomLevel = INITIAL_ZOOM_LEVEL;
    private int mZoomFlag;

    public MapItem(LatLng position, Drawable drawable) {
        mPosition = position;
        mImageDrawable = drawable;
        mZoomLevel = INITIAL_ZOOM_LEVEL;
        mZoomFlag = -1;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public Drawable getImageDrawable() {
        return mImageDrawable;
    }

    public float getNextZoomLevel() {
        if (mZoomLevel==INITIAL_ZOOM_LEVEL+2*ZOOM_INCREMENT || mZoomLevel==INITIAL_ZOOM_LEVEL)
            mZoomFlag *= -1;
        mZoomLevel = mZoomFlag==1 ? mZoomLevel+ZOOM_INCREMENT:INITIAL_ZOOM_LEVEL;
        return mZoomLevel;
    }
}
