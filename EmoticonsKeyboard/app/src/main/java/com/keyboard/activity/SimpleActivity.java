package com.keyboard.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.keyboard.EmoticonsKeyBoardPopWindow;
import com.keyboard.R;
import com.keyboard.activity.base.BaseActivity;
import com.keyboard.utils.EmoticonsUtils;
import com.keyboard.view.EmoticonsEditText;

public class SimpleActivity extends BaseActivity implements View.OnClickListener{

    private EmoticonsKeyBoardPopWindow mKeyBoardPopWindow;
    private EmoticonsEditText et_content;
    private TextView tv_at;
    private TextView tv_topic;
    private ImageView iv_face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        initTopBar("Simple");
        initLeftBtn(true,R.drawable.common_head_btn_back);

        findView();
        initEvent();
        initKeyBoardPopWindow();
    }

    public void findView(){
        et_content = (EmoticonsEditText)findViewById(R.id.et_content);
        tv_at = (TextView)findViewById(R.id.tv_at);
        tv_topic = (TextView)findViewById(R.id.tv_topic);
        iv_face = (ImageView)findViewById(R.id.iv_face);
    }

    public void initEvent(){
        tv_at.setOnClickListener(this);
        tv_topic.setOnClickListener(this);
        iv_face.setOnClickListener(this);
    }

    public void initKeyBoardPopWindow(){
        mKeyBoardPopWindow = new EmoticonsKeyBoardPopWindow(SimpleActivity.this);
        mKeyBoardPopWindow.setBuilder(EmoticonsUtils.getSimpleBuilder(this));
        mKeyBoardPopWindow.setEditText(et_content);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_at:
                Toast.makeText(SimpleActivity.this,"@",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_topic:
                Toast.makeText(SimpleActivity.this,"#",Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_face:
                mKeyBoardPopWindow.showPopupWindow();
                break;
        }
    }
}
