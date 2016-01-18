package io.ruoyan.pxnavigator.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import butterknife.InjectView;
import io.ruoyan.pxnavigator.R;
import io.ruoyan.pxnavigator.ui.adapter.FeedPagerAdapter;
import io.ruoyan.pxnavigator.utils.BasicUtils;
import io.ruoyan.pxnavigator.helper.DayHelper;
import io.ruoyan.pxnavigator.helper.MapHelper;

/**
 * Created by ruoyan on 12/20/15.
 */
public class MainActivity extends BaseDrawerActivity {

    @InjectView(R.id.photoCategoryTab)
    TabLayout mPhotoCategoryTab;

    @InjectView(R.id.photoViewPager)
    ViewPager mPhotoViewPager;

    private int mFeedDay = 0;
    private FeedPagerAdapter mFeedPagerAdapter;
    private boolean mPendingAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
            mPendingAnimation = true;

        setupTabs();
        initializeMap();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mFeedPagerAdapter = new FeedPagerAdapter(fragmentManager);
        mPhotoViewPager.setAdapter(mFeedPagerAdapter);
        mPhotoViewPager.setOffscreenPageLimit(3);
        TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout
                .OnTabSelectedListener() {
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
        mPhotoCategoryTab.setOnTabSelectedListener(tabSelectedListener);
        mPhotoViewPager.addOnPageChangeListener
                (new TabLayout.TabLayoutOnPageChangeListener(mPhotoCategoryTab));
        tabSelectedListener.onTabSelected(mPhotoCategoryTab.getTabAt(0));

    }

    private void setupTabs() {
        mPhotoCategoryTab.addTab(mPhotoCategoryTab.newTab().setIcon(R.drawable.ic_place_white));
        mPhotoCategoryTab.addTab(mPhotoCategoryTab.newTab().setIcon(R.drawable.ic_tab_city));
        mPhotoCategoryTab.addTab(mPhotoCategoryTab.newTab().setIcon(R.drawable.ic_tab_landscape));
        mPhotoCategoryTab.addTab(mPhotoCategoryTab.newTab().setIcon(R.drawable.ic_tab_people));
    }

    private void initializeMap() {
        GoogleMap map = ((SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.mapFragment)).getMap();
        MapHelper.setupGoogleMap(map);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (mPendingAnimation) {
            mPendingAnimation = false;
            startIntroAnimation();
        }
        return true;
    }

    private void startIntroAnimation() {
        int actionbarSize = BasicUtils.dpToPx(56);
        getToolbar().setTranslationY(-actionbarSize);
        getIvLogo().setTranslationY(-actionbarSize);

        getToolbar().animate().translationY(0).setDuration(300);
        getIvLogo().animate().translationY(0).setDuration(300).setStartDelay(500).start();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_day_selection)
            showDaySelectionDialog();

        return super.onOptionsItemSelected(item);
    }

    private void showDaySelectionDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.day_selection_dialog_title)
                .items(R.array.day_selection_options)
                .itemsCallbackSingleChoice(mFeedDay, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        int oldDay = mFeedDay;
                        mFeedDay = which;
                        if (oldDay != mFeedDay)
                            replaceFeed();
                        switch (mFeedDay) {
                            case 0:
                                getDayMenuItem().setIcon(R.drawable.ic_toolbar_today);
                                break;
                            case 1:
                                getDayMenuItem().setIcon(R.drawable.ic_toolbar_day3);
                                break;
                            case 2:
                                getDayMenuItem().setIcon(R.drawable.ic_toolbar_week);
                        }
                        return true;
                    }
                })
                .positiveText(R.string.choose_label)
                .show();
    }

    private void replaceFeed() {
        DayHelper.instance().setDay(mFeedDay);
        int currentPosition = mPhotoViewPager.getCurrentItem();
        mPhotoViewPager.setAdapter(mFeedPagerAdapter); //refresh current page
        if (currentPosition != 0)
            mPhotoViewPager.setCurrentItem(currentPosition);
        else {
            mPhotoViewPager.setCurrentItem(1); //this is a workaround for the bug that
            // setCurrentItem(0) does not work
        }
    }

    @Override
    protected void refreshFeed() {
        super.refreshFeed();
        replaceFeed();
    }
}
