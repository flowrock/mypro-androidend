package io.ruoyan.pxnavigator.utils;

import android.content.Context;
import android.widget.Toast;

import io.ruoyan.pxnavigator.MyApp;

/**
 * Created by ruoyan on 12/20/15.
 */
public class ToastUtils {
    private ToastUtils() {
    }

    private static void show(Context context, int resId, int duration) {
        Toast.makeText(context, resId, duration).show();
    }

    private static void show(Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

    public static void showShort(int resId) {
        Toast.makeText(MyApp.getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(String message) {
        Toast.makeText(MyApp.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(int resId) {
        Toast.makeText(MyApp.getContext(), resId, Toast.LENGTH_LONG).show();
    }

    public static void showLong(String message) {
        Toast.makeText(MyApp.getContext(), message, Toast.LENGTH_LONG).show();
    }
}
