package io.ruoyan.pxnavigator.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import butterknife.InjectView;
import io.ruoyan.pxnavigator.R;
import io.ruoyan.pxnavigator.ui.fragment.PhotoListFragment;

/**
 * Created by Miroslaw Stanek on 14.01.15.
 */
public class MainActivity extends BaseDrawerActivity {

    private static final int PHOTO_TABS = 4;

    @InjectView(R.id.photoCategoryTab)
    TabLayout mPhotoCategoryTab;

    @InjectView(R.id.photoViewPager)
    ViewPager mPhotoViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupTabs();

        mPhotoViewPager.addOnPageChangeListener
                (new TabLayout.TabLayoutOnPageChangeListener(mPhotoCategoryTab));
        FragmentManager fragmentManager = getSupportFragmentManager();
        mPhotoViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return new PhotoListFragment();
            }

            @Override
            public int getCount() {
                return PHOTO_TABS;
            }
        });
        mPhotoCategoryTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPhotoViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setupTabs() {
        mPhotoCategoryTab.addTab(mPhotoCategoryTab.newTab().setIcon(R.drawable.ic_grid_on_white));
        mPhotoCategoryTab.addTab(mPhotoCategoryTab.newTab().setIcon(R.drawable.ic_list_white));
        mPhotoCategoryTab.addTab(mPhotoCategoryTab.newTab().setIcon(R.drawable.ic_place_white));
        mPhotoCategoryTab.addTab(mPhotoCategoryTab.newTab().setIcon(R.drawable.ic_label_white));
    }

}
