package io.ruoyan.pxnavigator.utils;

import android.graphics.drawable.Drawable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ruoyan.pxnavigator.model.Category;
import io.ruoyan.pxnavigator.model.Photo;

/**
 * Created by ruoyan on 12/27/15.
 */
public class PhotoCacheUtils {
    private static Map<Category, List<Photo>> photoInfoCache = new HashMap<>();
    private static Map<Category, Map<Integer,Drawable>> drawablePhotoCache = new HashMap<>();

    public static List<Photo> getPhotoInfo(Category category) {
        return photoInfoCache.containsKey(category)?photoInfoCache.get(category):null;
    }

    public static void setPhotoInfo(Category category, List<Photo> photos) {
        photoInfoCache.put(category, photos);
    }

    public static Drawable getDrawablePhoto(Category category, int position) {
        return drawablePhotoCache.containsKey(category)?drawablePhotoCache.get(category).get
                (position):null;
    }

    public static void addDrawablePhoto(Category category, int position, Drawable drawable) {
        if (!drawablePhotoCache.containsKey(category))
            drawablePhotoCache.put(category, new HashMap<Integer,Drawable>());

        Map<Integer,Drawable> drawableMap = drawablePhotoCache.get(category);
        drawableMap.put(position, drawable);
    }

    public static void clearCache() {
        photoInfoCache.clear();
        drawablePhotoCache.clear();
    }
}
