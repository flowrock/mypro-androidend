package io.ruoyan.pxnavigator.ui.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.InjectView;
import io.ruoyan.pxnavigator.R;
import io.ruoyan.pxnavigator.utils.PhotoCacheUtils;

/**
 * Modified by Ruoyan Liu on 07.12.15.
 */
public class BaseDrawerActivity extends BaseActivity {

    @InjectView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;

    NavigationView mNavigationView;

    protected void setupDrawerContent() {
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch(menuItem.getItemId()) {
                            case R.id.menu_feed:
                                refreshFeed();
                            case R.id.menu_map_search:
                                Log.i("search","yes");break;
                            case R.id.menu_liked:
                                Log.i("liked","yes");break;

                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
    }

    protected void refreshFeed() {
        PhotoCacheUtils.clearCache();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentViewWithoutInject(R.layout.activity_drawer);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.flContentRoot);
        LayoutInflater.from(this).inflate(layoutResID, viewGroup, true);
        injectViews();
        mNavigationView = (NavigationView)findViewById(R.id.vNavigation);
        if (mNavigationView!=null)
            setupDrawerContent();

    }

    //define the behavior of toolbar navigation button
    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (getToolbar() != null) {
            getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            });
        }
    }

}
