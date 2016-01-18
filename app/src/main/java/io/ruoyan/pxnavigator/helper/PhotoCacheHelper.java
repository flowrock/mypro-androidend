package io.ruoyan.pxnavigator.helper;

import android.graphics.drawable.Drawable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ruoyan.pxnavigator.model.Category;
import io.ruoyan.pxnavigator.model.Photo;

/**
 * Created by ruoyan on 12/27/15.
 */
public class PhotoCacheHelper {
    private static Map<String, List<Photo>> photoInfoCache = new HashMap<>();
    private static Map<String, Map<Integer,Drawable>> drawablePhotoCache = new HashMap<>();

    public static List<Photo> getPhotoInfo(Category category, String day) {
        return photoInfoCache.containsKey(getKey(category,day)) ? photoInfoCache.get(getKey
                (category,day)):null;
    }

    public static void setPhotoInfo(Category category, String day, List<Photo> photos) {
        photoInfoCache.put(getKey(category,day), photos);
    }

    public static Drawable getDrawablePhoto(Category category, String day, int position) {
        return drawablePhotoCache.containsKey(getKey(category,day)) ? drawablePhotoCache.get
                (getKey(category,day)).get
                (position):null;
    }

    public static void addDrawablePhoto(Category category, String day, int position, Drawable
            drawable) {
        if (!drawablePhotoCache.containsKey(getKey(category,day)))
            drawablePhotoCache.put(getKey(category,day), new HashMap<Integer,Drawable>());

        Map<Integer,Drawable> drawableMap = drawablePhotoCache.get(getKey(category,day));
        drawableMap.put(position, drawable);
    }

    public static void clearCache() {
        photoInfoCache.clear();
        drawablePhotoCache.clear();
    }

    private static String getKey(Category category, String day) {
        return category.name()+day;
    }

}
