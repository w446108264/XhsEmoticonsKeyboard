package com.xhsemoticonskeyboard.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import sj.keyboard.data.PageSetEntity;
import sj.keyboard.widget.EmoticonsIndicatorView;

public class AnimEmoticonsIndicatorView extends EmoticonsIndicatorView {

    protected AnimatorSet mPlayToAnimatorSet;
    protected AnimatorSet mPlayByInAnimatorSet;
    protected AnimatorSet mPlayByOutAnimatorSet;

    public AnimEmoticonsIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void playTo(int position, PageSetEntity pageSetEntity) {
        if (!checkPageSetEntity(pageSetEntity)) {
            return;
        }

        updateIndicatorCount(pageSetEntity.getPageCount());

        for (ImageView iv : mImageViews) {
            iv.setImageDrawable(mDrawableNomal);
        }
        mImageViews.get(position).setImageDrawable(mDrawableSelect);
        final ImageView imageViewStrat = mImageViews.get(position);
        ObjectAnimator animIn1 = ObjectAnimator.ofFloat(imageViewStrat, "scaleX", 0.25f, 1.0f);
        ObjectAnimator animIn2 = ObjectAnimator.ofFloat(imageViewStrat, "scaleY", 0.25f, 1.0f);

        if (mPlayToAnimatorSet != null && mPlayToAnimatorSet.isRunning()) {
            mPlayToAnimatorSet.cancel();
            mPlayToAnimatorSet = null;
        }
        mPlayToAnimatorSet = new AnimatorSet();
        mPlayToAnimatorSet.play(animIn1).with(animIn2);
        mPlayToAnimatorSet.setDuration(100);
        mPlayToAnimatorSet.start();
    }

    public void playBy(int startPosition, int nextPosition, PageSetEntity pageSetEntity) {
        if (!checkPageSetEntity(pageSetEntity)) {
            return;
        }

        updateIndicatorCount(pageSetEntity.getPageCount());

        boolean isShowInAnimOnly = false;
        if (startPosition < 0 || nextPosition < 0 || nextPosition == startPosition) {
            startPosition = nextPosition = 0;
        }

        if (startPosition < 0) {
            isShowInAnimOnly = true;
            startPosition = nextPosition = 0;
        }

        final ImageView imageViewStrat = mImageViews.get(startPosition);
        final ImageView imageViewNext = mImageViews.get(nextPosition);

        ObjectAnimator anim1 = ObjectAnimator.ofFloat(imageViewStrat, "scaleX", 1.0f, 0.25f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(imageViewStrat, "scaleY", 1.0f, 0.25f);

        if (mPlayByOutAnimatorSet != null && mPlayByOutAnimatorSet.isRunning()) {
            mPlayByOutAnimatorSet.cancel();
            mPlayByOutAnimatorSet = null;
        }
        mPlayByOutAnimatorSet = new AnimatorSet();
        mPlayByOutAnimatorSet.play(anim1).with(anim2);
        mPlayByOutAnimatorSet.setDuration(100);

        ObjectAnimator animIn1 = ObjectAnimator.ofFloat(imageViewNext, "scaleX", 0.25f, 1.0f);
        ObjectAnimator animIn2 = ObjectAnimator.ofFloat(imageViewNext, "scaleY", 0.25f, 1.0f);

        if (mPlayByInAnimatorSet != null && mPlayByInAnimatorSet.isRunning()) {
            mPlayByInAnimatorSet.cancel();
            mPlayByInAnimatorSet = null;
        }
        mPlayByInAnimatorSet = new AnimatorSet();
        mPlayByInAnimatorSet.play(animIn1).with(animIn2);
        mPlayByInAnimatorSet.setDuration(100);

        if (isShowInAnimOnly) {
            mPlayByInAnimatorSet.start();
            return;
        }

        anim1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imageViewStrat.setImageDrawable(mDrawableNomal);
                ObjectAnimator animFil1l = ObjectAnimator.ofFloat(imageViewStrat, "scaleX", 1.0f);
                ObjectAnimator animFill2 = ObjectAnimator.ofFloat(imageViewStrat, "scaleY", 1.0f);
                AnimatorSet mFillAnimatorSet = new AnimatorSet();
                mFillAnimatorSet.play(animFil1l).with(animFill2);
                mFillAnimatorSet.start();
                imageViewNext.setImageDrawable(mDrawableSelect);
                mPlayByInAnimatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        mPlayByOutAnimatorSet.start();
    }
}
