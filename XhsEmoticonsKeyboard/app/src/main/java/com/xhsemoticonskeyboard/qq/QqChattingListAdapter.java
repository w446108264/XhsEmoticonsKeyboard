package com.xhsemoticonskeyboard.qq;

import android.app.Activity;
import android.widget.TextView;

import com.xhsemoticonskeyboard.common.adapter.ChattingListAdapter;

public class QqChattingListAdapter extends ChattingListAdapter {

    public QqChattingListAdapter(Activity activity) {
        super(activity);
    }

    public void setContent(TextView tv_content, String content) {
        QqUtils.spannableEmoticonFilter(tv_content, content);
    }
}