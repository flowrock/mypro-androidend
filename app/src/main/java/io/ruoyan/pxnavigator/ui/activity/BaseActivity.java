package io.ruoyan.pxnavigator.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import io.ruoyan.pxnavigator.R;

/**
 * Modified by Ruoyan Liu on 07.12.15.
 *
 * Base activity is to initialize the common elements in all activities.
 */
public class BaseActivity extends AppCompatActivity {

    @Optional
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @Optional
    @InjectView(R.id.ivLogo)
    ImageView ivLogo;

    private MenuItem dayMenuItem;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        injectViews();
    }

    protected void injectViews() {
        ButterKnife.inject(this);
        setupToolbar();
    }

    public void setContentViewWithoutInject(int layoutResId) {
        super.setContentView(layoutResId);
    }

    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        dayMenuItem = menu.findItem(R.id.action_day_selection);
        return true;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    //this function and the below one is to return the UI elements in toolbar for animation use
    public MenuItem getDayMenuItem() {
        return dayMenuItem;
    }

    public ImageView getIvLogo() {
        return ivLogo;
    }
}
