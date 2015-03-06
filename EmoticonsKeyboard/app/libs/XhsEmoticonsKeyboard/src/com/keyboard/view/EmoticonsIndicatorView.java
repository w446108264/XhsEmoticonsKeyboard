package com.keyboard.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.keyboard.utils.Utils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;


public class EmoticonsIndicatorView extends LinearLayout{

    private Context mContext;
    private ArrayList<ImageView> mImageViews ;
    private Bitmap bmpSelect;
    private Bitmap bmpNomal;
    private int mHeight = 16;
    private int mMaxHeight ;
    private AnimatorSet mPlayToAnimatorSet;
    private AnimatorSet mPlayByInAnimatorSet;
    private AnimatorSet mPlayByOutAnimatorSet;

    public EmoticonsIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.setOrientation(HORIZONTAL);

        mMaxHeight = Utils.dip2px(mContext, mHeight);
        bmpSelect= BitmapFactory.decodeResource(getResources(), R.drawable.indicator_point_select);
        bmpNomal= BitmapFactory.decodeResource(getResources(), R.drawable.indicator_point_nomal);
    }

    public void init(int count){
        if(mImageViews != null){
            return;
        }
        mImageViews = new ArrayList<ImageView>();
        for(int i = 0 ; i < count ; i++){
            RelativeLayout rl = new RelativeLayout(mContext);
            LayoutParams params = new LinearLayout.LayoutParams(mMaxHeight,mMaxHeight);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(ResizeLayout.CENTER_IN_PARENT);
            ImageView imageView = new ImageView(mContext);

            if(i == 0){
                imageView.setImageBitmap(bmpSelect);
                rl.addView(imageView, layoutParams);
            }
            else{
                imageView.setImageBitmap(bmpNomal);
                rl.addView(imageView, layoutParams);
            }
            this.addView(rl, params);
            mImageViews.add(imageView);
        }
    }

    public void setIndicatorCount(int count){
        if(mImageViews == null || count > mImageViews.size()){
            return;
        }
        for(int i = 0 ; i < mImageViews.size() ; i++){
            if(i >= count){
                mImageViews.get(i).setVisibility(GONE);
                ((View)mImageViews.get(i).getParent()).setVisibility(GONE);
            }
            else {
                mImageViews.get(i).setVisibility(VISIBLE);
                ((View)mImageViews.get(i).getParent()).setVisibility(VISIBLE);
            }
        }
    }

    public void playTo(int position){
        for(ImageView iv : mImageViews){
            iv.setImageBitmap(bmpNomal);
        }
        mImageViews.get(position).setImageBitmap(bmpSelect);
        final ImageView imageViewStrat = mImageViews.get(position);
        ObjectAnimator animIn1 = ObjectAnimator.ofFloat(imageViewStrat, "scaleX", 0.25f, 1.0f);
        ObjectAnimator animIn2 = ObjectAnimator.ofFloat(imageViewStrat, "scaleY", 0.25f, 1.0f);

        if(mPlayToAnimatorSet != null && mPlayToAnimatorSet.isRunning()){
            mPlayToAnimatorSet.cancel();
            mPlayToAnimatorSet = null;
        }
        mPlayToAnimatorSet = new AnimatorSet();
        mPlayToAnimatorSet.play(animIn1).with(animIn2);
        mPlayToAnimatorSet.setDuration(100);
        mPlayToAnimatorSet.start();
    }

    public void playBy(int startPosition,int nextPosition){
        boolean isShowInAnimOnly = false;
        if(startPosition < 0 || nextPosition < 0 || nextPosition == startPosition){
            startPosition = nextPosition = 0;
        }

        if(startPosition < 0){
            isShowInAnimOnly = true;
            startPosition = nextPosition = 0;
        }

        final ImageView imageViewStrat = mImageViews.get(startPosition);
        final ImageView imageViewNext = mImageViews.get(nextPosition);

        ObjectAnimator anim1 = ObjectAnimator.ofFloat(imageViewStrat, "scaleX", 1.0f, 0.25f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(imageViewStrat, "scaleY", 1.0f, 0.25f);

        if(mPlayByOutAnimatorSet != null && mPlayByOutAnimatorSet.isRunning()){
            mPlayByOutAnimatorSet.cancel();
            mPlayByOutAnimatorSet = null;
        }
        mPlayByOutAnimatorSet = new AnimatorSet();
        mPlayByOutAnimatorSet.play(anim1).with(anim2);
        mPlayByOutAnimatorSet.setDuration(100);

        ObjectAnimator animIn1 = ObjectAnimator.ofFloat(imageViewNext, "scaleX", 0.25f, 1.0f);
        ObjectAnimator animIn2 = ObjectAnimator.ofFloat(imageViewNext, "scaleY", 0.25f, 1.0f);

        if(mPlayByInAnimatorSet != null && mPlayByInAnimatorSet.isRunning()){
            mPlayByInAnimatorSet.cancel();
            mPlayByInAnimatorSet = null;
        }
        mPlayByInAnimatorSet = new AnimatorSet();
        mPlayByInAnimatorSet.play(animIn1).with(animIn2);
        mPlayByInAnimatorSet.setDuration(100);

        if(isShowInAnimOnly){
            mPlayByInAnimatorSet.start();
            return;
        }

        anim1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) { }

            @Override
            public void onAnimationEnd(Animator animation) {
                imageViewStrat.setImageBitmap(bmpNomal);
                ObjectAnimator animFil1l = ObjectAnimator.ofFloat(imageViewStrat, "scaleX", 1.0f);
                ObjectAnimator animFill2 = ObjectAnimator.ofFloat(imageViewStrat, "scaleY", 1.0f);
                AnimatorSet mFillAnimatorSet = new AnimatorSet();
                mFillAnimatorSet.play(animFil1l).with(animFill2);
                mFillAnimatorSet.start();
                imageViewNext.setImageBitmap(bmpSelect);
                mPlayByInAnimatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) { }

            @Override
            public void onAnimationRepeat(Animator animation) { }
        });
        mPlayByOutAnimatorSet.start();
    }
}
