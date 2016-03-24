package com.xhsemoticonskeyboard.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.xhsemoticonskeyboard.R;
import com.xhsemoticonskeyboard.common.SimpleCommonUtils;
import com.xhsemoticonskeyboard.common.adapter.ChattingListAdapter;
import com.xhsemoticonskeyboard.common.data.ImMsgBean;
import com.xhsemoticonskeyboard.common.widget.AutoHeightBehavior;
import com.xhsemoticonskeyboard.common.widget.SimpleAppsGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import sj.keyboard.XhsEmoticonsKeyBoard;
import sj.keyboard.widget.AutoHeightLayout;
import sj.keyboard.widget.FuncLayout;

public class SimpleChatOnCoordinatorLayoutActivity extends AppCompatActivity implements FuncLayout.OnFuncKeyBoardListener, AutoHeightLayout.OnMaxParentHeightChangeListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.lv_chat)
    ListView lvChat;
    @Bind(R.id.ek_bar)
    XhsEmoticonsKeyBoard ekBar;

    private ChattingListAdapter chattingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_chat_oncoordinatorlayout);
        ButterKnife.bind(this);
        toolbar.setTitle("Simple Chat Keyboard On CoordinatorLayout");
        setSupportActionBar(toolbar);
        initView();
    }

    private void initView() {
        initEmoticonsKeyBoardBar();
        initListView();
        initCoordinatorLayout();
    }

    private void initEmoticonsKeyBoardBar() {
        SimpleCommonUtils.initEmoticonsEditText(ekBar.getEtChat());
        ekBar.setAdapter(SimpleCommonUtils.getCommonAdapter(this, null));
        ekBar.addOnFuncKeyBoardListener(this);
        ekBar.addFuncView(new SimpleAppsGridView(this));
        ekBar.setOnMaxParentHeightChangeListener(this);

        ekBar.getBtnSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSendBtnClick(ekBar.getEtChat().getText().toString());
                ekBar.getEtChat().setText("");
            }
        });

        // TEST
        ekBar.getEmoticonsToolBarView().addToolItemView(com.keyboard.view.R.drawable.icon_face_nomal, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SimpleChatOnCoordinatorLayoutActivity.this, "TEST", Toast.LENGTH_SHORT).show();
            }
        });
        ekBar.getEmoticonsToolBarView().addToolItemView(com.keyboard.view.R.drawable.icon_face_nomal, null);
        ekBar.getEmoticonsToolBarView().addToolItemView(com.keyboard.view.R.drawable.icon_face_nomal, null);
        ekBar.getEmoticonsToolBarView().addToolItemView(com.keyboard.view.R.drawable.icon_face_nomal, null);
        ekBar.getEmoticonsToolBarView().addToolItemView(com.keyboard.view.R.drawable.icon_face_nomal, null);
        ekBar.getEmoticonsToolBarView().addToolItemView(com.keyboard.view.R.drawable.icon_face_nomal, null);
        ekBar.getEmoticonsToolBarView().addToolItemView(com.keyboard.view.R.drawable.icon_face_nomal, null);
        ekBar.getEmoticonsToolBarView().addToolItemView(com.keyboard.view.R.drawable.icon_face_nomal, null);
    }

    private void initCoordinatorLayout() {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ekBar.getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        if (behavior instanceof AutoHeightBehavior) {
            ((AutoHeightBehavior) behavior).setOnDependentViewChangedListener(new AutoHeightBehavior.OnDependentViewChangedListener() {
                @Override
                public void onDependentViewChangedListener(CoordinatorLayout parent, View child, View dependency) {
                    parent.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollToBottom();
                        }
                    });
                }
            });
        }
    }

    private void initListView() {
        chattingListAdapter = new ChattingListAdapter(this);
        List<ImMsgBean> beanList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ImMsgBean bean = new ImMsgBean();
            bean.setContent("Test:" + i);
            beanList.add(bean);
        }
        chattingListAdapter.addData(beanList);
        lvChat.setAdapter(chattingListAdapter);
        lvChat.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_FLING:
                        break;
                    case SCROLL_STATE_IDLE:
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        ekBar.reset();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    private void OnSendBtnClick(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            ImMsgBean bean = new ImMsgBean();
            bean.setContent(msg);
            chattingListAdapter.addData(bean, true, false);
            scrollToBottom();
        }
    }

    private void scrollToBottom() {
        lvChat.post(new Runnable() {
            @Override
            public void run() {
                lvChat.setSelection(lvChat.getBottom());
            }
        });
    }

    @Override
    public void onMaxParentHeightChange(int height) {
        scrollToBottom();
    }

    @Override
    public void OnFuncPop(int height) {
        scrollToBottom();
    }

    @Override
    public void OnFuncClose() { }

    @Override
    protected void onPause() {
        super.onPause();
        ekBar.reset();
    }
}
