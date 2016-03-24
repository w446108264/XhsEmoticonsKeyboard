package com.xhsemoticonskeyboard.qq;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.xhsemoticonskeyboard.R;
import com.xhsemoticonskeyboard.common.adapter.AppsAdapter;
import com.xhsemoticonskeyboard.common.data.AppBean;

import java.util.ArrayList;

import sj.keyboard.utils.EmoticonsKeyboardUtils;

public class SimpleQqGridView extends RelativeLayout {

    protected View view;
    protected Context context;

    public SimpleQqGridView(Context context) {
        this(context, null);
    }

    public SimpleQqGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_apps, this);
        setBackgroundColor(getResources().getColor(R.color.white));
        init();
    }

    protected void init(){
        GridView gv_apps = (GridView) view.findViewById(R.id.gv_apps);
        gv_apps.setPadding(0,0,0,EmoticonsKeyboardUtils.dip2px(context, 20));
        RelativeLayout.LayoutParams params = (LayoutParams) gv_apps.getLayoutParams();
        params.bottomMargin = EmoticonsKeyboardUtils.dip2px(context, 20);
        gv_apps.setLayoutParams(params);
        gv_apps.setVerticalSpacing(EmoticonsKeyboardUtils.dip2px(context, 18));
        ArrayList<AppBean> mAppBeanList = new ArrayList<>();
        mAppBeanList.add(new AppBean(R.mipmap.lcw, "QQ电话"));
        mAppBeanList.add(new AppBean(R.mipmap.lde, "视频电话"));
        mAppBeanList.add(new AppBean(R.mipmap.ldh, "短视频"));
        mAppBeanList.add(new AppBean(R.mipmap.lcx, "收藏"));
        mAppBeanList.add(new AppBean(R.mipmap.ldc, "发红包"));
        mAppBeanList.add(new AppBean(R.mipmap.ldk, "转账"));
        mAppBeanList.add(new AppBean(R.mipmap.ldf, "悄悄话"));
        mAppBeanList.add(new AppBean(R.mipmap.lcu, "文件"));
        AppsAdapter adapter = new AppsAdapter(getContext(), mAppBeanList);
        gv_apps.setAdapter(adapter);
    }
}
