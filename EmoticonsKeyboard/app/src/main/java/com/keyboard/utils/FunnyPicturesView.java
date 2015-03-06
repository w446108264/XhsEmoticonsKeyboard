package com.keyboard.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.keyboard.R;

public class FunnyPicturesView extends View {

    private GestureDetector gd;
    private int scrollingOffsetY;
    private int scrollingOffsetX;
    Bitmap mBitmap;

    int left;
    int top;
    float changeItemX = 0;
    float changeItemY = 0;

    public FunnyPicturesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gd = new GestureDetector(context, new InnerGestureListener());

        mBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.we);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int bmpHeight = mBitmap.getHeight();
        int bmpWidth = mBitmap.getWidth();
        int rectWidth = w;
        int rectHeight = h;
        left = (int) ((float) (bmpWidth - rectWidth) / 2);
        top = (int) ((float) (bmpHeight - rectHeight) / 2);
        changeItemX = left / split;
        changeItemY = top / split;
    }

    int split = 100;
    int max = 10;
    float min = (float)0.5;

    public void update(float distanceX, float distanceY) {
        if( (distanceX != 0 &&  (distanceX > 0 && distanceX > min)  ||  (distanceX < 0 && distanceX < -min) )
                ||  (distanceY != 0 &&   (distanceY > 0 && distanceY > min)  ||  (distanceY < 0 && distanceY < -min) )  ){
            if (distanceX > max) {
                distanceX = max;
            }
            if (distanceX < -max) {
                distanceX = -max;
            }

            if (distanceY > max) {
                distanceY = max;
            }
            if (distanceY < -max) {
                distanceY = -max;
            }

            scrollingOffsetX += distanceX * changeItemX;
            scrollingOffsetY += distanceY * changeItemY;

            if (scrollingOffsetX > left) {
                scrollingOffsetX = left;
            } else if (scrollingOffsetX < -left) {
                scrollingOffsetX = -left;
            }

            if (scrollingOffsetY > top) {
                scrollingOffsetY = top;
            } else if (scrollingOffsetY < -top) {
                scrollingOffsetY = -top;
            }
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(scrollingOffsetX, scrollingOffsetY);
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, -left, -top, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gd.setIsLongpressEnabled(true);
        return gd.onTouchEvent(event);
    }

    public Bitmap bitmapScale(int ivWidth, Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = ivWidth;
        int newHeight = (int) ((float) height) * newWidth / width;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    class InnerGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            scrollingOffsetY += -distanceY;
            scrollingOffsetX += -distanceX;
            invalidate();
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }
    }
}