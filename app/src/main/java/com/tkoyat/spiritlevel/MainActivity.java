package com.tkoyat.spiritlevel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends Activity implements SensorEventListener {
    //定义水平仪的仪表盘
    private SpiritView show;
    //定义水平仪能处理的最大倾斜角度，超过该角度气泡直接位于边界
    private int MAX_ANGLE = 30;
    private float zAngleLast = 0;  // 最后一次水平方向
    //定义Sensor管理器
    SensorManager sensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取水平仪的主组件
        show = (SpiritView) findViewById(R.id.show);
        //获取传感器
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float values[] = sensorEvent.values;
        //获取传感器的类型
        int sensorType = sensorEvent.sensor.getType();
        Log.d("sensorvalue", "begin");
        switch (sensorType) {
            case Sensor.TYPE_ORIENTATION:
//                //获取与Y轴的夹角
//                float yAngle = values[1];
//                //获取与Z轴的夹角
//                float zAngle = values[2];

                //获取与Y轴的夹角（处置方向旋转）
                float yAngle = values[1];
                float zAngle = values[2];
                Log.d("sensorvalue", zAngle + ", " + yAngle);

                if (zAngle > 0 && yAngle < 0) {
                    // 第一象限
                    zAngle = zAngle;
                    yAngle = yAngle;

                } else if (zAngle > 0 && yAngle > 0) {
                    // 第二象限
                    zAngle = zAngle;
                    yAngle = yAngle;

                } else if  (zAngle > 0 && yAngle > 0) {
                    // 第三象限
                    zAngle = zAngle;
                    yAngle = yAngle;

                } else if  (zAngle < 0 && yAngle < 0) {
                    // 第四象限
                    zAngle = zAngle;
                    yAngle = yAngle;
                }

                //气泡位于中间时（水平仪完全水平）
                int x = (show.back.getWidth() - show.bubble.getWidth()) / 2;
                int y = (show.back.getHeight() - show.bubble.getHeight()) / 2;

                //如果与Z轴的倾斜角还在最大角度之内
                if (Math.abs(zAngle) <= MAX_ANGLE) {
                    //根据与Z轴的倾斜角度计算X坐标轴的变化值
                    int deltaX = (int) ((show.back.getWidth() - show.bubble.getWidth()) / 2 * zAngle / MAX_ANGLE);
                    x += deltaX;
                }
                //如果与Z轴的倾斜角已经大于MAX_ANGLE，气泡应到最左边
                else if (zAngle > MAX_ANGLE) {
                    x = show.back.getWidth() - show.bubble.getWidth();
                }
                //如果与Z轴的倾斜角已经小于负的Max_ANGLE,气泡应到最右边
                else {
                    x = 0;

                }

                //如果与Y轴的倾斜角还在最大角度之内
                if (Math.abs(yAngle) <= MAX_ANGLE) {
                    //根据与Z轴的倾斜角度计算X坐标轴的变化值
                    int deltaY = (int) ((show.back.getHeight() - show.bubble.getHeight()) / 2 * yAngle / MAX_ANGLE);
                    y += deltaY;
                }
                //如果与Y轴的倾斜角已经大于MAX_ANGLE，气泡应到最下边
                else if (yAngle > MAX_ANGLE) {
                    y = show.back.getHeight() - show.bubble.getHeight();
                }
                //如果与Y轴的倾斜角已经小于负的Max_ANGLE,气泡应到最上边
                else {
                    y = 0;
                }
                //如果计算出来的X，Y坐标还位于水平仪的仪表盘之内，则更新水平仪气泡坐标
                if (true) {
                    show.bubbleX = x;
                    show.bubbleY = y;
                    //Toast.makeText(Spirit.this, "在仪表盘内", Toast.LENGTH_SHORT).show();
                }
                //通知组件更新
                show.postInvalidate();
                //show.invalidate();
                break;
        }
    }

    private boolean isContain(int x, int y) {
        //计算气泡的圆心坐标X，y
        int bubbleCx = x + show.bubble.getWidth() / 2;
        int bubbleCy = y + show.bubble.getWidth() / 2;
        //计算水平仪仪表盘圆心的坐标
        int backCx = show.back.getWidth() / 2;
        int backCy = show.back.getWidth() / 2;
        //计算气泡的圆心与水平仪表盘的圆心之间的距离
        double distance = Math.sqrt((bubbleCx - backCx) * (bubbleCx * backCx) +
                (bubbleCy - backCy) * (bubbleCy - backCy));
        //若两圆心的距离小于他们的半径差，即可认为处于该点的气泡任然位于仪表盘内
        if (distance < (show.back.getWidth() - show.bubble.getWidth())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onStop() {
        //取消注册
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onPause() {
        //取消注册
        sensorManager.unregisterListener(this);
        super.onPause();
    }
}