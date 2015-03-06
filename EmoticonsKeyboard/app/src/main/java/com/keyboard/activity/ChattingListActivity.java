package com.keyboard.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.keyboard.R;
import com.keyboard.XhsEmoticonsKeyBoardBar;
import com.keyboard.activity.base.BaseActivity;
import com.keyboard.bean.EmoticonBean;
import com.keyboard.utils.AppBean;
import com.keyboard.utils.AppsAdapter;
import com.keyboard.utils.ChattingListAdapter;
import com.keyboard.utils.EmoticonsUtils;
import com.keyboard.utils.ImMsgBean;
import com.keyboard.view.EmoticonsToolBarView;
import com.keyboard.view.I.IView;

import java.util.ArrayList;
import java.util.List;

public class ChattingListActivity extends BaseActivity  {

    XhsEmoticonsKeyBoardBar kv_bar;
    ListView lv_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        initTopBar("Chatting List");
        initLeftBtn(true,R.drawable.common_head_btn_back);

        findView();
        initEvent();
    }

    public void findView(){
        kv_bar = (XhsEmoticonsKeyBoardBar) findViewById(R.id.kv_bar);
        kv_bar.setBuilder(EmoticonsUtils.getBuilder(this));
        kv_bar.addToolView(com.keyboard.view.R.drawable.icon_face_pop);
        kv_bar.addToolView(com.keyboard.view.R.drawable.icon_face_pop);
        kv_bar.addToolView(com.keyboard.view.R.drawable.icon_face_pop);
        kv_bar.addToolView(com.keyboard.view.R.drawable.icon_face_pop);

//        TextView tvLeft = new TextView(this);
//        tvLeft.setText("LETF");
//        tvLeft.setGravity(Gravity.CENTER);
//        kv_bar.addFixedView(tvLeft,false);

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View toolBtnView = inflater.inflate(R.layout.view_toolbtn_right_simple,null);
        toolBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kv_bar.del();
            }
        });
        kv_bar.addFixedView(toolBtnView,true);

        View view =  inflater.inflate(R.layout.view_apps, null);
        kv_bar.add(view);

        TextView view2 =  (TextView)inflater.inflate(R.layout.view_test, null);
        view2.setText("PAGE 3");
        kv_bar.add(view2);
        TextView view3 =  (TextView)inflater.inflate(R.layout.view_test, null);
        view3.setText("PAGE 4");
        kv_bar.add(view3);
        TextView view4 =  (TextView)inflater.inflate(R.layout.view_test, null);
        view4.setText("PAGE 5");
        kv_bar.add(view4);


        GridView gv_apps = (GridView)view.findViewById(R.id.gv_apps);
        ArrayList<AppBean> mAppBeanList = new ArrayList<AppBean>();
        String[] funcArray = getResources().getStringArray(com.keyboard.view.R.array.apps_func);
        String[] funcIconArray = getResources().getStringArray(com.keyboard.view.R.array.apps_func_icon);
        for (int i = 0; i < funcArray.length; i++) {
            AppBean bean = new AppBean();
            bean.setId(i);
            bean.setIcon(funcIconArray[i]);
            bean.setFuncName(funcArray[i]);
            mAppBeanList.add(bean);
        }

        AppsAdapter adapter = new AppsAdapter(this, mAppBeanList);
        gv_apps.setAdapter(adapter);

        kv_bar.getEmoticonsToolBarView().addOnToolBarItemClickListener(new EmoticonsToolBarView.OnToolBarItemClickListener() {
            @Override
            public void onToolBarItemClick(int position) {
                Toast.makeText(ChattingListActivity.this, "EmoticonsToolBarView Click : " + position, Toast.LENGTH_SHORT).show();
                if (position == 3) {
                    kv_bar.show(3);
                } else if (position == 4) {
                    kv_bar.show(4);
                } else if (position == 5) {
                    kv_bar.show(5);
                }
            }
        });

        kv_bar.getEmoticonsPageView().addIViewListener(new IView() {
            @Override
            public void onItemClick(EmoticonBean bean) {
                if(bean.getEventType() == EmoticonBean.FACE_TYPE_USERDEF){
                    ImMsgBean imMsgBean = new ImMsgBean();
                    imMsgBean.setContent(bean.getIconUri());
                    imMsgBean.setMsgType(ImMsgBean.CHAT_MSGTYPE_IMG);
                    mChattingListAdapter.addData(imMsgBean,true,false);
                    lv_chat.setSelection(lv_chat.getBottom());
                }
            }

            @Override
            public void onItemDisplay(EmoticonBean bean) {

            }

            @Override
            public void onPageChangeTo(int position) {

            }
        });

        kv_bar.setOnKeyBoardBarViewListener(new XhsEmoticonsKeyBoardBar.KeyBoardBarViewListener() {
            @Override
            public void OnKeyBoardStateChange(int state, int height) {
                lv_chat.post(new Runnable() {
                    @Override
                    public void run() {
                        lv_chat.setSelection(lv_chat.getAdapter().getCount() - 1);
                    }
                });
            }

            @Override
            public void OnSendBtnClick(String msg) {
                if(!TextUtils.isEmpty(msg)){
                    ImMsgBean bean = new ImMsgBean();
                    bean.setContent(msg);
                    mChattingListAdapter.addData(bean, true, false);
                    lv_chat.post(new Runnable() {
                        @Override
                        public void run() {
                            lv_chat.setSelection(lv_chat.getBottom());
                        }
                    });


                    kv_bar.clearEditText();
                }
                Toast.makeText(ChattingListActivity.this,"SendBtn Click",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnVideoBtnClick() {
                Toast.makeText(ChattingListActivity.this,"VideoBtn Click",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnMultimediaBtnClick() {
                Toast.makeText(ChattingListActivity.this,"MultimediaBtn Click",Toast.LENGTH_SHORT).show();
            }
        });

        lv_chat = (ListView) findViewById(R.id.lv_chat);

        lv_chat.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_FLING:
                        break;
                    case SCROLL_STATE_IDLE:
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        kv_bar.hideAutoView();
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) { }
        });

        mChattingListAdapter = new ChattingListAdapter(this);
        List<ImMsgBean> beanList = new ArrayList<ImMsgBean>();
        for(int i = 0 ; i <20 ; i++){
            ImMsgBean bean = new ImMsgBean();
            bean.setContent("Test:" + i);
            beanList.add(bean);
        }
        mChattingListAdapter.addData(beanList);
        lv_chat.setAdapter(mChattingListAdapter);
    }
    ChattingListAdapter mChattingListAdapter;
    public void initEvent(){
    }
}
