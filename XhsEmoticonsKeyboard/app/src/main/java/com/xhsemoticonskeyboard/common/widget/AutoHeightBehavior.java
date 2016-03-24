package com.xhsemoticonskeyboard.common.widget;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import sj.keyboard.widget.AutoHeightLayout;

public class AutoHeightBehavior extends AppBarLayout.ScrollingViewBehavior {

    public AutoHeightBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        boolean b = super.onDependentViewChanged(parent, child, dependency);
        if (child instanceof AutoHeightLayout) {
            ((AutoHeightLayout) child).updateMaxParentHeight(child.getHeight() - dependency.getHeight());
            if(onDependentViewChangedListener != null){
                onDependentViewChangedListener.onDependentViewChangedListener(parent, child, dependency);
            }
        }
        return b;
    }

    private OnDependentViewChangedListener onDependentViewChangedListener;

    public interface OnDependentViewChangedListener {
        void onDependentViewChangedListener(CoordinatorLayout parent, View child, View dependency);
    }

    public void setOnDependentViewChangedListener(OnDependentViewChangedListener listener) {
        this.onDependentViewChangedListener = listener;
    }
}
