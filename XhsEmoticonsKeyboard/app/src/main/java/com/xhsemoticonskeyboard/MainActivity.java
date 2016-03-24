package com.xhsemoticonskeyboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.xhsemoticonskeyboard.activity.SimpleChatActivity;
import com.xhsemoticonskeyboard.activity.SimpleChatOnCoordinatorLayoutActivity;
import com.xhsemoticonskeyboard.activity.SimpleCommentActivity;
import com.xhsemoticonskeyboard.qq.QqActivity;
import com.xhsemoticonskeyboard.userdef.SimpleChatUserDefActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.btn_simple_comment) void btn_simple_comment() {
        startActivity(new Intent(MainActivity.this, SimpleCommentActivity.class));
    }

    @OnClick(R.id.btn_simple_chat) void btn_simple_chat() {
        startActivity(new Intent(MainActivity.this, SimpleChatActivity.class));
    }

    @OnClick(R.id.btn_simple_chat_coordinatorlayout) void btn_simple_chat_coordinatorlayout() {
        startActivity(new Intent(MainActivity.this, SimpleChatOnCoordinatorLayoutActivity.class));
    }

    @OnClick(R.id.btn_userdef_ui) void btn_userdef_ui() {
        startActivity(new Intent(MainActivity.this, SimpleChatUserDefActivity.class));
    }

    @OnClick(R.id.btn_qq) void btn_qq() {
        startActivity(new Intent(MainActivity.this, QqActivity.class));
    }

    @OnClick(R.id.btn_github) void btn_github() {
        Uri uri = Uri.parse("http://github.com/w446108264/XhsEmoticonsKeyboard");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }
}
