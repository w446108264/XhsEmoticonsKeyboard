package com.xhsemoticonskeyboard.qq;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xhsemoticonskeyboard.R;

import java.io.IOException;

import sj.keyboard.data.PageSetEntity;
import sj.keyboard.utils.imageloader.ImageLoader;
import sj.keyboard.widget.EmoticonsToolBarView;

public class QqEmoticonsToolBarView extends EmoticonsToolBarView {

    public QqEmoticonsToolBarView(Context context) {
        super(context);
    }

    public QqEmoticonsToolBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected View getCommonItemToolBtn() {
        return mInflater == null ? null : mInflater.inflate(R.layout.item_toolbtn_qq, null);
    }

    protected void initItemToolBtn(View toolBtnView, int rec, final PageSetEntity pageSetEntity, OnClickListener onClickListener){
        ImageView iv_icon = (ImageView) toolBtnView.findViewById(R.id.iv_icon);
        if (rec > 0) {
            iv_icon.setBackgroundResource(rec);
        }
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(mBtnWidth, LayoutParams.MATCH_PARENT);
        iv_icon.setLayoutParams(imgParams);
        if (pageSetEntity != null) {
            iv_icon.setTag(R.id.id_tag_pageset, pageSetEntity);
            try {
                ImageLoader.getInstance(mContext).displayImage(pageSetEntity.getIconUri(), iv_icon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        toolBtnView.setOnClickListener(onClickListener != null ? onClickListener : new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListeners != null && pageSetEntity != null) {
                    mItemClickListeners.onToolBarItemClick(pageSetEntity);
                }
            }
        });
    }
}

