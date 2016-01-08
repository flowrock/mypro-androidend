package io.ruoyan.pxnavigator.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.List;

import io.ruoyan.pxnavigator.R;
import io.ruoyan.pxnavigator.model.Photo;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by ruoyan on 1/7/16.
 */
public class PhotoGalleryPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Photo> mPhotos;

    public PhotoGalleryPagerAdapter(Context context, List<Photo> photos) {
        mContext = context;
        mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        mPhotos = photos;
    }

    @Override
    public int getCount() {
        return mPhotos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (LinearLayout)object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.item_photo_gallery, container, false);
        PhotoView imageView = (PhotoView)view.findViewById(R.id.gallery_photo);
        String imageUrl = mPhotos.get(position).getFullImageUrl();
        Glide.with(mContext).load(imageUrl).into(imageView);
        ((ViewPager) container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
