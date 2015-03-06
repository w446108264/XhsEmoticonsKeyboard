package com.keyboard;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.keyboard.bean.EmoticonBean;
import com.keyboard.utils.EmoticonsKeyboardBuilder;
import com.keyboard.utils.Utils;
import com.keyboard.view.AutoHeightLayout;
import com.keyboard.view.EmoticonsEditText;
import com.keyboard.view.EmoticonsIndicatorView;
import com.keyboard.view.EmoticonsPageView;
import com.keyboard.view.EmoticonsToolBarView;
import com.keyboard.view.I.IEmoticonsKeyboard;
import com.keyboard.view.I.IView;
import com.keyboard.view.R;

public class XhsEmoticonsKeyBoardBar extends AutoHeightLayout implements IEmoticonsKeyboard, View.OnClickListener,EmoticonsToolBarView.OnToolBarItemClickListener {

    public static int FUNC_CHILLDVIEW_EMOTICON = 0;
    public static int FUNC_CHILLDVIEW_APPS = 1;
    public int mChildViewPosition = -1;

    private EmoticonsPageView mEmoticonsPageView;
    private EmoticonsIndicatorView mEmoticonsIndicatorView;
    private EmoticonsToolBarView mEmoticonsToolBarView;

    private EmoticonsEditText et_chat;
    private RelativeLayout rl_input;
    private LinearLayout ly_foot_func;
    private ImageView btn_face;
    private ImageView btn_multimedia;
    private Button btn_send;
    private Button btn_voice;
    private ImageView btn_voice_or_text;

    private boolean mIsMultimediaVisibility = true;

    public XhsEmoticonsKeyBoardBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_keyboardbar, this);
        initView();
    }

    private void initView() {
        mEmoticonsPageView = (EmoticonsPageView) findViewById(R.id.view_epv);
        mEmoticonsIndicatorView = (EmoticonsIndicatorView) findViewById(R.id.view_eiv);
        mEmoticonsToolBarView = (EmoticonsToolBarView) findViewById(R.id.view_etv);

        rl_input = (RelativeLayout) findViewById(R.id.rl_input);
        ly_foot_func = (LinearLayout) findViewById(R.id.ly_foot_func);
        btn_face = (ImageView) findViewById(R.id.btn_face);
        btn_voice_or_text = (ImageView) findViewById(R.id.btn_voice_or_text);
        btn_voice = (Button) findViewById(R.id.btn_voice);
        btn_multimedia = (ImageView) findViewById(R.id.btn_multimedia);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_chat = (EmoticonsEditText) findViewById(R.id.et_chat);

        setAutoHeightLayoutView(ly_foot_func);
        btn_voice_or_text.setOnClickListener(this);
        btn_multimedia.setOnClickListener(this);
        btn_face.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        btn_voice.setOnClickListener(this);

        mEmoticonsPageView.setOnIndicatorListener(new EmoticonsPageView.OnEmoticonsPageViewListener() {
            @Override
            public void emoticonsPageViewInitFinish(int count) {
                mEmoticonsIndicatorView.init(count);
            }

            @Override
            public void emoticonsPageViewCountChanged(int count) {
                mEmoticonsIndicatorView.setIndicatorCount(count);
            }

            @Override
            public void playTo(int position) {
                mEmoticonsIndicatorView.playTo(position);
            }

            @Override
            public void playBy(int oldPosition, int newPosition) {
                mEmoticonsIndicatorView.playBy(oldPosition, newPosition);
            }
        });

        mEmoticonsPageView.setIViewListener(new IView() {
            @Override
            public void onItemClick(EmoticonBean bean) {
                if (et_chat != null) {
                    et_chat.setFocusable(true);
                    et_chat.setFocusableInTouchMode(true);
                    et_chat.requestFocus();

                    // 删除
                    if (bean.getEventType() == EmoticonBean.FACE_TYPE_DEL) {
                        int action = KeyEvent.ACTION_DOWN;
                        int code = KeyEvent.KEYCODE_DEL;
                        KeyEvent event = new KeyEvent(action, code);
                        et_chat.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                        return;
                    }
                    // 用户自定义
                    else if (bean.getEventType() == EmoticonBean.FACE_TYPE_USERDEF) {
                        return;
                    }

                    int index = et_chat.getSelectionStart();
                    Editable editable = et_chat.getEditableText();
                    if (index < 0) {
                        editable.append(bean.getContent());
                    } else {
                        editable.insert(index, bean.getContent());
                    }
                }
            }

            @Override
            public void onItemDisplay(EmoticonBean bean) { }

            @Override
            public void onPageChangeTo(int position) {
                mEmoticonsToolBarView.setToolBtnSelect(position);
            }
        });

        mEmoticonsToolBarView.setOnToolBarItemClickListener(new EmoticonsToolBarView.OnToolBarItemClickListener() {
            @Override
            public void onToolBarItemClick(int position) {
                mEmoticonsPageView.setPageSelect(position);
            }
        });

        et_chat.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!et_chat.isFocused()) {
                    et_chat.setFocusable(true);
                    et_chat.setFocusableInTouchMode(true);
                }
                return false;
            }
        });
        et_chat.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    setEditableState(true);
                } else {
                    setEditableState(false);
                }
            }
        });
        et_chat.setOnSizeChangedListener(new EmoticonsEditText.OnSizeChangedListener() {
            @Override
            public void onSizeChanged() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        if(mKeyBoardBarViewListener != null){
                            mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState,-1);
                        }
                    }
                });
            }
        });
        et_chat.setOnTextChangedInterface(new EmoticonsEditText.OnTextChangedInterface() {
            @Override
            public void onTextChanged(CharSequence arg0) {
                String str = arg0.toString();
                if (TextUtils.isEmpty(str)) {
                    if(mIsMultimediaVisibility){
                        btn_multimedia.setVisibility(VISIBLE);
                        btn_send.setVisibility(GONE);
                    }
                    else{
                        btn_send.setBackgroundResource(R.drawable.btn_send_bg_disable);
                    }
                }
                // -> 发送
                else {
                    if(mIsMultimediaVisibility){
                        btn_multimedia.setVisibility(GONE);
                        btn_send.setVisibility(VISIBLE);
                        btn_send.setBackgroundResource(R.drawable.btn_send_bg);
                    }
                    else{
                        btn_send.setBackgroundResource(R.drawable.btn_send_bg);
                    }
                }
            }
        });
    }

    private void setEditableState(boolean b) {
        if (b) {
            et_chat.setFocusable(true);
            et_chat.setFocusableInTouchMode(true);
            et_chat.requestFocus();
            rl_input.setBackgroundResource(R.drawable.input_bg_green);
        } else {
            et_chat.setFocusable(false);
            et_chat.setFocusableInTouchMode(false);
            rl_input.setBackgroundResource(R.drawable.input_bg_gray);
        }
    }

    public EmoticonsToolBarView getEmoticonsToolBarView() {
        return mEmoticonsToolBarView;
    }

    public EmoticonsPageView getEmoticonsPageView() {
        return mEmoticonsPageView;
    }

    public EmoticonsEditText getEt_chat() {
        return et_chat;
    }

    public void addToolView(int icon){
        if(mEmoticonsToolBarView != null && icon > 0){
            mEmoticonsToolBarView.addData(icon);
        }
    }

    public void addFixedView(View view , boolean isRight){
        if(mEmoticonsToolBarView != null){
            mEmoticonsToolBarView.addFixedView(view,isRight);
        }
    }

    public void clearEditText(){
        if(et_chat != null){
            et_chat.setText("");
        }
    }

    public void del(){
        if(et_chat != null){
            int action = KeyEvent.ACTION_DOWN;
            int code = KeyEvent.KEYCODE_DEL;
            KeyEvent event = new KeyEvent(action, code);
            et_chat.onKeyDown(KeyEvent.KEYCODE_DEL, event);
        }
    }

    public void setVideoVisibility(boolean b){
        if(b){
            btn_voice_or_text.setVisibility(VISIBLE);
        }
        else{
            btn_voice_or_text.setVisibility(GONE);
        }
    }

    public void setMultimediaVisibility(boolean b){
        mIsMultimediaVisibility = b;
        if(b){
            btn_multimedia.setVisibility(VISIBLE);
            btn_send.setVisibility(GONE);
        }
        else{
            btn_send.setVisibility(VISIBLE);
            btn_multimedia.setVisibility(GONE);
        }
    }

    @Override
    public void setBuilder(EmoticonsKeyboardBuilder builder) {
        mEmoticonsPageView.setBuilder(builder);
        mEmoticonsToolBarView.setBuilder(builder);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (ly_foot_func != null && ly_foot_func.isShown()) {
                    hideAutoView();
                    btn_face.setImageResource(R.drawable.icon_face_nomal);
                    return true;
                } else {
                    return super.dispatchKeyEvent(event);
                }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_face) {
            switch (mKeyboardState){
                case KEYBOARD_STATE_NONE:
                case KEYBOARD_STATE_BOTH:
                    show(FUNC_CHILLDVIEW_EMOTICON);
                    btn_face.setImageResource(R.drawable.icon_face_pop);
                    showAutoView();
                    Utils.closeSoftKeyboard(mContext);
                    break;
                case KEYBOARD_STATE_FUNC:
                    if(mChildViewPosition == FUNC_CHILLDVIEW_EMOTICON){
                        btn_face.setImageResource(R.drawable.icon_face_nomal);
                        Utils.openSoftKeyboard(et_chat);
                    }
                    else {
                        show(FUNC_CHILLDVIEW_EMOTICON);
                        btn_face.setImageResource(R.drawable.icon_face_pop);
                    }
                    break;
            }
        }
        else if (id == R.id.btn_send) {
            if(mKeyBoardBarViewListener != null){
                mKeyBoardBarViewListener.OnSendBtnClick(et_chat.getText().toString());
            }
        }
        else if (id == R.id.btn_multimedia) {
            switch (mKeyboardState){
                case KEYBOARD_STATE_NONE:
                case KEYBOARD_STATE_BOTH:
                    show(FUNC_CHILLDVIEW_APPS);
                    btn_face.setImageResource(R.drawable.icon_face_nomal);
                    rl_input.setVisibility(VISIBLE);
                    btn_voice.setVisibility(GONE);
                    showAutoView();
                    Utils.closeSoftKeyboard(mContext);
                    break;
                case KEYBOARD_STATE_FUNC:
                    btn_face.setImageResource(R.drawable.icon_face_nomal);
                    if(mChildViewPosition == FUNC_CHILLDVIEW_APPS){
                        hideAutoView();
                    }
                    else {
                        show(FUNC_CHILLDVIEW_APPS);
                    }
                    break;
            }
        }
        else if (id == R.id.btn_voice_or_text) {
            if(rl_input.isShown()){
                hideAutoView();
                rl_input.setVisibility(GONE);
                btn_voice.setVisibility(VISIBLE);
            }
            else{
                rl_input.setVisibility(VISIBLE);
                btn_voice.setVisibility(GONE);
                setEditableState(true);
                Utils.openSoftKeyboard(et_chat);
            }
        }
        else if (id == R.id.btn_voice) {
            if(mKeyBoardBarViewListener != null){
                mKeyBoardBarViewListener.OnVideoBtnClick();
            }
        }
    }

    public void add(View view){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ly_foot_func.addView(view,params);
    }

    public void show(int position){
        int childCount = ly_foot_func.getChildCount();
        if(position < childCount){
            for(int i = 0 ; i < childCount ; i++){
                if(i == position){
                    ly_foot_func.getChildAt(i).setVisibility(VISIBLE);
                    mChildViewPosition  = i;
                } else{
                    ly_foot_func.getChildAt(i).setVisibility(GONE);
                }
            }
        }
        post(new Runnable() {
            @Override
            public void run() {
                if(mKeyBoardBarViewListener != null){
                    mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState,-1);
                }
            }
        });
    }

    @Override
    public void OnSoftPop(final int height) {
        super.OnSoftPop(height);
        post(new Runnable() {
            @Override
            public void run() {
                btn_face.setImageResource(R.drawable.icon_face_nomal);
                if(mKeyBoardBarViewListener != null){
                    mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState,height);
                }
            }
        });
    }

    @Override
    public void OnSoftClose(int height) {
        super.OnSoftClose(height);
        if(mKeyBoardBarViewListener != null){
            mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState,height);
        }
    }

    @Override
    public void OnSoftChanegHeight(int height) {
        super.OnSoftChanegHeight(height);
        if(mKeyBoardBarViewListener != null){
            mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState,height);
        }
    }

    KeyBoardBarViewListener mKeyBoardBarViewListener;
    public void setOnKeyBoardBarViewListener(KeyBoardBarViewListener l) { this.mKeyBoardBarViewListener = l; }

    @Override
    public void onToolBarItemClick(int position) {

    }

    public interface KeyBoardBarViewListener {
        public void OnKeyBoardStateChange(int state, int height);

        public void OnSendBtnClick(String msg);

        public void OnVideoBtnClick();

        public void OnMultimediaBtnClick();
    }
}
