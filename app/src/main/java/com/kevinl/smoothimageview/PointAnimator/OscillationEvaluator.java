package com.kevinl.smoothimageview.PointAnimator;


import static com.kevinl.smoothimageview.MainActivity.getNavigationBarHeight;

import android.animation.TypeEvaluator;
import android.app.Activity;
import android.util.DisplayMetrics;

public class OscillationEvaluator implements TypeEvaluator<Point> {

    @Override
    public Point evaluate(float fraction, Point startValue, Point endValue) {
        Point startPoint = (Point) startValue;
        Point endPoint = (Point) endValue;
        float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());//x坐标线性变化
        float y = 120 * (float) (Math.sin(0.01 * Math.PI * x)) + getHeight() / 2;//y坐标取相对应函数值
        return new Point(x, y);
    }

    private float getHeight() {

        return 1080f;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels + getNavigationBarHeight(activity);
    }
}
