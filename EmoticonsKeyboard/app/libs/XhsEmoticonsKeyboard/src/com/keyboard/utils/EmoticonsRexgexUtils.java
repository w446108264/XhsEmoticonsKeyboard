package com.keyboard.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.widget.TextView;

import com.keyboard.bean.EmoticonBean;
import com.keyboard.db.DBHelper;
import com.keyboard.utils.imageloader.ImageLoader;
import com.keyboard.view.EmoticonsEditText;
import com.keyboard.view.VerticalImageSpan;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utils
 * @author zhongdaxia 2014-9-2 12:05:55
 */

public class EmoticonsRexgexUtils {

    public static ArrayList<EmoticonBean> emoticonBeanList = null;

    private static int getFontHeight(TextView tv) {
        Paint paint = new Paint();
        paint.setTextSize(tv.getTextSize());
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.bottom - fm.top);
    }

    public static boolean setTextFace(final Context context, TextView tv ,String content, Object spannable, int width, int height) {
        boolean isEmoticonMatcher = false;
        int fontHeight = height;
        if (width == EmoticonsEditText.WRAP_FONT && tv!= null) {
            fontHeight = getFontHeight(tv);
        }

        Pattern p = Pattern.compile("\\[[a-zA-Z0-9\\u4e00-\\u9fa5]+\\]");
        Matcher m = p.matcher(content);
        if(m != null){
            while (m.find()) {
                if (emoticonBeanList == null) {
                    DBHelper dbHelper = new DBHelper(context);
                    emoticonBeanList = dbHelper.queryAllEmoticonBeans();
                    dbHelper.cleanup();
                    if (emoticonBeanList == null) {
                        return isEmoticonMatcher;
                    }
                }

                int start = m.start();
                int end = m.end();
                String key = content.substring(start, end).toString();
                for (EmoticonBean bean : emoticonBeanList) {
                    if (!TextUtils.isEmpty(bean.getContent()) && bean.getContent().equals(key)) {
                        Drawable drawable = ImageLoader.getInstance(context).getDrawable(bean.getIconUri());
                        if (drawable != null) {
                            int itemHeight;
                            if (height == EmoticonsEditText.WRAP_DRAWABLE) {
                                itemHeight = drawable.getIntrinsicHeight();
                            } else if (height == EmoticonsEditText.WRAP_FONT) {
                                itemHeight = fontHeight;
                            } else {
                                itemHeight = height;
                            }

                            int itemWidth;
                            if (width == EmoticonsEditText.WRAP_DRAWABLE) {
                                itemWidth = drawable.getIntrinsicWidth();
                            } else if (width == EmoticonsEditText.WRAP_FONT) {
                                itemWidth = fontHeight;
                            } else {
                                itemWidth = width;
                            }

                            drawable.setBounds(0, 0, itemHeight, itemWidth);

                            VerticalImageSpan imageSpan = new VerticalImageSpan(drawable);
                            if (spannable instanceof SpannableString) {
                                ((SpannableString) spannable).setSpan(imageSpan, start ,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            }
                            if (spannable instanceof SpannableStringBuilder) {
                                ((SpannableStringBuilder) spannable).setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            }
                            isEmoticonMatcher = true;
                        }
                    }
                }
            }
        }
        return isEmoticonMatcher;
    }

}
