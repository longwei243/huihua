package com.saiman.smcall.options.im;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.saiman.smcall.MainActivity;
import com.saiman.smcall.R;
import com.saiman.smcall.app.MobileApplication;
import com.saiman.smcall.options.login.LoginActivity;
import com.saiman.smcall.request.RequestUrl;
import com.saiman.smcall.util.LogUtil;
import com.saiman.smcall.view.AutoScrollTextView;

public class IMFragment extends Fragment implements OnClickListener{
	private View view;
	private ImageButton mTouXiang;
	private ProgressBar probar;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_im, null);
		init();
		return view;
	}
	
	private AutoScrollTextView autoScrollTextView;

	private void init() {

		autoScrollTextView = (AutoScrollTextView) view
				.findViewById(R.id.TextViewNotice);
		autoScrollTextView.init(((Activity) view.getContext())
				.getWindowManager());
		autoScrollTextView.startScroll();

		sharedPreferences = view.getContext().getSharedPreferences("saiman",
				Context.MODE_WORLD_READABLE);
		mTouXiang = (ImageButton) view
				.findViewById(R.id.imageButtonId_TouXiang_More);
		TextView name = (TextView) view.findViewById(R.id.textViewId_Num_More);
		ImageButton ziFei = (ImageButton) view
				.findViewById(R.id.imageButtonId_ZiFei_More);
		ImageButton chaXun = (ImageButton) view
				.findViewById(R.id.imageButtonId_ChaXun_More);
		ImageButton shangJi = (ImageButton) view
				.findViewById(R.id.imageButtonId_ShangJi_More);
		ImageButton zhuXiao = (ImageButton) view
				.findViewById(R.id.imageButtonId_ZhuXiao_More);
		ImageButton guanYu = (ImageButton) view
				.findViewById(R.id.imageButtonId_GuanYu_More);
		ImageButton ChongZhi = (ImageButton) view
				.findViewById(R.id.imageButtonId_ChongZhi_More);
		ImageButton changePassword = (ImageButton) view
				.findViewById(R.id.more_changePassword);
		changePassword.setOnClickListener(this);
		ziFei.setOnClickListener(this);
		chaXun.setOnClickListener(this);
		shangJi.setOnClickListener(this);
		zhuXiao.setOnClickListener(this);
		guanYu.setOnClickListener(this);
		ChongZhi.setOnClickListener(this);
		mTouXiang.setOnClickListener(this);
		nameE = sharedPreferences.getString("UserName", null);
		numM = sharedPreferences.getString("PassWord", null);
		name.setText(nameE);

		Button kfMore = (Button) view.findViewById(R.id.buttonId_KF_More);
		kfMore.setOnClickListener(this);
		ImageView sheZhi = (ImageView) view
				.findViewById(R.id.imageViewId_MoreFunction_SheZhi);
		sheZhi.setOnClickListener(this);
	}

	private String[] split = null;
	private Handler handlerMore = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {

			case 1866:
				String s = (String) msg.obj;
				if (s != null) {
					split = s.split(",");
					if (getVersion().equals(split[0])||"null".equals(split[0])) {
						new AlertDialog.Builder(view.getContext())
								.setTitle("版本信息")
								.setMessage("已是最新版本,无需下载") 
								.setNegativeButton("确定",
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										}).show();
					} else {
						new AlertDialog.Builder(view.getContext())
								.setTitle("版本信息")
								.setMessage(
										"最新版本是:" + split[0] + ",您现在的版本是:"
												+ getVersion())
								.setPositiveButton("确定更新",
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface dialog,
													int which) {
												getNewAkp();
												dialog.dismiss();
											}
										})
								.setNegativeButton("暂不更新",
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										}).show();
					}
				}
				break;
			case 736:
				String sss = (String) msg.obj;
				if ('1' == sss.charAt(2)) {
					String split = sss.substring(4, sss.length() - 43);
					new AlertDialog.Builder(view.getContext())
							.setTitle("余额")
							.setMessage("您的余额为:" + split + "元")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).show();
				}
				break;
			}
		}
	};

	public String getVersion() {
		String version=null;
		try {
			PackageManager manager = view.getContext().getPackageManager();
			PackageInfo info = manager.getPackageInfo(view.getContext()
					.getPackageName(), 0);
			 version = info.versionName;
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return version;
	}
	//从服务器打开最新版本地址
	private void getNewAkp() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(split[1]);
		intent.setData(content_url);
		view.getContext().startActivity(intent);

	}

	private String mBanBenPath = RequestUrl.pathVesion(RequestUrl.userid);

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.imageButtonId_TouXiang_More:
			// Toast.makeText(context, "头像", Toast.LENGTH_SHORT).show();
			break;
		case R.id.imageButtonId_ZiFei_More:
			// Toast.makeText(context, "资费", Toast.LENGTH_SHORT).show();
			Intent intentZF = new Intent(view.getContext(), MoreI.class);
			intentZF.putExtra("titleName", "资费标准");
			view.getContext().startActivity(intentZF);

			break;
		case R.id.imageButtonId_ChaXun_More:
			Intent intentCX = new Intent(view.getContext(), ChaXun.class);
			view.getContext().startActivity(intentCX);
			break;
		case R.id.imageButtonId_ShangJi_More:
			// Toast.makeText(context, "目前已是最新版本了", Toast.LENGTH_SHORT).show();
			if (networkStatusOK()) {
//				new IXinShangJi(handlerMore, view.getContext())
//						.execute(mBanBenPath);
				MobileApplication.requestQueue.add(new StringRequest(Method.GET, mBanBenPath, 
						new Response.Listener<String>() {
		
							@Override
							public void onResponse(String response) {
								LogUtil.i(this.getClass().getSimpleName(), "登陆返回数据："+response);
								if (response != null) {
									JSONObject jsonObject3;
									try {
										jsonObject3 = new JSONObject(response);
										String banBen = jsonObject3.getString("VersionNumber");
										String diZhi = jsonObject3.getString("Url");
										String s = banBen + "," + diZhi;
										if (s != null) {
											split = s.split(",");
											if (getVersion().equals(split[0])||"null".equals(split[0])) {
												new AlertDialog.Builder(view.getContext())
														.setTitle("版本信息")
														.setMessage("已是最新版本,无需下载") 
														.setNegativeButton("确定",
																new DialogInterface.OnClickListener() {

																	public void onClick(
																			DialogInterface dialog,
																			int which) {
																		dialog.dismiss();
																	}
																}).show();
											} else {
												new AlertDialog.Builder(view.getContext())
														.setTitle("版本信息")
														.setMessage(
																"最新版本是:" + split[0] + ",您现在的版本是:"
																		+ getVersion())
														.setPositiveButton("确定更新",
																new DialogInterface.OnClickListener() {

																	public void onClick(
																			DialogInterface dialog,
																			int which) {
																		getNewAkp();
																		dialog.dismiss();
																	}
																})
														.setNegativeButton("暂不更新",
																new DialogInterface.OnClickListener() {

																	public void onClick(
																			DialogInterface dialog,
																			int which) {
																		dialog.dismiss();
																	}
																}).show();
											}
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}, new Response.ErrorListener() {
		
							@Override
							public void onErrorResponse(VolleyError error) {
							}
						}));
			} else {
				Toast.makeText(view.getContext(), "无网络连接...",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.imageButtonId_ZhuXiao_More:
			new AlertDialog.Builder(view.getContext())
					.setTitle("提示")
					.setMessage("是否注销")
					.setPositiveButton("是",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									Editor edit = sharedPreferences.edit();
									edit.remove("UserName");
									edit.remove("PassWord");
									edit.commit();
									// Intent intentT = new
									// Intent(getActivity(),
									// MainActivity.class);
									// getActivity().startActivity(intentT);
									System.exit(0);
									dialog.dismiss();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();

			break;
		case R.id.imageButtonId_GuanYu_More:
			// Toast.makeText(context, "关于", Toast.LENGTH_SHORT).show();
			Intent intentGY = new Intent(view.getContext(), GuanYuMe.class);
			view.getContext().startActivity(intentGY);
			break;
		case R.id.imageButtonId_ChongZhi_More:
			// Toast.makeText(context, "充值", Toast.LENGTH_SHORT).show();
			if (networkStatusOK()) {
				Intent intent = new Intent(view.getContext(), ChongZhi.class);
				view.getContext().startActivity(intent);
			} else {
				Toast.makeText(view.getContext(), "无网络连接...",
						Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.buttonId_KF_More:
			Intent intent1 = new Intent();
			intent1.setAction("android.intent.action.DIAL");
			intent1.setData(Uri.parse("tel:13803417365"));
			view.getContext().startActivity(intent1);
			break;
		case R.id.imageViewId_MoreFunction_SheZhi:
			// Intent intent = new Intent(getActivity(), SheZhiActivity.class);
			// getActivity().startActivity(intent);
			Toast.makeText(view.getContext(), "暂未开放", Toast.LENGTH_SHORT)
					.show();
			// Intent intent2 = new Intent(view.getContext(), SetUp.class);
			// view.getContext().startActivity(intent2);
			break;
		case R.id.more_changePassword:
			Intent intentcp = new Intent(view.getContext(), ChangePassword.class);
			view.getContext().startActivity(intentcp);
			break;
		}
	}

	private boolean networkStatusOK() {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) view
					.getContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
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

	private SharedPreferences sharedPreferences = null;
	private String nameE = null;
	private String numM = null;
	private String mChaXun = "http://182.92.151.130/vos/api/balance2?a=";
}
