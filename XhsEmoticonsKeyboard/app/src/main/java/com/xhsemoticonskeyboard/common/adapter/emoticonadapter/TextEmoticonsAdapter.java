package com.xhsemoticonskeyboard.common.adapter.emoticonadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhsemoticonskeyboard.R;
import com.xhsemoticonskeyboard.common.Constants;

import sj.keyboard.adpater.EmoticonsAdapter;
import sj.keyboard.data.EmoticonEntity;
import sj.keyboard.data.EmoticonPageEntity;
import sj.keyboard.interfaces.EmoticonClickListener;

public class TextEmoticonsAdapter extends EmoticonsAdapter<EmoticonEntity> {

    public TextEmoticonsAdapter(Context context, EmoticonPageEntity emoticonPageEntity, EmoticonClickListener onEmoticonClickListener) {
        super(context, emoticonPageEntity, onEmoticonClickListener);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_emoticon_text, null);
            viewHolder.rootView = convertView;
            viewHolder.ly_root = (LinearLayout) convertView.findViewById(R.id.ly_root);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final boolean isDelBtn = isDelBtn(position);
        final EmoticonEntity emoticonEntity = mData.get(position);
        if (isDelBtn) {
            viewHolder.ly_root.setBackgroundResource(com.keyboard.view.R.drawable.bg_emoticon);
        } else {
            viewHolder.tv_content.setVisibility(View.VISIBLE);
            if (emoticonEntity != null) {
                viewHolder.tv_content.setText(emoticonEntity.getContent());
                viewHolder.ly_root.setBackgroundResource(com.keyboard.view.R.drawable.bg_emoticon);
            }
        }

        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnEmoticonClickListener != null) {
                    mOnEmoticonClickListener.onEmoticonClick(emoticonEntity, Constants.EMOTICON_CLICK_TEXT, isDelBtn);
                }
            }
        });

        updateUI(viewHolder, parent);
        return convertView;
    }

    protected void updateUI(ViewHolder viewHolder, ViewGroup parent) {
        if(mDefalutItemHeight != mItemHeight){
            viewHolder.tv_content.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mItemHeight));
        }
        mItemHeightMax = this.mItemHeightMax != 0 ? this.mItemHeightMax : (int) (mItemHeight * mItemHeightMaxRatio);
        mItemHeightMin = this.mItemHeightMin != 0 ? this.mItemHeightMin : mItemHeight;
        int realItemHeight = ((View) parent.getParent()).getMeasuredHeight() / mEmoticonPageEntity.getLine();
        realItemHeight = Math.min(realItemHeight, mItemHeightMax);
        realItemHeight = Math.max(realItemHeight, mItemHeightMin);
        viewHolder.ly_root.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, realItemHeight));
    }

    public static class ViewHolder {
        public View rootView;
        public LinearLayout ly_root;
        public TextView tv_content;
    }
}