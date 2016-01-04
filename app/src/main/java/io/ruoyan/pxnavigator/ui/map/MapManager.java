package io.ruoyan.pxnavigator.ui.map;

import android.content.Context;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import io.ruoyan.pxnavigator.model.Category;
import io.ruoyan.pxnavigator.model.MapItem;
import io.ruoyan.pxnavigator.model.Photo;
import io.ruoyan.pxnavigator.utils.MapUtils;
import io.ruoyan.pxnavigator.utils.PhotoCacheUtils;

/**
 * Created by ruoyan on 1/2/16.
 */
public class MapManager {
    private ClusterManager<MapItem> mClusterManager;
    private static MapManager sMapManager;
    private GoogleMap mGoogleMap;

    public MapManager(Context context) {
        mGoogleMap = MapUtils.getGoogleMap();
        mClusterManager = new ClusterManager<>(context,mGoogleMap);
        mClusterManager.setRenderer(new MapRenderer(context, mGoogleMap, mClusterManager));
        mGoogleMap.setOnCameraChangeListener(mClusterManager);
        mGoogleMap.setOnMarkerClickListener(mClusterManager);
        mGoogleMap.setOnInfoWindowClickListener(mClusterManager);
    }

    public static MapManager getMapManager(Context context) {
        if (sMapManager == null)
            sMapManager = new MapManager(context);
        return sMapManager;
    }

    public void generateCluster(Category category, int[] visiblePositions) {
        mClusterManager.clearItems();
        double meanLat=0, meanLong=0;
        List<Photo> list = PhotoCacheUtils.getPhotoInfo(category);
        for (int i=visiblePositions[0]; i<=visiblePositions[1]; i++) {
            Photo photo = list.get(i);
            LatLng lng = new LatLng(photo.getLatitude(), photo.getLongitude());
            meanLat += photo.getLatitude();
            meanLong += photo.getLongitude();
            MapItem mapItem = new MapItem(lng, PhotoCacheUtils.getDrawablePhoto(category, i));
            mClusterManager.addItem(mapItem);
        }
        meanLat /= visiblePositions[1]-visiblePositions[0]+1;
        meanLong /= visiblePositions[1]-visiblePositions[0]+1;
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(meanLat, meanLong),0));
        mClusterManager.cluster();
    }
}
