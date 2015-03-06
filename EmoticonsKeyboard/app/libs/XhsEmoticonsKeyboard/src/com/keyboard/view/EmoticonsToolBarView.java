package com.keyboard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.keyboard.bean.EmoticonSetBean;
import com.keyboard.utils.EmoticonsKeyboardBuilder;
import com.keyboard.utils.Utils;
import com.keyboard.utils.imageloader.ImageLoader;
import com.keyboard.view.I.IEmoticonsKeyboard;
import com.nineoldandroids.view.ViewHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EmoticonsToolBarView extends RelativeLayout implements IEmoticonsKeyboard {

    private LayoutInflater inflater;
    private Context mContext;
    private HorizontalScrollView hsv_toolbar;
    private LinearLayout ly_tool;

    private List<EmoticonSetBean> mEmoticonSetBeanList;
    private ArrayList<ImageView> mToolBtnList = new ArrayList<ImageView>();
    private int mBtnWidth = 60;

    public EmoticonsToolBarView(Context context) {
        this(context, null);
    }

    public EmoticonsToolBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_emoticonstoolbar, this);
        this.mContext = context;
        findView();
    }

    private void findView() {
        hsv_toolbar = (HorizontalScrollView) findViewById(R.id.hsv_toolbar);
        ly_tool = (LinearLayout) findViewById(R.id.ly_tool);
    }

    private void scrollToBtnPosition(final int position){
        int childCount = ly_tool.getChildCount();
        if(position < childCount){
            hsv_toolbar.post(new Runnable() {
                @Override
                public void run() {
                    int mScrollX = hsv_toolbar.getScrollX();

                    int childX = (int)ViewHelper.getX(ly_tool.getChildAt(position));

                    if(childX < mScrollX){
                        hsv_toolbar.scrollTo(childX,0);
                        return;
                    }

                    int childWidth = (int)ly_tool.getChildAt(position).getWidth();
                    int hsvWidth = hsv_toolbar.getWidth();
                    int childRight = childX + childWidth;
                    int scrollRight = mScrollX + hsvWidth;

                    if(childRight > scrollRight){
                        hsv_toolbar.scrollTo(childRight - scrollRight,0);
                        return;
                    }
                }
            });
        }
    }

    public void setToolBtnSelect(int select) {
        scrollToBtnPosition(select);
        for (int i = 0; i < mToolBtnList.size(); i++) {
            if (select == i) {
                mToolBtnList.get(i).setBackgroundColor(getResources().getColor(R.color.toolbar_btn_select));
            } else {
                mToolBtnList.get(i).setBackgroundColor(getResources().getColor(R.color.toolbar_btn_nomal));
            }
        }
    }

    public void setBtnWidth(int width){
        mBtnWidth = width;
    }

    public void addData(int rec){
        if(ly_tool != null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View toolBtnView = inflater.inflate(R.layout.item_toolbtn,null);
            ImageView iv_icon = (ImageView)toolBtnView.findViewById(R.id.iv_icon);
            iv_icon.setImageResource(rec);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(Utils.dip2px(mContext,mBtnWidth), LayoutParams.MATCH_PARENT);
            iv_icon.setLayoutParams(imgParams);
            ly_tool.addView(toolBtnView);
            final int position = mToolBtnList.size();
            mToolBtnList.add(iv_icon);
            iv_icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListeners != null && !mItemClickListeners.isEmpty()) {
                        for (OnToolBarItemClickListener listener : mItemClickListeners) {
                            listener.onToolBarItemClick(position);
                        }
                    }
                }
            });
        }
    }

    private int getIdValue(){
        int childCount = getChildCount();
        int id = 1;
        if(childCount == 0){
            return id;
        }
        boolean isKeep = true;
        while (isKeep){
            isKeep = false;
            Random random = new Random();
            id = random.nextInt(100);
            for(int i = 0 ; i < childCount ; i++){
                if(getChildAt(i).getId() == id){
                    isKeep = true;
                    break;
                }
            }
        }
        return id;
    }

    public void addFixedView(View view , boolean isRight){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams hsvParams = (RelativeLayout.LayoutParams) hsv_toolbar.getLayoutParams();
        if(view.getId() <= 0){
            view.setId(getIdValue());
        }
        if(isRight){
            params.addRule(ALIGN_PARENT_RIGHT);
            hsvParams.addRule(LEFT_OF, view.getId());
        }
        else{
            params.addRule(ALIGN_PARENT_LEFT);
            hsvParams.addRule(RIGHT_OF,view.getId());
        }
        addView(view,params);
        hsv_toolbar.setLayoutParams(hsvParams);
    }

    @Override
    public void setBuilder(EmoticonsKeyboardBuilder builder) {
        mEmoticonSetBeanList = builder.builder == null ? null : builder.builder.getEmoticonSetBeanList();
        if(mEmoticonSetBeanList == null){
            return;
        }

        int i = 0;
        for(EmoticonSetBean bean : mEmoticonSetBeanList){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View toolBtnView = inflater.inflate(R.layout.item_toolbtn,null);
            View v_spit = (View)toolBtnView.findViewById(R.id.v_spit);
            ImageView iv_icon = (ImageView)toolBtnView.findViewById(R.id.iv_icon);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(Utils.dip2px(mContext,mBtnWidth), LayoutParams.MATCH_PARENT);
            iv_icon.setLayoutParams(imgParams);
            ly_tool.addView(toolBtnView);

            try {
                ImageLoader.getInstance(mContext).displayImage(bean.getIconUri(),iv_icon);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mToolBtnList.add(iv_icon);

            final int finalI = i;
            iv_icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListeners != null && !mItemClickListeners.isEmpty()) {
                        for (OnToolBarItemClickListener listener : mItemClickListeners) {
                            listener.onToolBarItemClick(finalI);
                        }
                    }
                }
            });
            i++;
        }
        setToolBtnSelect(0);
    }

    private List<OnToolBarItemClickListener> mItemClickListeners;
    public interface OnToolBarItemClickListener {
        void onToolBarItemClick(int position);
    }
    public void addOnToolBarItemClickListener(OnToolBarItemClickListener listener) {
        if (mItemClickListeners == null) {
            mItemClickListeners = new ArrayList<OnToolBarItemClickListener>();
        }
        mItemClickListeners.add(listener);
    }
    public void setOnToolBarItemClickListener(OnToolBarItemClickListener listener) { addOnToolBarItemClickListener(listener);}
}
