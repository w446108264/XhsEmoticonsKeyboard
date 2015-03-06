package com.keyboard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.keyboard.R;
import com.keyboard.activity.base.BaseActivity;
import com.keyboard.utils.EmoticonsUtils;

/**
 * XhsEmoticonsKeyboard
 * @author zhongdaxia 2015-3-5
 */
public class MainActivity extends BaseActivity {

    Button btn_simple;
    Button btn_chatting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EmoticonsUtils.initEmoticonsDB(getApplicationContext());

        initTopBar("XhsEmoticonsKeyboard");
        initLeftBtn(true,R.drawable.common_head_btn_back);

        btn_simple = (Button)findViewById(R.id.btn_simple);
        btn_simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,SimpleActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        btn_chatting = (Button)findViewById(R.id.btn_chatting);
        btn_chatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,ChattingListActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
