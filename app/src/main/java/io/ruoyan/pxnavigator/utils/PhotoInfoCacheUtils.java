package io.ruoyan.pxnavigator.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ruoyan.pxnavigator.model.Category;
import io.ruoyan.pxnavigator.model.Photo;

/**
 * Created by ruoyan on 12/27/15.
 */
public class PhotoInfoCacheUtils {
    public static Map<Category, List<Photo>> photoInfoCache = new HashMap<>();

    public static List<Photo> getPhotoInfo(Category category) {
        return photoInfoCache.containsKey(category)?photoInfoCache.get(category):null;
    }

    public static void setPhotoInfo(Category category, List<Photo> photos) {
        photoInfoCache.put(category, photos);
    }
}
