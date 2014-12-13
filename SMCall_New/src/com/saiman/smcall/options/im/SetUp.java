package com.saiman.smcall.options.im;

import android.app.Activity;
import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.saiman.smcall.R;
import com.saiman.smcall.domain.CallPhoneYinPin;
import com.saiman.smcall.view.AutoScrollTextView;

public class SetUp extends Activity implements SensorEventListener {
	private AutoScrollTextView autoScrollTextView;

	Button clear;

	// 定义sensor管理器
	private SensorManager mSensorManager;
	// 震动
	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_up);

		autoScrollTextView = (AutoScrollTextView) findViewById(R.id.TextViewNotice);
		autoScrollTextView.init(getWindowManager());
		autoScrollTextView.startScroll();

		// 获取传感器管理服务
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// 震动
		vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

		clear = (Button) findViewById(R.id.clear);
		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clear.setText("现在给button的text赋值喽~");
				MediaPlayer mp = iXinCallPhoneYinPin.getMp();
				mp.stop();
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

		// 加速度传感器
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				//还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME等，
				// 根据不同应用，需要的反应速率不同，具体根据实际情况设定
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		mSensorManager.unregisterListener(this);
		super.onStop();
	}

	@Override
	protected void onPause() {
		mSensorManager.unregisterListener(this);
		super.onPause();
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	private CallPhoneYinPin iXinCallPhoneYinPin;

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		int sensorType = event.sensor.getType();

		//values[0]:X轴，values[1]：Y轴，values[2]：Z轴
		float[] values = event.values;

		if (sensorType == Sensor.TYPE_ACCELEROMETER) {

			/*
			 * 因为一般正常情况下，任意轴数值最大就在9.8~10之间，只有在你突然摇动手机的时候，瞬时加速度才会突然增大或减少。
			 * 所以，经过实际测试，只需监听任一轴的加速度大于14的时候，改变你需要的设置就OK了~~~
			 */
			if ((Math.abs(values[0]) > 18 || Math.abs(values[1]) > 18 || Math
					.abs(values[2]) > 18)) {

				// 摇动手机后，设置button上显示的字为空
				clear.setText(null);

				// 摇动手机后，再伴随震动提示~~
				vibrator.vibrate(1000);
				iXinCallPhoneYinPin = new CallPhoneYinPin(this);
				MediaPlayer mp = iXinCallPhoneYinPin.getMp();
				mp.start();
			}

		}
	}
}
