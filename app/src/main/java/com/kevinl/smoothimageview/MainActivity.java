package com.kevinl.smoothimageview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kevinl.smoothimageview.PointAnimator.PointViewActivity;


/**
 * Author: liuk
 * Created at: 15/12/15
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private MainAdapter mainAdapter;

    private String[] urls = {"http://img.1985t.com/uploads/attaches/2013/03/10471-GAAGE6.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/8435e5dde71190efa7aa1231ca1b9d16fcfa608f.jpg",
            "http://c.hiphotos.baidu.com/image/pic/item/6a600c338744ebf84821a4edddf9d72a6159a794.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/6c224f4a20a44623c458fa889c22720e0df3d74e.jpg"};

    private FrameLayout frameLayout1;
    private FrameLayout frameLayout2;
    private ImageView imageView1;
    private ImageView imageView2;
    private ViewWrapper wrapper;
    private int width;
    private int height;
    private int screenWidth;
    private int screenHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        mainAdapter = new MainAdapter(this, urls);
        recyclerView.setAdapter(mainAdapter);

        frameLayout1 = (FrameLayout) findViewById(R.id.frameLayout1);
        frameLayout2 = (FrameLayout) findViewById(R.id.frameLayout2);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);

        wrapper = new ViewWrapper(frameLayout2);
        imageView();
    }

    private boolean isOut = false;
    private void imageView() {
        Glide.with(this)
                .load("http://img.1985t.com/uploads/attaches/2013/03/10471-GAAGE6.jpg")
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .priority(Priority.IMMEDIATE)
                .into(imageView1);
        Glide.with(this)
                .load("http://img.1985t.com/uploads/attaches/2013/03/10471-GAAGE6.jpg")
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .priority(Priority.IMMEDIATE)
                .into(imageView2);

        width = dip2px(this, 200);
        height = width;

        screenWidth = getScreenWidth(this);
        screenHeight = getScreenHeight(this);

        Log.e("zgf", "====width====" + width);
        Log.e("zgf", "====height====" + height);
        Log.e("zgf", "====screenWidth====" + screenWidth);
        Log.e("zgf", "====screenHeight====" + screenHeight);

        frameLayout2.setVisibility(View.VISIBLE);

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (isOut) {
                    isOut = false;

                    animationOut();
                } else {
                    isOut = true;

                    animationIn();
                }*/

                Intent intent = new Intent(MainActivity.this, BitmapActivity.class);
                startActivity(intent);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PointViewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void animationIn() {
        int[] position = new int[2];
        imageView1.getLocationInWindow(position);
        wrapper.setPivotX(position[0]);
        wrapper.setPivotY(position[1]);

        AnimatorSet animator = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofInt(wrapper, "width", width, screenWidth);
        ObjectAnimator scaleY = ObjectAnimator.ofInt(wrapper, "height", height, screenHeight);

        animator.setDuration(4000);
        animator.setInterpolator(new LinearInterpolator());
        animator.play(scaleX).with(scaleY);
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

//                frameLayout2.setVisibility(View.INVISIBLE);
            }
        });
        animator.start();
    }

    private void animationOut() {
        int[] position = new int[2];
        imageView1.getLocationInWindow(position);
        wrapper.setPivotX(position[0]);
        wrapper.setPivotY(position[1]);

        AnimatorSet animator = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofInt(wrapper, "width", screenWidth, width);
        ObjectAnimator scaleY = ObjectAnimator.ofInt(wrapper, "height", screenHeight, height);

        animator.setDuration(4000);
        animator.setInterpolator(new LinearInterpolator());
        animator.play(scaleX).with(scaleY);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

//                frameLayout2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        animator.start();
    }

    // 提供ViewWrapper类,用于包装View对象
    // 本例:包装Button对象
    private static class ViewWrapper {
        private View mTarget;

        // 构造方法:传入需要包装的对象
        public ViewWrapper(View target) {
            mTarget = target;
        }

        // 为宽度设置get（） & set（）
        public int getWidth() {
            return mTarget.getLayoutParams().width;
        }

        public void setWidth(int width) {
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }

        public int getHeight() {
            return mTarget.getLayoutParams().height;
        }

        public void setHeight(int height) {
            mTarget.getLayoutParams().height = height;
            mTarget.requestLayout();
        }

        public void setPivotX(int x) {
            mTarget.setPivotX(x);
        }

        public void setPivotY(int y) {
            mTarget.setPivotY(y);
        }
    }

    public int dip2px(Context cc, float dipValue) {
        final float scale = cc.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels + getNavigationBarHeight(activity);
    }

    public static int getNavigationBarHeight(Activity activity) {
        if (!isNavigationBarShow(activity)) {
            return 0;
        }
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        //获取NavigationBar的高度
        return resources.getDimensionPixelSize(resourceId);
    }

    public static boolean isNavigationBarShow(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = context.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if (menu || back) {
                return false;
            } else {
                return true;
            }
        }
    }
}
