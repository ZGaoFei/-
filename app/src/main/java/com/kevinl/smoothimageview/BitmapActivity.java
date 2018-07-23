package com.kevinl.smoothimageview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class BitmapActivity extends AppCompatActivity {
    private ImageView imageView;
    private ImageView imageViewCopy;
    private FrameLayout frameLayout;

    private int screenWidth;
    private int screenHeight;
    private int width;
    private int height;
    private int x;
    private int y;
    private int statusBarHeight;
    private int actionBarHeight;

    private boolean isOut = false;
    private boolean isHave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_bitmap);

        imageView = (ImageView) findViewById(R.id.image_view_bitmap);
        imageViewCopy = (ImageView) findViewById(R.id.image_view_bitmap_copy);
        frameLayout = (FrameLayout) findViewById(R.id.frame_layout_copy);

        getScreenParams();
        width = 300;
        height = 450;
        statusBarHeight = getStatusBarHeight(this);
        actionBarHeight = (int) getActionBarHeight();

        Log.e("zgf", "====screenWidth====" + screenWidth + "====screenHeight===" + screenHeight);
        Log.e("zgf", "====width====" + width + "====height===" + height);
        Log.e("zgf", "====x====" + x + "====y===" + y);
        Log.e("zgf", "====statusBarHeight====" + statusBarHeight + "====actionBarHeight===" + actionBarHeight);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageXY();


                if (isOut) {
                    animatorIn();
                    isOut = false;
                } else {
//                    setImageViewCopyMargin();
                    animatorOut();
                    isOut = true;
                }
            }
        });
    }

    public int dip2px(Context cc, float dipValue) {
        final float scale = cc.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private void getScreenParams() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    private void getImageXY() {
        if (isHave) {
            return;
        }

        isHave = true;
        int[] position = new int[2];
        imageView.getLocationInWindow(position);
        x = position[0];
        y = position[1];
        Log.e("zgf", "=111===x====" + x + "====y===" + y);
    }

    private void setImageViewCopyMargin() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageViewCopy.getLayoutParams();
        layoutParams.setMargins(x, y, 0, 0);
    }

    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private float getActionBarHeight() {
        TypedArray actionbarSizeTypedArray = obtainStyledAttributes(new int[]{
                android.R.attr.actionBarSize
        });

        float actionBarHeight = actionbarSizeTypedArray.getDimension(0, 0);
        return actionBarHeight;
    }

    public void setHide() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void setShow() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void setStatusBar() {
        if (Build.VERSION.SDK_INT>21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void animatorOut() {
        final int currentY = y - statusBarHeight /*- actionBarHeight*/;
        final int currentScreenY = screenHeight /*- statusBarHeight - actionBarHeight*/;

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 100);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            //持有一个IntEvaluator对象，方便下面估值的时候使用
            private IntEvaluator mEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                //计算当前进度占整个动画过程的比例，浮点型，0-1之间
                float fraction = currentValue / 100f;
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageViewCopy.getLayoutParams();
                params.width = mEvaluator.evaluate(fraction, width, screenWidth);//从当前缩略图的宽度到满屏
                params.height = mEvaluator.evaluate(fraction, height, currentScreenY);//从当前缩略图的高度到满屏
                params.leftMargin = mEvaluator.evaluate(fraction, x, 0);//从photoview的x到0
                params.topMargin = mEvaluator.evaluate(fraction, currentY, 0);//从photoview的y到0
                imageViewCopy.setLayoutParams(params);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                frameLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                setHide();

                setStatusBar();
            }
        });
        valueAnimator.setDuration(4000);
        valueAnimator.start();
    }

    private void animatorIn() {
        final int currentY = y - statusBarHeight /*- actionBarHeight*/;
        final int currentScreenY = screenHeight /*- statusBarHeight - actionBarHeight*/;

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 100);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            //持有一个IntEvaluator对象，方便下面估值的时候使用
            private IntEvaluator mEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                //计算当前进度占整个动画过程的比例，浮点型，0-1之间
                float fraction = currentValue / 100f;
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageViewCopy.getLayoutParams();
                params.width = mEvaluator.evaluate(fraction, screenWidth, width);//从当前满屏到缩略图的宽度
                params.height = mEvaluator.evaluate(fraction, currentScreenY, height);//从当前满屏到缩略图的高度
                params.leftMargin = mEvaluator.evaluate(fraction, 0, x);//从bitmap的左上角x坐标到缩略图的x
                params.topMargin = mEvaluator.evaluate(fraction, 0, currentY);//从bitmap的左上角y坐标到缩略图的y
                imageViewCopy.setLayoutParams(params);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setShow();
                frameLayout.setVisibility(View.INVISIBLE);
            }
        });
        valueAnimator.setDuration(4000);
        valueAnimator.start();
    }
}
