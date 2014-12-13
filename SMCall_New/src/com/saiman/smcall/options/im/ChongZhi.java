package com.saiman.smcall.options.im;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.saiman.smcall.R;
import com.saiman.smcall.app.MobileApplication;
import com.saiman.smcall.request.RequestUrl;
import com.saiman.smcall.util.LogUtil;

public class ChongZhi extends Activity implements OnClickListener {
	private EditText mCH;
	private EditText mMM;
	private EditText mSJ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chong_zhi);
		init();

	}

	private void init() {
		mCH = (EditText) findViewById(R.id.editTextId_ch_CZ);
		mMM = (EditText) findViewById(R.id.editTextId_mm_CZ);
		mSJ = (EditText) findViewById(R.id.editTextId_sj_CZ);
		Button cz = (Button) findViewById(R.id.buttonId_cz_CZ);
		Button fh = (Button) findViewById(R.id.buttonId_fh_CZ);
		Button gk = (Button) findViewById(R.id.buttonId_zx_GK);
		cz.setOnClickListener(this);
		fh.setOnClickListener(this);
		gk.setOnClickListener(this);
		SharedPreferences sharedPreferences = getSharedPreferences("saiman",
				Context.MODE_WORLD_READABLE);
		String namees = sharedPreferences.getString("UserName", null);
		mSJ.setText(namees);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 69:
				String ss = (String) msg.obj;
				if (ss != null) {
					char charAt = ss.charAt(2);
					if ('1' == charAt) {
						Toast.makeText(ChongZhi.this, "充值成功!",
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(ChongZhi.this, "充值失败："+ss,
								Toast.LENGTH_LONG).show();
					}
				}
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.buttonId_cz_CZ:
			if (networkStatusOK()) {
				String sjt = mSJ.getText().toString();
				String cht = mCH.getText().toString();
				String mmt = mMM.getText().toString();
				if (sjt != null) {
					//String path = mCZPath + sjt + "&b=" + cht + "&c=" + mmt
							;
					final StringBuffer path = new StringBuffer(RequestUrl.chongzhiURL.replace("[用户手机号码]", sjt)
									.replace("[卡号]",cht).replace("[卡密]", mmt));  
//					new IXinChongZhi(handler, this).execute(path.toString());
					MobileApplication.requestQueue.add(new StringRequest(Method.GET, path.toString(), 
							new Response.Listener<String>() {
			
								@Override
								public void onResponse(String response) {
									LogUtil.i(this.getClass().getSimpleName(), "登陆返回数据："+response);
									if (response != null) {
										char charAt = response.charAt(2);
										if ('1' == charAt) {
											Toast.makeText(ChongZhi.this, "充值成功!",
													Toast.LENGTH_LONG).show();
										} else {
											Toast.makeText(ChongZhi.this, "充值失败："+response,
													Toast.LENGTH_LONG).show();
										}
									}
								}
							}, new Response.ErrorListener() {
			
								@Override
								public void onErrorResponse(VolleyError error) {
								}
							}));
				}
			} else {
				Toast.makeText(this, "无网络连接，请检查网络状态", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.buttonId_fh_CZ:
			finish();
			break;
		case R.id.buttonId_zx_GK:
			 Uri uri = Uri.parse("http://ma.taobao.com/ezj8i");  
		      	Intent it = new Intent(Intent.ACTION_VIEW, uri);  
		      	startActivity(it);
			break;
		}
	}

	private boolean networkStatusOK() {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
}