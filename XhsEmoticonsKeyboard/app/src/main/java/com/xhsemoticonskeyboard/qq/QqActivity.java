package com.xhsemoticonskeyboard.qq;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.sj.emoji.EmojiBean;
import com.xhsemoticonskeyboard.R;
import com.xhsemoticonskeyboard.common.Constants;
import com.xhsemoticonskeyboard.common.data.ImMsgBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import sj.keyboard.data.EmoticonEntity;
import sj.keyboard.interfaces.EmoticonClickListener;
import sj.keyboard.widget.EmoticonsEditText;
import sj.keyboard.widget.FuncLayout;

public class QqActivity extends AppCompatActivity implements FuncLayout.OnFuncKeyBoardListener {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.lv_chat) ListView lvChat;
    @Bind(R.id.ek_bar) QqEmoticonsKeyBoard ekBar;

    private QqChattingListAdapter chattingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_qq);
        ButterKnife.bind(this);
        toolbar.setTitle("Qq Keyboard");
        initView();
    }

    private void initView() {
        initEmoticonsKeyBoardBar();
        initListView();
    }

    private void initEmoticonsKeyBoardBar() {
        QqUtils.initEmoticonsEditText(ekBar.getEtChat());
        ekBar.setAdapter(QqUtils.getCommonAdapter(this, emoticonClickListener));
        ekBar.addOnFuncKeyBoardListener(this);

        ekBar.addFuncView(QqEmoticonsKeyBoard.FUNC_TYPE_PTT, new SimpleQqGridView(this));
        ekBar.addFuncView(QqEmoticonsKeyBoard.FUNC_TYPE_PTV, new SimpleQqGridView(this));
        ekBar.addFuncView(QqEmoticonsKeyBoard.FUNC_TYPE_PLUG, new SimpleQqGridView(this));

        ekBar.getEtChat().setOnSizeChangedListener(new EmoticonsEditText.OnSizeChangedListener() {
            @Override
            public void onSizeChanged(int w, int h, int oldw, int oldh) {
                scrollToBottom();
            }
        });
        ekBar.getBtnSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSendBtnClick(ekBar.getEtChat().getText().toString());
                ekBar.getEtChat().setText("");
            }
        });
        ekBar.getEmoticonsToolBarView().addFixedToolItemView(true, R.mipmap.qvip_emoji_tab_more_new_pressed, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QqActivity.this, "ADD", Toast.LENGTH_SHORT).show();
            }
        });
    }

    EmoticonClickListener emoticonClickListener = new EmoticonClickListener() {
        @Override
        public void onEmoticonClick(Object o, int actionType, boolean isDelBtn) {

            if (isDelBtn) {
                QqUtils.delClick(ekBar.getEtChat());
            } else {
                if(o == null){
                    return;
                }
                if(actionType == Constants.EMOTICON_CLICK_BIGIMAGE){
                    if(o instanceof EmoticonEntity){
                        OnSendImage(((EmoticonEntity)o).getIconUri());
                    }
                } else {
                    String content = null;
                    if(o instanceof EmojiBean){
                        content = ((EmojiBean)o).emoji;
                    } else if(o instanceof EmoticonEntity){
                        content = ((EmoticonEntity)o).getContent();
                    }

                    if(TextUtils.isEmpty(content)){
                        return;
                    }
                    int index = ekBar.getEtChat().getSelectionStart();
                    Editable editable = ekBar.getEtChat().getText();
                    editable.insert(index, content);
                }
            }
        }
    };

    private void initListView() {
        chattingListAdapter = new QqChattingListAdapter(this);
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

    private void OnSendImage(String image) {
        if (!TextUtils.isEmpty(image)) {
            OnSendBtnClick("[img]" + image);
        }
    }

    private void scrollToBottom() {
        lvChat.requestLayout();
        lvChat.post(new Runnable() {
            @Override
            public void run() {
                lvChat.setSelection(lvChat.getBottom());
            }
        });
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
