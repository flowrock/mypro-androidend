package io.ruoyan.pxnavigator.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import butterknife.InjectView;
import io.ruoyan.pxnavigator.R;
import io.ruoyan.pxnavigator.ui.adapter.UserProfileAdapter;

/**
 * Created by Miroslaw Stanek on 14.01.15.
 */
public class MainActivity extends BaseDrawerActivity {

    private UserProfileAdapter userPhotosAdapter;

    @InjectView(R.id.rvUserProfile)
    RecyclerView rvUserProfile;

    @InjectView(R.id.tlUserProfileTabs)
    TabLayout tlUserProfileTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupTabs();
        setupUserProfileGrid();
    }

    private void setupTabs() {
        tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.ic_grid_on_white));
        tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.ic_list_white));
        tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.ic_place_white));
        tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.ic_label_white));
    }

    private void setupUserProfileGrid() {
        rvUserProfile.setVisibility(View.VISIBLE);
        userPhotosAdapter = new UserProfileAdapter(this);
        rvUserProfile.setAdapter(userPhotosAdapter);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvUserProfile.setLayoutManager(layoutManager);
        rvUserProfile.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                userPhotosAdapter.setLockedAnimations(true);
            }
        });
    }
}
