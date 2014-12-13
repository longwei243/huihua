package com.saiman.smcall.options.login;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.saiman.smcall.R;
import com.saiman.smcall.app.MobileApplication;
import com.saiman.smcall.request.RequestUrl;
import com.saiman.smcall.util.LogUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener{


	private EditText mUserName;
	private EditText mPassWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_registration);
		init();
	}

	private void init() {
		mUserName = (EditText) findViewById(R.id.editTextId_UserName_Registration);
		mPassWord = (EditText) findViewById(R.id.editTextId_PassWord_Registration);
		Button Registration = (Button) findViewById(R.id.buttonId_Registration_Registration);
		Button Return = (Button) findViewById(R.id.buttonId_Return_Registration);
		Registration.setOnClickListener(this);
		Return.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.buttonId_Registration_Registration:
			String userName = mUserName.getText().toString();
			String passWord = mPassWord.getText().toString();
			if (userName != null && passWord != null) {
				final StringBuffer path = new StringBuffer(RequestUrl.registURL.replace("[手机号码]", userName)
										.replace("[验证码]", "")
										.replace("[推介人]", "2")
										.replace("[用户密码]", passWord));  
				MobileApplication.requestQueue.add(new StringRequest(Method.GET, path.toString(), 
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						LogUtil.i(this.getClass().getSimpleName(), "注册返回的数据："+response);
						if (response != null) {
							if ('1' == response.charAt(2)) {
								Toast.makeText(RegisterActivity.this, "注册成功",
										Toast.LENGTH_SHORT).show();
								finish();
							} else {
								Toast.makeText(RegisterActivity.this, "该用户已存在...",
										Toast.LENGTH_SHORT).show();
							}
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
					}
				}));
			} else {
				Toast.makeText(this, "网络不给力", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.buttonId_Return_Registration:
			finish();
			break;

		}
	}

}
