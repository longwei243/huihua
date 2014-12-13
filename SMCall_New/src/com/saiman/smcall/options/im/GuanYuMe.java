package com.saiman.smcall.options.im;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.saiman.smcall.R;

public class GuanYuMe extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guan_yu_me);
		init();
	}

	private void init() {
		TextView sjgw = (TextView) findViewById(R.id.textViewId_sjgw_guanyu);
		sjgw.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		TextView kfdh = (TextView) findViewById(R.id.textViewId_kfdh_guanyu);
		kfdh.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		sjgw.setOnClickListener(this);
		kfdh.setOnClickListener(this);
		ImageButton button = (ImageButton) findViewById(R.id.imageButtonId_ddMore);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textViewId_sjgw_guanyu:
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			Uri content_url = Uri.parse("http://182.92.157.215/index.php?g=Wap&m=Index&a=index&token=wkhiqq1405655853");
			intent.setData(content_url);
			startActivity(intent);
			break;
		case R.id.textViewId_kfdh_guanyu:
			Intent intent1 = new Intent();
			intent1.setAction("android.intent.action.DIAL");
			intent1.setData(Uri.parse("tel:18635121659"));
			startActivity(intent1);
			break;
		case R.id.imageButtonId_ddMore:
			finish();
			break;

		}

	}
}
