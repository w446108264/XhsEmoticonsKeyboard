package com.keyboard.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.keyboard.R;
import com.keyboard.activity.base.BaseActivity;
import com.keyboard.utils.FunnyPicturesView;

import java.math.BigDecimal;

public class WeActivity extends BaseActivity {
    private SensorManager manager;
    private Sensor sensor;
    private SensorEventListener listener;

    boolean mIsDealSensor = true;

    private float num_Y = 0;
    private float num_X = 0;

    FunnyPicturesView fpv_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we);

        initTopBar("We");
        initLeftBtn(true,R.drawable.common_head_btn_back);

        fpv_image =  (FunnyPicturesView) findViewById(R.id.fpv_image);
        initSensorManager();

        TextView tv_download = (TextView)findViewById(R.id.tv_download);
        tv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri uri = Uri.parse("market://details?id=com.xingin.xhs");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                }
            }
        });
    }

    public void initSensorManager() {
        manager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                float X = event.values[SensorManager.DATA_X];    //由于手机横向   所以X轴表示上下方向
                float Y = event.values[SensorManager.DATA_Y];    // Y轴表示左右方向
                float Z = event.values[SensorManager.DATA_Z];

                if (mIsDealSensor) {
                    if (num_Y != Y || num_X != X){
                        X = scaleFloat(X);
                        Y = scaleFloat(Y);
                        num_X = X;
                        num_Y = Y;
                        fpv_image.update(num_X,num_Y);
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public float scaleFloat(float num){
        int   scale  =   1;
        int   roundingMode  =  4;
        BigDecimal bd  =   new  BigDecimal((double)num);
        bd   =  bd.setScale(scale,roundingMode);
        return  bd.floatValue();
    }
}
