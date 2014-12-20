package com.saiman.smcall.options.dial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.saiman.smcall.R;
import com.saiman.smcall.app.MobileApplication;
import com.saiman.smcall.domain.CallPhoneHeHe;
import com.saiman.smcall.domain.CallPhoneYinPin;
import com.saiman.smcall.domain.IXinTempStorage;
import com.saiman.smcall.options.receive.PhoneReceiver;
import com.saiman.smcall.request.RequestUrl;
import com.saiman.smcall.util.LogUtil;

public class CallPhone extends Activity implements SensorEventListener {
	private TextView mZT;
	private CallPhoneYinPin iXinCallPhoneYinPin;

	private AudioManager audioManager;
	private SensorManager mSensorManager;
	private Sensor mSensor;

	@Override
	protected void onResume() {
		if (mSensorManager != null) {
			mSensorManager.registerListener(this, mSensor,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		super.onResume();
		// System.exit(0);
	}

	@Override
	protected void onPause() {
		if (mSensorManager != null) {
			mSensorManager.unregisterListener(this);
		}
		this.finish();
		mp.stop();
		unregisterReceiver(myReceiver2);
		super.onPause();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float range = event.values[0];
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	private MediaPlayer mp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iXinCallPhoneYinPin = new CallPhoneYinPin(this);
		mp = iXinCallPhoneYinPin.getMp();

		audioManager = (AudioManager) this
				.getSystemService(Context.AUDIO_SERVICE);

		if (audioManager != null) {
			audioManager.setMode(AudioManager.MODE_IN_CALL);
			mp.start();
		}
		if (mSensorManager != null) {
			mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
			mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		}

		CallPhoneHeHe iXinCallPhoneHeHe = CallPhoneHeHe.getIXinCallPhoneHeHe();
		iXinCallPhoneHeHe.setCallPhoneYinPin(iXinCallPhoneYinPin);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.call_phone);
		init();

	}

	public void showTopWindow(View paramView) {
		WindowManager localWindowManager = (WindowManager) getApplicationContext()
				.getSystemService("window");
		WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
		localLayoutParams.type = 2007;
		localLayoutParams.width = -1;
		localLayoutParams.height = -1;
		localLayoutParams.x = 0;
		localLayoutParams.y = 0;
		localWindowManager.addView(paramView, localLayoutParams);
	}

	private TextView tvPhoneName;
	private TextView tvPhoneNum;
	private IXinTempStorage iXinTempStorage = IXinTempStorage
			.getIXinTempStorage();

	private void init() {

		tvPhoneName = (TextView) findViewById(R.id.textViewId_phoneName_callingPhone);
		tvPhoneNum = (TextView) findViewById(R.id.textViewId_phoneNum_callingPhone);
		Intent intent = getIntent();

		String phoneName = intent.getStringExtra("CALLNAME");
		String phoneNum = intent.getStringExtra("CALLNUMBER");

		ContentResolver resolver = getContentResolver();

		if (resolver != null) {
			ContentValues values = new ContentValues();
			values.put(CallLog.Calls.NUMBER, phoneNum);
			values.put(CallLog.Calls.CACHED_NAME, phoneName);
			values.put(CallLog.Calls.TYPE, CallLog.Calls.OUTGOING_TYPE);
			values.put(CallLog.Calls.DATE, System.currentTimeMillis());
			resolver.insert(CallLog.Calls.CONTENT_URI, values);
		}

		tvPhoneName.setText(phoneName);
		tvPhoneNum.setText(phoneNum);
		IXinTempStorage iXinTempStorage = IXinTempStorage.getIXinTempStorage();
		// iXinTempStorage.setView(mView);
		mZT = (TextView) findViewById(R.id.textViewId_ZT_CallingPhone);
		mZT.setText("等待对方接听，请稍后");
		StringBuffer sb = new StringBuffer(phoneNum);
		SharedPreferences sharedPreferences = getSharedPreferences("saiman",
				Context.MODE_WORLD_READABLE);
		String namees = sharedPreferences.getString("UserName", null);
		String[] CallApiS=RequestUrl.callURL.split("↓");
	    String CallUrl=CallApiS[0].toString().replace("[主叫]",namees).replace("[被叫]",phoneNum).replace("[透传]","1");
//	    String CallUrl=RequestUrl.callURL.replace("[主叫]",namees).replace("[被叫]",phoneNum);

		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mWifiLock = mWifiManager.createWifiLock("Test");
		if (mWifiLock != null) {
			mWifiLock.acquire();
		}

		MobileApplication.requestQueue.add(new StringRequest(Method.GET, CallUrl, 
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						LogUtil.i(this.getClass().getSimpleName(), "打电话返回数据："+response);
						if (response != null) {
//							try {
//								JSONObject obj = new JSONObject(response);
//								int retCode = (Integer) obj.get("retCode");
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
							String[] split = response.split("[|]");
							if ('1' == response.charAt(2)) {
								mZT.setText("等待对方接听，请稍后");
								if (mWifiLock.isHeld()) {
									mWifiLock.acquire();
								}
							} else {
								mZT.setText(split[2]);
								if (mWifiLock.isHeld()) {
									mWifiLock.acquire();
								}
							}
							} else {
								Toast.makeText(CallPhone.this, "输入有误,请重新输入!",
										Toast.LENGTH_SHORT).show();
							}
						}
					
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(CallPhone.this, "失败", Toast.LENGTH_SHORT).show();
					}
				}){

					@Override
					public Map<String, String> getHeaders() throws AuthFailureError {
						Map<String, String> map = new HashMap<String, String>();
						map.put("Connection", "Keep-Alive");
						map.put("Content-type", "application/x-www-form-urlencoded");
						return map;
					}
			
		});
		
		
		List<View> list = new ArrayList<View>();
		Button fhh = (Button) findViewById(R.id.imageButtonId_FHH_CallingPhone);
		fhh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				CallPhone.this.finish();
				MediaPlayer mp = iXinCallPhoneYinPin.getMp();
				if (mp != null) {
					mp.stop();
				}

				Cursor cursor = getContentResolver().query(
						CallLog.Calls.CONTENT_URI, null, null, null, null);
				ContentResolver resolver = getContentResolver();
				if (cursor != null && resolver != null) {
					cursor.moveToFirst();
					while (cursor.moveToNext()) {
						String number = cursor.getString(cursor
								.getColumnIndex(CallLog.Calls.NUMBER));
						int id = cursor.getInt(cursor
								.getColumnIndex(CallLog.Calls._ID));
						if ("-1".equals(number) || "-2".equals(number)
								|| "".equals(number) || number == null) {
							resolver.delete(CallLog.Calls.CONTENT_URI,
									CallLog.Calls._ID + "=?", new String[] { id
											+ "" });
						}
					}
				}
//				final Intent intent22 = getPackageManager()
//						.getLaunchIntentForPackage(getPackageName());
//				intent22.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent22);
			}
		});

		String phoneNamea = android.os.Build.MODEL;

		if (phoneNamea != null && !"M040".equals(phoneNamea)) {
			myReceiver2 = new PhoneReceiver();
			intentFilter = new IntentFilter("android.intent.action.PHONE_STATE");
			registerReceiver(myReceiver2, intentFilter);
		} else {
			Toast.makeText(this, "您的这款手机不支持自动接听，请您手动接听回拨来电", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private PhoneReceiver myReceiver2;
	private WifiManager mWifiManager;
	private WifiLock mWifiLock;
	private IntentFilter intentFilter;
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		audioManager.setMode(AudioManager.MODE_NORMAL);
		LogUtil.i(this.getClass().getSimpleName(), "是否将声音设置好");
	}
}
