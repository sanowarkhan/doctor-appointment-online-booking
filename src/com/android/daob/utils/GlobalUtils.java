
package com.android.daob.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import com.android.daob.application.AppController;

public class GlobalUtils {
    static DisplayMetrics metrics = null;

    static int statusBarHeight = -1;

    static final int STANDARD_WIDTH = 240;

    static final int STANDARD_HEIGHT = 320;

    static float SCALE_RATE = 0;

    public static int getDisplayWidth() {
        if (metrics == null) {
            if (AppController.getInstance().getActivityContext() instanceof Activity) {
                metrics = new DisplayMetrics();
                ((Activity) AppController.getInstance().getActivityContext()).getWindowManager()
                        .getDefaultDisplay().getMetrics(metrics);
            } else {
                metrics = AppController.getInstance().getActivityContext().getResources()
                        .getDisplayMetrics();
            }
        }
        return metrics.widthPixels;
    }

    public static int getDisplayHeight() {
        if (metrics == null) {
            if (AppController.getInstance().getActivityContext() instanceof Activity) {
                metrics = new DisplayMetrics();
                ((Activity) AppController.getInstance().getActivityContext()).getWindowManager()
                        .getDefaultDisplay().getMetrics(metrics);
            } else {
                metrics = AppController.getInstance().getActivityContext().getResources()
                        .getDisplayMetrics();
            }
        }
        if (statusBarHeight < 0) {
            Context context = AppController.getInstance().getActivityContext();
            statusBarHeight = (int) Math
                    .ceil(25 * context.getResources().getDisplayMetrics().density);
        }
        return metrics.heightPixels - statusBarHeight;
    }

    public static float getScaleRate() {
        if (SCALE_RATE == 0) {
            SCALE_RATE = (GlobalUtils.getDisplayWidth() * GlobalUtils.getDisplayHeight())
                    / (STANDARD_WIDTH * STANDARD_HEIGHT);
            if (SCALE_RATE <= 0) {
                SCALE_RATE = 1;
            }
        }
        return SCALE_RATE;
    }
}
