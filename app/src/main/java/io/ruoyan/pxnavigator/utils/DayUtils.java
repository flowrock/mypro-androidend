package io.ruoyan.pxnavigator.utils;

import java.util.HashMap;
import java.util.Map;

import io.ruoyan.pxnavigator.model.Category;

/**
 * Created by ruoyan on 1/4/16.
 */
public class DayUtils {
    private static final String[] DAYS = {"1","3","7"};
    private static int pos = 0;
    private static Map<Category, Boolean> refreshMap = new HashMap<>();

    public static void setDay(int index) {
        pos = index;
    }

    public static String getDay() {
        return DAYS[pos];
    }

    public static boolean needRefresh(Category category) {
        return !refreshMap.get(category);
    }

    public static void resetRefreshMap() {
        for (Category c : refreshMap.keySet())
            refreshMap.put(c, false);
    }

    public static void setRefreshMap(Category category) {
        refreshMap.put(category, true);
    }

}
