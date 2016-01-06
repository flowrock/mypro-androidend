package io.ruoyan.pxnavigator;

import android.app.ActivityManager;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by ruoyan on 12/29/15.
 */
public class MyGlideModule implements GlideModule{
    private static final int MEM_CACHE_SIZE = 1024 * 1024 * ((ActivityManager) MyApp.getContext()
            .getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass() / 10;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, MEM_CACHE_SIZE * 2));
        builder.setMemoryCache(new LruResourceCache(MEM_CACHE_SIZE));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
