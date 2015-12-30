package io.ruoyan.pxnavigator.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import butterknife.InjectView;
import io.ruoyan.pxnavigator.R;
import io.ruoyan.pxnavigator.ui.adapter.PagerAdapter;
import io.ruoyan.pxnavigator.utils.MapUtils;

/**
 * Created by Miroslaw Stanek on 14.01.15.
 */
public class MainActivity extends BaseDrawerActivity {

    @InjectView(R.id.photoCategoryTab)
    TabLayout mPhotoCategoryTab;

    @InjectView(R.id.photoViewPager)
    ViewPager mPhotoViewPager;

    @InjectView(R.id.progressbar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar.setVisibility(View.GONE);

        setupTabs();
        initializeMap();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mPhotoViewPager.setAdapter(new PagerAdapter(fragmentManager));
        TabLayout.OnTabSelectedListener tabListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mPhotoViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
        mPhotoCategoryTab.setOnTabSelectedListener(tabListener);
        mPhotoViewPager.addOnPageChangeListener
                (new TabLayout.TabLayoutOnPageChangeListener(mPhotoCategoryTab));
        tabListener.onTabSelected(mPhotoCategoryTab.getTabAt(0));
    }

    private void setupTabs() {
        mPhotoCategoryTab.addTab(mPhotoCategoryTab.newTab().setIcon(R.drawable.ic_grid_on_white));
        mPhotoCategoryTab.addTab(mPhotoCategoryTab.newTab().setIcon(R.drawable.ic_list_white));
        mPhotoCategoryTab.addTab(mPhotoCategoryTab.newTab().setIcon(R.drawable.ic_place_white));
        mPhotoCategoryTab.addTab(mPhotoCategoryTab.newTab().setIcon(R.drawable.ic_label_white));
    }

    private void initializeMap() {
        GoogleMap map = ((SupportMapFragment)getSupportFragmentManager().
                findFragmentById(R.id.mapFragment)).getMap();
        MapUtils.setupGoogleMap(map);
    }

}
