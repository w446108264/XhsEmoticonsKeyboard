package com.xhsemoticonskeyboard.userdef;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.xhsemoticonskeyboard.R;

import sj.keyboard.XhsEmoticonsKeyBoard;
import sj.keyboard.utils.EmoticonsKeyboardUtils;

public class SimpleUserdefEmoticonsKeyBoard extends XhsEmoticonsKeyBoard {

    public final int APPS_HEIGHT = 120;

    public SimpleUserdefEmoticonsKeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void inflateKeyboardBar(){
        mInflater.inflate(R.layout.view_keyboard_userdef, this);
    }

    @Override
    protected View inflateFunc(){
        return mInflater.inflate(R.layout.view_func_emoticon_userdef, null);
    }

    @Override
    public void reset() {
        EmoticonsKeyboardUtils.closeSoftKeyboard(getContext());
        mLyKvml.hideAllFuncView();
        mBtnFace.setImageResource(R.mipmap.chatting_emoticons);
    }

    @Override
    public void onFuncChange(int key) {
        if (FUNC_TYPE_EMOTION == key) {
            mBtnFace.setImageResource(R.mipmap.chatting_softkeyboard);
        } else {
            mBtnFace.setImageResource(R.mipmap.chatting_emoticons);
        }
        checkVoice();
    }

    @Override
    public void OnSoftClose() {
        super.OnSoftClose();
        if (mLyKvml.getCurrentFuncKey() == FUNC_TYPE_APPPS) {
            setFuncViewHeight(EmoticonsKeyboardUtils.dip2px(getContext(), APPS_HEIGHT));
        }
    }

    @Override
    protected void showText() {
        mEtChat.setVisibility(VISIBLE);
        mBtnFace.setVisibility(VISIBLE);
        mBtnVoice.setVisibility(GONE);
    }

    @Override
    protected void showVoice() {
        mEtChat.setVisibility(GONE);
        mBtnFace.setVisibility(GONE);
        mBtnVoice.setVisibility(VISIBLE);
        reset();
    }

    @Override
    protected void checkVoice() {
        if (mBtnVoice.isShown()) {
            mBtnVoiceOrText.setImageResource(R.mipmap.chatting_softkeyboard);
        } else {
            mBtnVoiceOrText.setImageResource(R.mipmap.chatting_vodie);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == com.keyboard.view.R.id.btn_voice_or_text) {
            if (mEtChat.isShown()) {
                mBtnVoiceOrText.setImageResource(R.mipmap.chatting_softkeyboard);
                showVoice();
            } else {
                showText();
                mBtnVoiceOrText.setImageResource(R.mipmap.chatting_vodie);
                EmoticonsKeyboardUtils.openSoftKeyboard(mEtChat);
            }
        } else if (i == com.keyboard.view.R.id.btn_face) {
            toggleFuncView(FUNC_TYPE_EMOTION);
        } else if (i == com.keyboard.view.R.id.btn_multimedia) {
            toggleFuncView(FUNC_TYPE_APPPS);
            setFuncViewHeight(EmoticonsKeyboardUtils.dip2px(getContext(), APPS_HEIGHT));
        }
    }
}
