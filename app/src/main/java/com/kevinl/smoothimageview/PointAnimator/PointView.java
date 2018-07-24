package com.kevinl.smoothimageview.PointAnimator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;


public class PointView extends View {
    private int width;
    private int height;
    private Paint mPaint;
    private Point currentPoint;
    private int mRadius;

    public PointView(Context context) {
        super(context);
        init();
    }

    public PointView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();

        mRadius = 20;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        if (currentPoint == null) {
            startAnimationMotion();// 执行动画
        }

        mPaint.setColor(Color.BLACK);
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, mPaint);
        mPaint.setColor(Color.MAGENTA);
        canvas.drawCircle(currentPoint.getX(), currentPoint.getY(), mRadius, mPaint);
    }

    private void startAnimationMotion() {
        Point startPoint = new Point(mRadius, getHeight() / 2);
        Point endPoint = new Point(getWidth() - mRadius, 0);
        ValueAnimator animator = ValueAnimator.ofObject(new OscillationEvaluator(), startPoint, endPoint);
        animator.setDuration(7000).setRepeatCount(3);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.setInterpolator(new LinearInterpolator());//设置插值器
        animator.start();
    }

}
