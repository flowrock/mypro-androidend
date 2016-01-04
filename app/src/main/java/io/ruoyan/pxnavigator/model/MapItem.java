package io.ruoyan.pxnavigator.model;

import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by ruoyan on 12/31/15.
 */
public class MapItem implements ClusterItem {
    private final LatLng mPosition;
    private final Drawable mImageDrawable;

    public MapItem(LatLng position, Drawable drawable) {
        this.mPosition = position;
        this.mImageDrawable = drawable;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public Drawable getImageDrawable() {
        return mImageDrawable;
    }

}
