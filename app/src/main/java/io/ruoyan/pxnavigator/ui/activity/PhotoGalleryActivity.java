package io.ruoyan.pxnavigator.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.ruoyan.pxnavigator.R;
import io.ruoyan.pxnavigator.model.Category;
import io.ruoyan.pxnavigator.model.Photo;
import io.ruoyan.pxnavigator.ui.adapter.PhotoGalleryPagerAdapter;
import io.ruoyan.pxnavigator.helper.PhotoCacheHelper;

public class PhotoGalleryActivity extends AppCompatActivity {
    private static final String INTENT_EXTRA_PHOTO_CATEGORY = "PHOTO_CATEGORY";
    private static final String INTENT_EXTRA_PHOTO_DAY = "PHOTO_DAY";
    private static final String INTENT_EXTRA_PHOTO_POSITION = "PHOTO_POSITION";

    @InjectView(R.id.photo_gallery_viewpager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_gallery);
        ButterKnife.inject(this);

        Category category = Category.valueOf(getIntent().getStringExtra
                (INTENT_EXTRA_PHOTO_CATEGORY));
        String day = getIntent().getStringExtra(INTENT_EXTRA_PHOTO_DAY);
        List<Photo> photos = PhotoCacheHelper.getPhotoInfo(category, day);
        int position = getIntent().getIntExtra(INTENT_EXTRA_PHOTO_POSITION, 0);

        mViewPager.setAdapter(new PhotoGalleryPagerAdapter(PhotoGalleryActivity.this, photos));
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(position);
    }

}
