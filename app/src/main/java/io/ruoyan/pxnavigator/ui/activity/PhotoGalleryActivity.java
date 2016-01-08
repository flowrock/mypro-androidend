package io.ruoyan.pxnavigator.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.ruoyan.pxnavigator.R;
import io.ruoyan.pxnavigator.model.Photo;
import io.ruoyan.pxnavigator.model.PhotoWrapper;
import io.ruoyan.pxnavigator.ui.adapter.PhotoGalleryPagerAdapter;

public class PhotoGalleryActivity extends AppCompatActivity {
    private static final String INTENT_EXTRA_PHOTO_POSITION = "PHOTO_POSITION";
    private static final String INTENT_EXTRA_PHOTO_LIST = "PHOTO_LIST";

    @InjectView(R.id.photo_gallery_viewpager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_gallery);
        ButterKnife.inject(this);

        int position = getIntent().getIntExtra(INTENT_EXTRA_PHOTO_POSITION, 0);
        PhotoWrapper wrapper = (PhotoWrapper)getIntent().getSerializableExtra(INTENT_EXTRA_PHOTO_LIST);
        List<Photo> photos = wrapper.getPhotos();
        mViewPager.setAdapter(new PhotoGalleryPagerAdapter(PhotoGalleryActivity.this, photos));
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(position);
    }

}
