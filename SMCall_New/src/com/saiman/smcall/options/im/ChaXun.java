package com.saiman.smcall.options.im;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.saiman.smcall.R;
import com.saiman.smcall.app.MobileApplication;
import com.saiman.smcall.domain.LandedDate;
import com.saiman.smcall.options.login.LoginActivity;
import com.saiman.smcall.request.RequestUrl;
import com.saiman.smcall.util.LogUtil;

public class ChaXun extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cha_xun);
		init();
	}

	private Handler handlerMore = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 736:
				String sss = (String) msg.obj;
				if ('1' == sss.charAt(2)) {

					String[] split2 = sss.split("[|]");
					phoneYE.setText("您的余额为:" + split2[2] + "元");
					phoneYX.setText(split2[3]);
				}
				break;
			default:
				break;
			}
		}
	};
	private SharedPreferences sharedPreferences = null;
	private String nameE = null;
	private String numM = null;
	private TextView phoneYE = null;
	private TextView phoneYX;

	private boolean getLandName() {
		LandedDate landedDate = new LandedDate(this);
		String passWord = landedDate.getPassWord();
		String userName = landedDate.getUserName();
		if (passWord != null || userName != null) {
			return true;
		}
		return false;
	}

	private void init() {
		ImageButton fhChaXun = (ImageButton) findViewById(R.id.textViewId_FH_ChaXun);
		fhChaXun.setOnClickListener(this);
		TextView sxChaXun = (TextView) findViewById(R.id.textViewId_SX_ChaXun);
		sxChaXun.setOnClickListener(this);
		TextView phoneNum = (TextView) findViewById(R.id.textViewId_PhoneNum_ChaXun);
		phoneYE = (TextView) findViewById(R.id.textViewId_PhoneYE_ChaXun);
		phoneYX = (TextView) findViewById(R.id.textViewId_PhoneYX_ChaXun);
		sharedPreferences = getSharedPreferences("saiman",
				Context.MODE_WORLD_READABLE);
		nameE = sharedPreferences.getString("UserName", null);
		numM = sharedPreferences.getString("PassWord", null);
		phoneNum.setText(nameE);
		if (networkStatusOK()) {
			if (getLandName()) {
				//String ss = mChaXun + nameE + "&b=" + numM + "&parentid=1015";
				final StringBuffer ss = new StringBuffer(RequestUrl.chaxunURL.replace("[用户手机号码]", nameE)
            		.replace("[密码]", numM));  
//				new IXinChaXun(handlerMore, this).execute(ss.toString());
				MobileApplication.requestQueue.add(new StringRequest(Method.GET, ss.toString(), 
						new Response.Listener<String>() {
		
							@Override
							public void onResponse(String response) {
								LogUtil.i(this.getClass().getSimpleName(), "登陆返回数据："+response);
								if (response != null) {
									if ('1' == response.charAt(2)) {

										String[] split2 = response.split("[|]");
										phoneYE.setText("您的余额为:" + split2[2] + "元");
										phoneYX.setText(split2[3]);
									}
								}
							}
						}, new Response.ErrorListener() {
		
							@Override
							public void onErrorResponse(VolleyError error) {
								Toast.makeText(ChaXun.this, "失败", Toast.LENGTH_SHORT).show();
							}
						}));
			} else {
				Toast.makeText(this, "请先登录在查询", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(this, LoginActivity.class);
				this.startActivity(intent);
			}
		} else {
			Toast.makeText(this, "无网络连接...", Toast.LENGTH_SHORT).show();
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

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.textViewId_FH_ChaXun:
			finish();
			break;
		case R.id.textViewId_SX_ChaXun:
			if (networkStatusOK()) {
				//String ss = mChaXun + nameE + "&b=" + numM;
				final StringBuffer ss = new StringBuffer(RequestUrl.chaxunURL.replace("[用户手机号码]", nameE)
							.replace("[密码]", numM));  
//				new IXinChaXun(handlerMore, this).execute(ss.toString());
				MobileApplication.requestQueue.add(new StringRequest(Method.GET, ss.toString(), 
						new Response.Listener<String>() {
		
							@Override
							public void onResponse(String response) {
								LogUtil.i(this.getClass().getSimpleName(), "登陆返回数据："+response);
								if (response != null) {
									if ('1' == response.charAt(2)) {

										String[] split2 = response.split("[|]");
										phoneYE.setText("您的余额为:" + split2[2] + "元");
										phoneYX.setText(split2[3]);
									}
								}
							}
						}, new Response.ErrorListener() {
		
							@Override
							public void onErrorResponse(VolleyError error) {
								Toast.makeText(ChaXun.this, "失败", Toast.LENGTH_SHORT).show();
							}
						}));
			} else {
				Toast.makeText(this, "无网络连接...", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}

	}
}
