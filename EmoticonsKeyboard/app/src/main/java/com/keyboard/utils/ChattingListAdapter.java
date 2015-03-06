package com.keyboard.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.keyboard.R;
import com.keyboard.activity.WeActivity;
import com.keyboard.utils.imageloader.ImageLoader;
import com.keyboard.view.EmoticonsEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChattingListAdapter extends BaseAdapter {

    private final int VIEW_TYPE_COUNT = 8;
    final int VIEW_TYPE_LEFT_TEXT = 0;
    final int VIEW_TYPE_LEFT_IMAGE = 1;

    private Activity mActivity;
    private LayoutInflater mInflater;
    private List<ImMsgBean> mData;

    public ChattingListAdapter(Activity activity) {
        this.mActivity = activity;
        mInflater = LayoutInflater.from(activity);
    }

    public void addData(List<ImMsgBean> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        if (mData == null) {
            mData = new ArrayList<ImMsgBean>();
        }
        for (ImMsgBean bean : data) {
            addData(bean, false, false);
        }
        this.notifyDataSetChanged();
    }

    /**
     * @param bean
     * @param isNotifyDataSetChanged 是否刷新
     * @param isFromHead 是否从前端插入
     */
    public void addData(ImMsgBean bean, boolean isNotifyDataSetChanged, boolean isFromHead) {
        if (bean == null) {
            return;
        }
        if (mData == null) {
            mData = new ArrayList<ImMsgBean>();
        }

        if(bean.getMsgType() <= 0){
            String content = bean.getContent();
            if (content != null) {
                if (content.indexOf("[img]") >= 0) {
                    bean.setImage(content.replace("[img]", ""));
                    bean.setMsgType(ImMsgBean.CHAT_MSGTYPE_IMG);
                } else {
                    bean.setMsgType(ImMsgBean.CHAT_MSGTYPE_TEXT);
                }
            }
        }

        if (isFromHead) {
            mData.add(0, bean);
        } else {
            mData.add(bean);
        }

        if (isNotifyDataSetChanged) {
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) == null) {
            return -1;
        } else {
            return mData.get(position).getMsgType() == ImMsgBean.CHAT_MSGTYPE_TEXT ? VIEW_TYPE_LEFT_TEXT : VIEW_TYPE_LEFT_IMAGE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImMsgBean bean = mData.get(position);
        int type = getItemViewType(position);
        View holderView = null;
        switch (type) {
            case VIEW_TYPE_LEFT_TEXT:
                ViewHolderLeftText holder = null;
                if (convertView == null) {
                    holder = new ViewHolderLeftText();
                    holderView = mInflater.inflate(R.layout.listitem_cha_left_text, null);
                    holderView.setFocusable(true);
                    holder.iv_avatar = (ImageView) holderView.findViewById(R.id.iv_avatar);
                    holder.tv_content = (TextView) holderView.findViewById(R.id.tv_content);
                    holderView.setTag(holder);
                    convertView = holderView;
                } else {
                    holder = (ViewHolderLeftText) convertView.getTag();
                }
                disPlayLeftTextView(position, convertView, holder, bean);
                break;
            case VIEW_TYPE_LEFT_IMAGE:
                ViewHolderLeftImage imageHolder = null;
                if (convertView == null) {
                    imageHolder = new ViewHolderLeftImage();
                    holderView = mInflater.inflate(R.layout.listitem_cha_left_image, null);
                    holderView.setFocusable(true);
                    imageHolder.iv_avatar = (ImageView) holderView.findViewById(R.id.iv_avatar);
                    imageHolder.iv_image = (ImageView) holderView.findViewById(R.id.iv_image);
                    holderView.setTag(imageHolder);
                    convertView = holderView;
                } else {
                    imageHolder = (ViewHolderLeftImage) convertView.getTag();
                }
                disPlayRightTextView(position, convertView, imageHolder, bean);
                break;
            default:
                convertView = new View(mActivity);
                break;
        }
        return convertView;
    }

    public void disPlayLeftTextView(int position , View view , ViewHolderLeftText holder , ImMsgBean bean){
        setContent(holder.tv_content,bean.getContent());
        holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mActivity,WeActivity.class);
                mActivity.startActivity(intent);
            }
        });
    }

    public void setContent(TextView tv_content, String content) {
        tv_content.setText(content);
        CharSequence text = tv_content.getText();
        SpannableStringBuilder style=new SpannableStringBuilder(text);
        style.clearSpans();
        EmoticonsRexgexUtils.setTextFace(mActivity, tv_content, content, style, EmoticonsEditText.WRAP_FONT, EmoticonsEditText.WRAP_FONT);
        tv_content.setText(style);
    }

    public void disPlayRightTextView(int position , View view , ViewHolderLeftImage holder , ImMsgBean bean){
        holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mActivity,WeActivity.class);
                mActivity.startActivity(intent);
            }
        });
        try {
            ImageLoader.getInstance(mActivity).displayImage(bean.getContent(),holder.iv_image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final class ViewHolderLeftText {
        public ImageView iv_avatar;
        public TextView tv_content;
    }

    public final class ViewHolderLeftImage {
        public ImageView iv_avatar;
        public ImageView iv_image;
    }
}