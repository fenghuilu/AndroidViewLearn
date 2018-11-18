package com.feng.viewdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class MyRelativeLayoutLauncherView extends RelativeLayout {
    private final String TAG = MyRelativeLayoutLauncherView.class.getSimpleName();
    Matrix mMatrix;
    Paint mPaint;
    int mItemCount = 5;
    ImageView[] mImgViews = new ImageView[mItemCount];
    float[] mOffSetX = new float[mItemCount];
    private Camera camera;
    Scroller mScroller;

    public MyRelativeLayoutLauncherView(Context context) {
        this(context, null);
    }

    public MyRelativeLayoutLauncherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRelativeLayoutLauncherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    int mScreenWidth;
    int mScreenHeight;

    private void init() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);//display = getWindowManager().getDefaultDisplay();display.getMetrics(dm)（把屏幕尺寸信息赋值给DisplayMetrics dm）;
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
//        width = Math.max(width, height);
        Log.d(TAG, " mScreenWidth = " + mScreenWidth + " mScreenHeight = " + mScreenHeight);
        mMatrix = new Matrix();
        mPaint = new Paint();
        camera = new Camera();
        mScroller = new Scroller(getContext());
        setBackgroundColor(Color.parseColor("#3f51b5"));
//        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setColor(Color.parseColor("#ff4081"));
        mOffSetX[2] = mScreenWidth / 2 - 450 / 2;

        for (int i = 0; i < mImgViews.length; i++) {
            mOffSetX[i] = (i - 2) * 550 + mOffSetX[2];

            mImgViews[i] = new ImageView(getContext());
//            mImgViews[i].setId();
//            LayoutParams layoutParams = new LayoutParams(450, 600);
//            mImgViews[i].setLayoutParams(layoutParams);
            mImgViews[i].setBackgroundColor(Color.BLUE);
            addView(mImgViews[i]);
//            layoutParams.addRule(RelativeLayout);
//            layoutParams.
//            mImgViews[i].layout((int) mOffSetX[i], 200, (int) mOffSetX[i] + mImgViews[i].getWidth(), 800);
        }

        mImgViews[0].setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.p1));
        mImgViews[1].setImageDrawable(getResources().getDrawable(R.drawable.p2));
        mImgViews[2].setImageDrawable(getResources().getDrawable(R.drawable.p3));
        mImgViews[3].setImageDrawable(getResources().getDrawable(R.drawable.p4));
        mImgViews[4].setImageDrawable(getResources().getDrawable(R.drawable.p5));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d("haha", "onLayout ");
        for (int i = 0; i < mImgViews.length; i++) {
            mImgViews[i].layout((int) mOffSetX[i], 200, (int) mOffSetX[i] + 450, 800);
            float centerPosition = (mOffSetX[i] + 450 / 2 - mScreenWidth / 2);
            Log.d(TAG, "  mOffSetX[" + i + "] = " + mOffSetX[i] + "centerPosition = " + centerPosition);
            rotate3dAnim(mImgViews[i], centerPosition);
        }
    }

    private void rotate3dAnim(ImageView imageView, float centerPosition) {
        Log.d(TAG, "  rotate3dAnim centerPosition = " + centerPosition);
        Rotate3dAnimation rotate3dAnimation;
        if (centerPosition >= 0) {
            rotate3dAnimation = new Rotate3dAnimation(0, 30 * centerPosition / 1000, 0, imageView.getHeight() / 2, 0, false);
        } else {
            rotate3dAnimation = new Rotate3dAnimation(0, 30 * centerPosition / 1000, imageView.getWidth(), imageView.getHeight() / 2, 0, false);
        }
//            rotate3dAnimation.setInterpolator(new AccelerateInterpolator());
        rotate3dAnimation.setDuration(1);
        // 动画完成后保持完成的状态
        rotate3dAnimation.setFillAfter(true);
        imageView.startAnimation(rotate3dAnimation);
    }

    private void computeLayout() {
        mCurSlideX = mCurSlideX % 2750;
        int offset = (int) (mCurSlideX / 550);
        if (mCurSlideX < -275) {
            ImageView temp = mImgViews[0];
            mImgViews[0] = mImgViews[1];
            mImgViews[1] = mImgViews[2];
            mImgViews[2] = mImgViews[3];
            mImgViews[3] = mImgViews[4];
            mImgViews[4] = temp;
            mCurSlideX += 550;
        } else if (mCurSlideX > 275) {
            ImageView temp = mImgViews[4];
            mImgViews[4] = mImgViews[3];
            mImgViews[3] = mImgViews[2];
            mImgViews[2] = mImgViews[1];
            mImgViews[1] = mImgViews[0];
            mImgViews[0] = temp;
            mCurSlideX -= 550;
        }
        if (mCurSlideX > 550 || mCurSlideX < -550) {
            computeLayout();
        }
    }

    private void invalidateLayout() {
        computeLayout();
        for (int i = 0; i < mImgViews.length; i++) {
//            Log.d("haha", "mImgViews[" + i + "].getWidth() = " + mImgViews[i].getWidth());
            float centerPosition = (mOffSetX[i] + mCurSlideX) + 450 / 2 - mScreenWidth / 2;
            rotate3dAnim(mImgViews[i], centerPosition);
            mImgViews[i].layout((int) (mOffSetX[i] + mCurSlideX), 200, (int) (mOffSetX[i] + 450 + mCurSlideX), 800);
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
                if (mSlideX > 0) {
                    mCurX = event.getX();
                    mCurSlideX += mSlideX;
                    Log.d(TAG, " mSlideX = " + mSlideX);
                    Log.d(TAG, " mCurX = " + mCurX);
                    invalidateLayout();
                }
                break;
            case MotionEvent.ACTION_UP:
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

        if (xVelocity > 4000) {
            xVelocity = 4000;
        } else if (xVelocity < -4000) {
            xVelocity = -4000;
        }
        post(refrash);
    }

    private void scrollBack() {
//        mImgViews[3].layout();
    }

    Runnable refrash = new Runnable() {
        @Override
        public void run() {
            if (xVelocity < 0) {
                xVelocity = (float) (xVelocity * 0.9);
                Log.d("haha", " run   xVelocity = " + xVelocity);
                if (xVelocity >= -1) {
                    xVelocity = 0;
                    scrollBack();
                    return;
                }
                mCurSlideX += xVelocity;
                mCurSlideX = mCurSlideX % 2750;
                invalidateLayout();
                postDelayed(refrash, 10);
            } else if (xVelocity > 0) {
                xVelocity = (float) (xVelocity * 0.9);
                if (xVelocity <= 1) {
                    xVelocity = 0;
                    scrollBack();
                    return;
                }
                mCurSlideX += xVelocity;
                mCurSlideX = mCurSlideX % 2750;
                invalidateLayout();
                postDelayed(refrash, 10);
            }
        }
    };
}
