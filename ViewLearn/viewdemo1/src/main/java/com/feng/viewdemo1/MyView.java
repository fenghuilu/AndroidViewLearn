package com.feng.viewdemo1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

/**
 * Created by 李超峰 on 2018/4/2.
 */
public class MyView extends View {
    String TAG = MyView.class.getSimpleName();
    Matrix mMatrix;
    Paint mPaint;
    int mBitmapCount = 5;
    Bitmap[] mBitmaps = new Bitmap[mBitmapCount];
    float[] mOffSetX = new float[mBitmapCount];
    private Camera camera;

    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mMatrix = new Matrix();
        mPaint = new Paint();
        camera = new Camera();
        mScroller = new Scroller(getContext());
        setBackgroundColor(Color.parseColor("#3f51b5"));
//        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setColor(Color.parseColor("#ff4081"));
        for (int i = 0; i < mBitmaps.length; i++) {
            mOffSetX[i] = (i - 1) * 550 + 100;
        }
        mBitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.p1);
        mBitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.p2);
        mBitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.p3);
        mBitmaps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.p4);
        mBitmaps[4] = BitmapFactory.decodeResource(getResources(), R.drawable.p5);
        Log.d(TAG, "mBitmaps[0].getHeight() = " + mBitmaps[0].getHeight());
        Log.d(TAG, "mBitmaps[0].getWidth() = " + mBitmaps[0].getWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw   mSlideX = " + mSlideX);
        for (int i = 0; i < mBitmaps.length; i++) {
            Log.d(TAG, "mOffSetX[" + i + "] = " + mOffSetX[i]);
            mMatrix.reset();


//            camera.save();
//            camera.translate(600,-100,0);
//            camera.rotateY(-(960-(mCurSlideX + mOffSetX[i]))*0.1f);
//            camera.rotateY(0);
//            camera.getMatrix(mMatrix);
//            camera.restore();
//            mMatrix.postScale(0.7f, 1f, 0.5f, 0.5f);
            mMatrix.postTranslate(mCurSlideX + mOffSetX[i], 200);

//            mMatrix.preTranslate(-getWidth()/2, -getHeight()/2);
//            mMatrix.postTranslate(getWidth()/2, getHeight()/2);

            canvas.drawBitmap(mBitmaps[i], mMatrix, mPaint);
            if (mCurSlideX + mOffSetX[i] < -500) {
                mOffSetX[i] += 2750;
            } else if (mCurSlideX + mOffSetX[i] > 2000) {
                mOffSetX[i] -= 2750;
            }
        }
//        if (mCurSlideX  < -300) {
//           Bitmap temp =  mBitmaps[0];
//        } else if (mCurSlideX  > 300) {
//            mOffSetX[4] -= 2500;
//        }
    }

    Scroller mScroller;

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            postInvalidate();
        }
    }

    float mCurX = 0;
    float mSlideX = 0;
    float mCurSlideX = 0;
    VelocityTracker mVelocityTracker = null;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                }
                mVelocityTracker.addMovement(event);

                mCurX = event.getX();
                Log.d(TAG, "ACTION_DOWN mCurX = " + mCurX);
                return true;
            case MotionEvent.ACTION_MOVE:

                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(50);

                mSlideX = event.getX() - mCurX;
                mCurX = event.getX();
                mCurSlideX += mSlideX;
                Log.d(TAG, " mSlideX = " + mSlideX);
                Log.d(TAG, " mCurX = " + mCurX);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                for (int i = 0; i < mBitmapCount; i++) {
                    if (mCurSlideX + mOffSetX[i] < -500) {
                        mOffSetX[i] += 3000;
                    } else if (mCurSlideX + mOffSetX[i] > 1900) {
                        mOffSetX[i] -= 3000;
                    }
                }
                smoothScroll();
                if (mVelocityTracker != null) {
                    Log.d("haha", " mVelocityTracker.recycle()");
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;

        }
        return super.onTouchEvent(event);
    }

    float test = 50;
    float xVelocity;

    private void smoothScroll() {
        Log.d(TAG, " smoothScroll");
        xVelocity = mVelocityTracker.getXVelocity();
        Log.d(TAG, " xVelocity = " + xVelocity);
        Log.d("haha", " xVelocity = " + xVelocity);
        post(refrash);
    }

    private void scrollBack() {

    }

    Runnable refrash = new Runnable() {
        @Override
        public void run() {
            if (xVelocity < 0) {
                xVelocity += test;
                Log.d("haha", " run   xVelocity = " + xVelocity);
                if (xVelocity >= 0) {
                    xVelocity = 0;
                    scrollBack();
                    return;
                }
                mCurSlideX += xVelocity;
                invalidate();
                postDelayed(refrash, 50);
            } else if (xVelocity > 0) {
                xVelocity -= test;
                if (xVelocity <= 0) {
                    xVelocity = 0;
                    scrollBack();
                    return;
                }
                mCurSlideX += xVelocity;
                invalidate();
                postDelayed(refrash, 50);
            }
        }
    };
}
