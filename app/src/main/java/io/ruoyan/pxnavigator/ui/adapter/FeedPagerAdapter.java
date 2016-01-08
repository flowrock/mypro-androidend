package io.ruoyan.pxnavigator.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.HashMap;
import java.util.Map;

import io.ruoyan.pxnavigator.model.Category;
import io.ruoyan.pxnavigator.ui.fragment.PhotoListFragment;

/**
 * Created by ruoyan on 12/25/15.
 */
public class FeedPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private Map<Category, Fragment> mfragmentCache;

    public FeedPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        mfragmentCache = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        Category category = Category.values()[position];
        if (mfragmentCache.containsKey(category))
            return mfragmentCache.get(category);
        PhotoListFragment pfragment = PhotoListFragment.newInstance(category);
        mfragmentCache.put(category, pfragment);
        return pfragment;
    }

    @Override
    public int getCount() {
        return Category.values().length;
    }

}
