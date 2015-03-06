package com.keyboard.view;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.keyboard.bean.EmoticonBean;
import com.keyboard.db.DBHelper;
import com.keyboard.utils.imageloader.ImageLoader;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmoticonsEditText extends EditText {

    public static final int WRAP_DRAWABLE = -1;
    public static final int WRAP_FONT = -2;

    private Context mContext;
    private ArrayList<EmoticonBean> emoticonBeanList = null;
    private int mItemHeight;
    private int mItemWidth;
    private int mFontHeight;

    public EmoticonsEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public EmoticonsEditText(Context context) {
        super(context);
    }

    public EmoticonsEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mFontHeight = getFontHeight();
        mItemHeight = mFontHeight;
        mItemWidth = mFontHeight;

        if (emoticonBeanList == null) {
            DBHelper dbHelper = new DBHelper(mContext);
            emoticonBeanList = dbHelper.queryAllEmoticonBeans();
            dbHelper.cleanup();
            if (emoticonBeanList == null) {
                return;
            }
        }
    }

    public void setEmoticonImageSpanSize(int width, int height) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(oldh > 0 && onSizeChangedListener != null){
            onSizeChangedListener.onSizeChanged();
        }
    }

    public static String emojiText(String text){
        String returnTextString = text;
        //Pattern to match
//        Pattern pattern = Pattern.compile("\\\\[[^\\\\]]+\\\\]", Pattern.CASE_INSENSITIVE);
        Pattern pattern = Pattern . compile (
                "[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]" ,
                Pattern . UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
        Matcher matcher = pattern.matcher(text);
        Log.i("","");
        while (matcher.find()) {
            String found = matcher.group();
            Log.i("","");
//            if(EmojiMap.get(found)==null)continue;
//            returnTextString = returnTextString.replace(found, EmojiMap.get(found));
        }
        //Returning text
        return returnTextString;
    }

    @Override
    protected void onTextChanged(CharSequence arg0, int start, int lengthBefore, int after) {
        super.onTextChanged(arg0, start, lengthBefore, after);
        emojiText(arg0.toString());
        if (after > 0) {
            int end = start + after;
            String keyStr = arg0.toString().substring(start, end);
            boolean isEmoticonMatcher = false;
            for (EmoticonBean bean : emoticonBeanList) {
                if (!TextUtils.isEmpty(bean.getContent()) && bean.getContent().equals(keyStr)) {
                    Drawable drawable = ImageLoader.getInstance(mContext).getDrawable(bean.getIconUri());
                    if (drawable != null) {
                        int itemHeight;
                        if (mItemHeight == WRAP_DRAWABLE) {
                            itemHeight = drawable.getIntrinsicHeight();
                        } else if (mItemHeight == WRAP_FONT) {
                            itemHeight = mFontHeight;
                        } else {
                            itemHeight = mItemHeight;
                        }

                        int itemWidth;
                        if (mItemWidth == WRAP_DRAWABLE) {
                            itemWidth = drawable.getIntrinsicWidth();
                        } else if (mItemWidth == WRAP_FONT) {
                            itemWidth = mFontHeight;
                        } else {
                            itemWidth = mItemWidth;
                        }

                        drawable.setBounds(0, 0, itemHeight, itemWidth);
                        VerticalImageSpan imageSpan = new VerticalImageSpan(drawable);
                        getText().setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        isEmoticonMatcher = true;
                    }
                }
            }

            if (!isEmoticonMatcher) {
                ImageSpan[] oldSpans = getText().getSpans(start, end, ImageSpan.class);
                if(oldSpans != null){
                    for (int i = 0; i < oldSpans.length; i++) {
                        int startOld = end;
                        int endOld = after + getText().getSpanEnd(oldSpans[i]) - 1;
                        if (startOld >= 0 && endOld > startOld) {
                            ImageSpan imageSpan = new ImageSpan(oldSpans[i].getDrawable(), ImageSpan.ALIGN_BASELINE);
                            getText().removeSpan(oldSpans[i]);
                            getText().setSpan(imageSpan, startOld, endOld, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
            }
        }
        if(onTextChangedInterface != null){
            onTextChangedInterface.onTextChanged(arg0);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } catch (ArrayIndexOutOfBoundsException e) {
            setText(getText().toString());
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void setGravity(int gravity) {
        try {
            super.setGravity(gravity);
        } catch (ArrayIndexOutOfBoundsException e) {
            setText(getText().toString());
            super.setGravity(gravity);
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        try {
            super.setText(text, type);
        } catch (ArrayIndexOutOfBoundsException e) {
            setText(text.toString());
        }
    }

    private int getFontHeight() {
        Paint paint = new Paint();
        paint.setTextSize(getTextSize());
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.bottom - fm.top);
    }

    public interface OnTextChangedInterface {
        void onTextChanged(CharSequence argo);
    }

    OnTextChangedInterface onTextChangedInterface;

    public void setOnTextChangedInterface(OnTextChangedInterface i) {
        onTextChangedInterface = i;
    }


    public interface OnSizeChangedListener {
        void onSizeChanged();
    }

    OnSizeChangedListener onSizeChangedListener;

    public void setOnSizeChangedListener(OnSizeChangedListener i) {
        onSizeChangedListener = i;
    }
}
