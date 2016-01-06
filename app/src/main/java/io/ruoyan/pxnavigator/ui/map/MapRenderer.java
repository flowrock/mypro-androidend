package io.ruoyan.pxnavigator.ui.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;

import io.ruoyan.pxnavigator.R;
import io.ruoyan.pxnavigator.model.MapItem;
import io.ruoyan.pxnavigator.ui.view.MultiDrawable;

/**
 * Created by ruoyan on 1/2/16.
 */
public class MapRenderer extends DefaultClusterRenderer<MapItem> implements ClusterManager
        .OnClusterItemClickListener<MapItem>{
    private Context mContext;
    private GoogleMap mGoogleMap;
    private final IconGenerator mIconGenerator;
    private final IconGenerator mClusterIconGenerator;
    private final ImageView mImageView;
    private final ImageView mClusterImageView;
    private final int mDimension;

    public MapRenderer(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
        mGoogleMap = map;

        mIconGenerator = new IconGenerator(mContext);
        mClusterIconGenerator = new IconGenerator(mContext);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mapThumbnailView = inflater.inflate(R.layout.map_thumbnail, null);
        mClusterIconGenerator.setContentView(mapThumbnailView);
        mClusterImageView = (ImageView) mapThumbnailView.findViewById(R.id.image);

        mImageView = new ImageView(mContext);
        mDimension = (int) mContext.getResources().getDimension(R.dimen.map_thumbnail_size);
        mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
        int padding = (int) mContext.getResources().getDimension(R.dimen.map_thumbnail_padding);
        mImageView.setPadding(padding, padding, padding, padding);
        mIconGenerator.setContentView(mImageView);
    }

    @Override
    protected void onBeforeClusterItemRendered(MapItem item, MarkerOptions markerOptions) {
        mImageView.setImageDrawable(item.getImageDrawable());
        Bitmap icon = mIconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<MapItem> cluster, MarkerOptions markerOptions) {
        List<Drawable> profilePhotos = new ArrayList<>(Math.min(4, cluster.getSize()));

        for (MapItem item : cluster.getItems()) {
            if (profilePhotos.size() == 4) break;
            Drawable drawable = item.getImageDrawable();
            profilePhotos.add(drawable);
        }

        MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
        try {
            mClusterImageView.setImageDrawable(multiDrawable);
        } catch (Exception e) {

        }
        Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        return cluster.getSize() > 6;
    }

    @Override
    public boolean onClusterItemClick(MapItem mapItem) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapItem.getPosition(),
                mapItem.getNextZoomLevel()));
        return false;
    }
}
