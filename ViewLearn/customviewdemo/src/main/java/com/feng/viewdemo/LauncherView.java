package com.feng.viewdemo;

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
import android.view.ViewConfiguration;
import android.view.animation.Animation;

/**
 * Created by 李超峰 on 2018/4/2.
 */
public class LauncherView extends View {
    String TAG = LauncherView.class.getSimpleName();
    Matrix mMatrix;
    Paint mPaint;
    int mBitmapCount = 5;
    Bitmap[] mBitmaps = new Bitmap[mBitmapCount];
    float[] mOffSetX = new float[mBitmapCount];
    private Camera camera;
    private float mMinTouchSlop;

    public LauncherView(Context context) {
        this(context, null);
    }

    public LauncherView(Context context, @Nullable AttributeSet attrs) {
        this(context, null, 0);
    }

    public LauncherView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mMinTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mMatrix = new Matrix();
        mPaint = new Paint();
        camera = new Camera();
        setBackgroundColor(Color.parseColor("#3f51b5"));
//        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setColor(Color.parseColor("#ff4081"));
        for (int i = 0; i < mBitmaps.length; i++) {
            mOffSetX[i] = (i - 1) * 550;
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
        Log.d(TAG, "onDraw   mCurSlideX = " + mCurSlideX);
        refreshData();
        for (int i = 0; i < mBitmaps.length; i++) {
            mMatrix.reset();
//            camera.save();
//            camera.translate(600,-100,0);
//            camera.rotateY(-(960-(mCurSlideX + mOffSetX[i]))*0.1f);
//            camera.rotateY(0);
//            camera.getMatrix(mMatrix);
//            camera.restore();
//            mMatrix.postScale(0.7f, 1f, 0.5f, 0.5f);
            mMatrix.postTranslate(mCurSlideX + mOffSetX[i], 200);
//            mMatrix.postTranslate(mOffSetX[i], 200);

//            mMatrix.preTranslate(-getWidth()/2, -getHeight()/2);
//            mMatrix.postTranslate(getWidth()/2, getHeight()/2);

            canvas.drawBitmap(mBitmaps[i], mMatrix, mPaint);
        }
//        refreshData();
    }

    private Matrix getComputeMatrix(float centerX, float centerY, float mDepthZ, Camera camera, float degrees) {

        Matrix matrix = new Matrix();
        camera.save();
//        if (mReverse) {
//            camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
//        } else {
//            camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
//        }
        camera.translate(0.0f, 0.0f, mDepthZ);
        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
        return matrix;
    }

    @Override
    public void setAnimation(Animation animation) {
        super.setAnimation(animation);
    }

    private void refreshData() {
        if (mCurSlideX < -275) {
            Bitmap temp = mBitmaps[0];
            mBitmaps[0] = mBitmaps[1];
            mBitmaps[1] = mBitmaps[2];
            mBitmaps[2] = mBitmaps[3];
            mBitmaps[3] = mBitmaps[4];
            mBitmaps[4] = temp;
            mCurSlideX += 550;
            mCurSlideX = mCurSlideX % 2750;
        } else if (mCurSlideX > 275) {
            Bitmap temp = mBitmaps[4];
            mBitmaps[4] = mBitmaps[3];
            mBitmaps[3] = mBitmaps[2];
            mBitmaps[2] = mBitmaps[1];
            mBitmaps[1] = mBitmaps[0];
            mBitmaps[0] = temp;
            mCurSlideX -= 550;
            mCurSlideX = mCurSlideX % 2750;
        }
        if (mCurSlideX > 550 || mCurSlideX < -550) {
            refreshData();
        }
    }

    float mDownX = 0;
    float mCurX = 0;
    float mSlideX = 0;
    float mCurSlideX = 0;
    VelocityTracker mVelocityTracker = null;
    boolean mHasDown = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHasDown = true;
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                }
                mVelocityTracker.addMovement(event);

                mCurX = event.getX();
                mDownX = event.getX();
                Log.d(TAG, "ACTION_DOWN mCurX = " + mCurX);
                return true;
            case MotionEvent.ACTION_MOVE:

                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000);
                mSlideX = event.getX() - mCurX;
//                if (Math.abs(mSlideX) >= mMinTouchSlop) {
                if (Math.abs(mSlideX) > 0) {
                    mCurX = event.getX();
                    mCurSlideX += mSlideX;
                    Log.d(TAG, " mSlideX = " + mSlideX);
                    Log.d(TAG, " mCurX = " + mCurX);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mHasDown = false;
                mXVelocity = mVelocityTracker.getXVelocity();
                Log.d("haha", " mVelocityTracker.recycle()");
                mVelocityTracker.recycle();
                mVelocityTracker = null;

                smoothScroll(mXVelocity);
                break;
            case MotionEvent.ACTION_CANCEL:
                break;

        }
        return super.onTouchEvent(event);
    }

    float test = 50;
    float mXVelocity;
    float mSmoothScrollTime = 0;

    private void smoothScroll(float xVelocity) {
        Log.d(TAG, " smoothScroll xVelocity = " + xVelocity);
        Log.e(TAG, " smoothScroll xVelocity = " + xVelocity);
        mXVelocity = xVelocity;
        if (Math.abs(mXVelocity) < 3000) {
            slowScroll();
            return;
        }
        if (mXVelocity > 5000) {
            mXVelocity = 5000;
        } else if (mXVelocity < -5000) {
            mXVelocity = -5000;
        }
        test = Math.abs(mXVelocity / 20);
        mSmoothScrollTime = xVelocity / 500;
        post(refrash);
    }

    private void slowScroll() {
        if (Math.abs(mCurSlideX) < 550 / 2) {
            slowScrollBack();
        } else {
            if (mCurSlideX > 0) {
                slowScrollNext();
            } else if (mCurSlideX < 0) {
                slowScrollPre();
            }
        }
    }

    Runnable mBackRunable = new Runnable() {
        @Override
        public void run() {
            mCurSlideX -= mCurSlideX / 10;
            if (Math.abs(mCurSlideX) <= 5) {
                return;
            }
            invalidate();
            postDelayed(mBackRunable, 50);
        }
    };

    private void slowScrollBack() {
    }

    private void slowScrollNext() {

    }

    private void slowScrollPre() {

    }

    float flag = 0.8f;
    Runnable refrash = new Runnable() {
        @Override
        public void run() {
            if (mHasDown) {
                return;
            }
            if (mXVelocity < 0) {
                mXVelocity = mXVelocity * flag;
                Log.d("haha", " run   mXVelocity = " + mXVelocity);
                if (mXVelocity >= -1) {
                    mXVelocity = 0;
                    slowScroll();
                    return;
                }
                mCurSlideX += mXVelocity;
                invalidate();
                postDelayed(refrash, 2);
            } else if (mXVelocity > 0) {
                mXVelocity = mXVelocity * flag;
                Log.d("haha", " run   mXVelocity = " + mXVelocity);
                if (mXVelocity < 1) {
                    mXVelocity = 0;
                    slowScroll();
                    return;
                }
//                mCurSlideX += test;
//                mCurSlideX -= mXVelocity;
                mCurSlideX += mXVelocity;
                invalidate();
                postDelayed(refrash, 2);
            }
        }
    };
}