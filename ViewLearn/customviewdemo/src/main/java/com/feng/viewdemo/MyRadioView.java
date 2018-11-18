package com.feng.viewdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("AppCompatCustomView")
public class MyRadioView extends View {
    private final String TAG = MyRadioView.class.getSimpleName();
    Paint mPaint;
    float[] mPoints;
    final int LINE_COUNT = 80;//刻度的个数
    final int COUNT = LINE_COUNT * 4;
    final float SHORT_LINE_HEGIHT = 40;//短刻度的长度
    final float LONG_LINE_HEGIHT = 60;//长刻度的长度
    float mHZPosition;//调频的线的位置
    int mHeight;
    int mWidth;
    float mInterval = 0;//刻度间隔

    public MyRadioView(Context context) {
        this(context, null);
    }

    public MyRadioView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRadioView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPoints = new float[COUNT];
        post(mGetWHRunable);
    }

    public void setHZPosition(float hzPosition) {
        mHZPosition = hzPosition;
        invalidate();
    }

    Runnable mGetWHRunable = new Runnable() {
        @Override
        public void run() {
            mHeight = getMeasuredHeight();
            mWidth = getMeasuredWidth();
            mInterval = mWidth / LINE_COUNT;
            Log.d(TAG, "mHeight = " + mHeight + " mWidth = " + mWidth);
        }
    };

    private void refreshPoints() {
        for (int i = 0; i < COUNT; i++) {
            int lineIndex = i / 4;
            int pointIndex = i % 4;
            switch (pointIndex) {
                case 0://startX
                    mPoints[i] = lineIndex * mInterval;
                    break;
                case 1://startY
                    mPoints[i] = mHeight;
                    break;
                case 2://stopX
                    mPoints[i] = lineIndex * mInterval;
                    break;
                case 3://stopY
                    float temp = mPoints[i - 1] - mHZPosition;
                    float shortLineHeight = SHORT_LINE_HEGIHT;
                    float longLineHeight = LONG_LINE_HEGIHT;
                    if (temp < 90 && temp > -90) {
                        double k = Math.cos(Math.PI * temp / 180) + 0.5;
//                        Log.d(TAG, " k = " + k);
                        shortLineHeight = (float) (longLineHeight * k);
                        longLineHeight = (float) (longLineHeight * k);
                    }
                    if (lineIndex % 5 != 0) {
                        if (shortLineHeight < SHORT_LINE_HEGIHT) {
                            shortLineHeight = SHORT_LINE_HEGIHT;
                        }
                        mPoints[i] = mHeight - shortLineHeight;
                    } else {
                        if (longLineHeight < LONG_LINE_HEGIHT) {
                            longLineHeight = LONG_LINE_HEGIHT;
                        }
                        mPoints[i] = mHeight - longLineHeight;
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        refreshPoints();
        canvas.drawColor(Color.BLACK);
        mPaint.setColor(Color.BLUE);
        canvas.drawLines(mPoints, mPaint);
        mPaint.setColor(Color.GREEN);
        canvas.drawLine(mHZPosition, mHeight, mHZPosition, 0, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mHZPosition = event.getX();
                if (mHZPosition > mWidth - 5) {
                    mHZPosition = mWidth - 5;
                } else {
                    invalidate();
                }
//                Log.d(TAG, " onTouchEvent event.getX() = " + event.getX() + " mHZPosition = " + mHZPosition);
                break;
        }
        return true;
    }

}
