package com.keyboard.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.keyboard.R;


public class BaseActivity extends Activity {

    protected TextView tv_left;
    protected TextView tv_title;
    protected TextView tv_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initTopBar(String title) {
        tv_left = (TextView) this.findViewById(R.id.tv_left);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_right = (TextView) this.findViewById(R.id.tv_right);
        if (tv_title != null && title != null) {
            tv_title.setText(title);
            tv_title.setVisibility(View.VISIBLE);
        }
    }

    protected void initLeftBtn(boolean isVisibility, int bg) {
        if (tv_left == null) {
            return;
        }
        if (isVisibility) {
            tv_left.setVisibility(View.VISIBLE);
            tv_left.setOnClickListener(onclick);
        } else {
            tv_left.setVisibility(View.GONE);
        }
        tv_left.setBackgroundResource(bg);
    }

    protected void initRightBtn(boolean isVisibility, int bg) {
        if (tv_right == null) {
            return;
        }
        if (isVisibility) {
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setOnClickListener(onclick);
        } else {
            tv_right.setVisibility(View.GONE);
        }
        tv_right.setBackgroundResource(bg);
    }

    protected void initRightBtn(boolean isVisibility, String str) {
        if (tv_right == null) {
            return;
        }
        if (isVisibility) {
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setOnClickListener(onclick);
        } else {
            tv_right.setVisibility(View.GONE);
        }
        tv_right.setText(str);
    }

    private OnClickListener onclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_left:
                    leftBtnHandle();
                    break;
                case R.id.tv_right:
                    rightBtnHandle();
                    break;
            }
        }
    };

    protected void rightBtnHandle() { }

    protected void leftBtnHandle() {
        finish();
    }

}
