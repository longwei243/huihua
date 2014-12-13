package com.saiman.smcall.options.im;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.saiman.smcall.R;

public class MoreI extends Activity implements OnClickListener {

	private String[] data = new String[] { "使用惠话网络电话，无需长途   漫游等费用，全国拨打电话统一标准资费：0.12元/分钟\n1.充值30送10一次到账40元，打电话每分钟低至0.09元/分钟\n2.充值50送20一次到账70元，打电话每分钟低至0.085元/分钟\n3.充值100送60一次到账160元，打电话每分钟低至0.075元/分钟\n4.充值200送150一次到账350元，打电话每分钟低至0.067元/分钟\n5.充值300送260一次到账560元，打电话每分钟低至0.064元/分钟\n6.充值500送500一次到账1000元，打电话每分钟低至0.06元/分钟" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_more_i);
		ImageButton fhFH = (ImageButton) findViewById(R.id.imageButtonId_IxinMore);
		fhFH.setOnClickListener(this);
		TextView titleName = (TextView) findViewById(R.id.textViewId_title_IxinMore);
		TextView textView1 = (TextView) findViewById(R.id.textView1);
		textView1.setText(data[0]);
		Intent intent = getIntent();
		String title = intent.getStringExtra("titleName");
		titleName.setText(title);
	}

	@Override
	public void onClick(View arg0) {
		finish();
	}
}
