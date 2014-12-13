package com.saiman.smcall.options.netmeeting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.saiman.smcall.R;

public class NetMeetingActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_nm);
		
		Button btn_bohao = (Button) findViewById(R.id.nm_btn_bohao);
		btn_bohao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NetMeetingActivity.this, BoHaoActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		Button btn_xuanhao = (Button) findViewById(R.id.nm_btn_xuanhao);
		btn_xuanhao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent1 = new Intent(NetMeetingActivity.this, XuanHaoActivity.class);
				startActivity(intent1);
				finish();
			}
		});
		
		Button nm_btn_back = (Button) findViewById(R.id.nm_btn_back);
		nm_btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
	}
}
