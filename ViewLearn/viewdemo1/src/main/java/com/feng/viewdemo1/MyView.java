package com.feng.viewdemo1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

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
        for (int i = 0; i < mBitmaps.length; i++) {
            mBitmaps[i] = BitmapFactory.decodeResource(getResources(), R.drawable.haha);
            mOffSetX[i] = (i - 1) * 600 + 100;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw   mSlideX = " + mSlideX);
        for (int i = 0; i < mBitmaps.length; i++) {
            Log.d(TAG, "mOffSetX[" + i + "] = " + mOffSetX[i]);
            mMatrix.reset();
            mMatrix.postScale(0.7f, 1f, 0.5f, 0.5f);
            mMatrix.postTranslate(mCurSlideX + mOffSetX[i], 200);
            canvas.drawBitmap(mBitmaps[i], mMatrix, mPaint);
//            if (mCurSlideX < -300) {
//                Bitmap temp = mBitmaps[0];
//                for (int j = 0; j < mBitmaps.length; j++) {
//                    if (j == mBitmaps.length - 1) {
//                        mBitmaps[j] = temp;
//                    } else {
//                        mBitmaps[j] = mBitmaps[j + 1];
//                    }
//                }
//                mOffSetX[0] += 1880;
//            } else if (mCurSlideX > 300) {
//                Bitmap temp2 = mBitmaps[mBitmaps.length - 1];
//                for (int j = mBitmaps.length - 1; j >= 0; j--) {
//                    if (j == 0) {
//                        mBitmaps[0] = temp2;
//                    } else {
//                        mBitmaps[j] = mBitmaps[j - 1];
//                    }
//                }
//                mOffSetX[mBitmapCount - 1] -= 1280;
//            }
            if (mCurSlideX + mOffSetX[i] < -500) {
                mOffSetX[i] += 3000;
            } else if (mCurSlideX + mOffSetX[i] > 2500) {
                mOffSetX[i] -= 3000;
            }
        }
//        if (mCurSlideX + mOffSetX[0] < -300) {
//            mOffSetX[0] += 2500;
//        } else if (mCurSlideX + mOffSetX[4] > 2800) {
//            mOffSetX[4] -= 2500;
//        }
    }

    float mCurX = 0;
    float mSlideX = 0;
    float mCurSlideX = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCurX = event.getX();
                Log.d(TAG, "ACTION_DOWN mCurX = " + mCurX);
                return true;
            case MotionEvent.ACTION_MOVE:
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
                    } else if (mCurSlideX + mOffSetX[i] > 2500) {
                        mOffSetX[i] -= 3000;
                    }
                }
                break;

        }
        return super.onTouchEvent(event);
    }
}
