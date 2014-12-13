package com.saiman.smcall.options.dial;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.saiman.smcall.R;
import com.saiman.smcall.app.MobileApplication;
import com.saiman.smcall.domain.LandedDate;
import com.saiman.smcall.event.DialEvent;
import com.saiman.smcall.options.dial.adapter.DialAdapter;
import com.saiman.smcall.options.dial.adapter.GalleryAdapter;
import com.saiman.smcall.options.login.LoginActivity;
import com.saiman.smcall.options.netmeeting.NetMeetingActivity;
import com.saiman.smcall.options.web.WebCenter;
import com.saiman.smcall.request.RequestUrl;
import com.saiman.smcall.view.FlowIndicator;

import de.greenrobot.event.EventBus;

public class DialFragment extends Fragment{
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private ListView mListView;
	private LinearLayout mKeyboard;
	private EditText mEditText;
	private AsyncQueryHandler asyncQuery;
	//广告
	private View bohao_ad;
	@SuppressWarnings("deprecation")
	private Gallery homepage_header_Gallery;
	private FlowIndicator homepage_header_FlowIndicator;
	private GalleryAdapter galleryAdapter;
	private List<Map<String, String>> galleryData;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_dial, null);
		initListView(view);
		
		initKeyboard(view);
		
		initAd(view);
		
		return view;
	}
	
	private void initAd(View view) {
		
		bohao_ad = view.findViewById(R.id.bohao_ad);
		bohao_ad.setVisibility(View.VISIBLE);
		homepage_header_Gallery = (Gallery) view.findViewById(R.id.homepage_header_Gallery);
		homepage_header_FlowIndicator = (FlowIndicator) view.findViewById(R.id.homepage_header_FlowIndicator);
		galleryData = new ArrayList<Map<String, String>>();
		Map<String, String> maptemp = new HashMap<String, String>();
		maptemp.put("picPath", "assets/img/default_big.png");
		maptemp.put("Url", "");
		galleryData.add(maptemp);
		galleryAdapter = new GalleryAdapter(getActivity(), galleryData);
		homepage_header_Gallery.setAdapter(galleryAdapter);
		homepage_header_FlowIndicator.setCount(1);
		
		GetAdTask task = new GetAdTask();
		task.execute();
	}

	private void initKeyboard(View view) {
		mKeyboard = (LinearLayout) view.findViewById(R.id.keyboardId_main);
		mEditText = (EditText)  view.findViewById(R.id.editTextId_main);
		mEditText.setInputType(InputType.TYPE_NULL);
		ImageButton delete = (ImageButton)  view.findViewById(R.id.imageButtonId_delete_main);
		ImageButton num1 = (ImageButton)  view.findViewById(R.id.buttonId_num1_main);
		ImageButton num2 = (ImageButton)  view.findViewById(R.id.buttonId_num2_main);
		ImageButton num3 = (ImageButton)  view.findViewById(R.id.buttonId_num3_main);
		ImageButton num4 = (ImageButton)  view.findViewById(R.id.buttonId_num4_main);
		ImageButton num5 = (ImageButton)  view.findViewById(R.id.buttonId_num5_main);
		ImageButton num6 = (ImageButton)  view.findViewById(R.id.buttonId_num6_main);
		ImageButton num7 = (ImageButton)  view.findViewById(R.id.buttonId_num7_main);
		ImageButton num8 = (ImageButton)  view.findViewById(R.id.buttonId_num8_main);
		ImageButton num9 = (ImageButton)  view.findViewById(R.id.buttonId_num9_main);
		ImageButton num0 = (ImageButton)  view.findViewById(R.id.buttonId_num0_main);
		Button numX = (Button)  view.findViewById(R.id.buttonId_numX_main);
		Button numJ = (Button)  view.findViewById(R.id.buttonId_numJ_main);
		num1.setOnClickListener(new NumberOnClickListener());
		num2.setOnClickListener(new NumberOnClickListener());
		num3.setOnClickListener(new NumberOnClickListener());
		num4.setOnClickListener(new NumberOnClickListener());
		num5.setOnClickListener(new NumberOnClickListener());
		num6.setOnClickListener(new NumberOnClickListener());
		num7.setOnClickListener(new NumberOnClickListener());
		num8.setOnClickListener(new NumberOnClickListener());
		num9.setOnClickListener(new NumberOnClickListener());
		num0.setOnClickListener(new NumberOnClickListener());
		numX.setOnClickListener(new NumberOnClickListener());
		numJ.setOnClickListener(new NumberOnClickListener());
		delete.setOnClickListener(new NumberOnClickListener());

		delete.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				mEditText.setText("");
				return false;
			}
		});

		mEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				String text = mEditText.getText().toString();
				if (text.length() != 0) {
					bohao_ad.setVisibility(View.GONE);
				} else {
					bohao_ad.setVisibility(View.VISIBLE);
				}
				if (text.length() != 0 && text != null) {
					List<Map<String, Object>> listFH = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> map = list.get(i);
						String number = map.get("number").toString();
						if (number.length() != 0) {
							if (number.contains(text)) {
								listFH.add(map);
							}
						}
					}
					mListView.setAdapter(new DialAdapter(listFH,
							getActivity()));
				} else {
					mListView.setAdapter(new DialAdapter(list,
							getActivity()));
				}
			}
		});
	}
	
	private class NumberOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			StringBuffer sb = new StringBuffer(mEditText.getText().toString());
			switch (v.getId()) {
			case R.id.buttonId_num1_main:
				sb.append("1");
				break;
			case R.id.buttonId_num2_main:
				sb.append("2");
				break;
			case R.id.buttonId_num3_main:
				sb.append("3");
				break;
			case R.id.buttonId_num4_main:
				sb.append("4");
				break;
			case R.id.buttonId_num5_main:
				sb.append("5");
				break;
			case R.id.buttonId_num6_main:
				sb.append("6");
				break;
			case R.id.buttonId_num7_main:
				sb.append("7");
				break;
			case R.id.buttonId_num8_main:
				sb.append("8");
				break;
			case R.id.buttonId_num9_main:
				sb.append("9");
				break;
			case R.id.buttonId_num0_main:
				sb.append("0");
				break;
			case R.id.buttonId_numX_main:
				//电话会议
				Intent intentq = new Intent(getActivity(), NetMeetingActivity.class);
				startActivity(intentq);
				break;
			case R.id.buttonId_numJ_main:
				//拨号
				if (networkStatusOK()) {
					String callName = mEditText.getText().toString();
					if (callName.length() > 2) {
						if (getLandName()) {

							Intent intent = new Intent(getActivity(),
									CallPhone.class);
							intent.putExtra("CALLNAME", "");
							intent.putExtra("CALLNUMBER", mEditText.getText()
									.toString());
							startActivity(intent);
						} else {
							Intent intent = new Intent(getActivity(),
									LoginActivity.class);
							startActivity(intent);
						}
					} else {
						Toast.makeText(getActivity(), "请输入正确号码",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getActivity(), "无网络,无法拨号,请检查您的网络",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.imageButtonId_delete_main:
				if (mEditText.getText().length() != 0) {
					sb.deleteCharAt(sb.length() - 1);
				}
				break;
			}
			if (sb.toString() != null) {
				mEditText.setText(sb.toString());
				mEditText.setSelection(sb.length());
			}
		}
	}
	
	
	//============================================================
	private void initListView(View view) {
		mListView = (ListView) view.findViewById(R.id.listViewId_main);

		Cursor cursor = getActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI,
				null, null, null, null);

		if (cursor != null) {
			cursor.moveToFirst();
			ContentResolver resolver = getActivity().getContentResolver();
			while (cursor.moveToNext()) {
				String number = cursor.getString(cursor
						.getColumnIndex(CallLog.Calls.NUMBER));
				int id = cursor
						.getInt(cursor.getColumnIndex(CallLog.Calls._ID));
				if ("-1".equals(number) || "-2".equals(number)
						|| "".equals(number) || number == null) {
					resolver.delete(CallLog.Calls.CONTENT_URI,
							CallLog.Calls._ID + "=?", new String[] { id + "" });
				}
			}
			asyncQuery = new MyAsyncQueryHandler(getActivity().getContentResolver());
			if (asyncQuery != null) {
				initVV();
			}
		}
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				final TextView callName = (TextView) v
						.findViewById(R.id.textViewId_name_fill_dial);
				final TextView callNumber = (TextView) v
						.findViewById(R.id.textViewId_number_fill_dial);
				String name = callName.getText().toString();
				String number = callNumber.getText().toString();
				if (number != null) {
					if (name.equals(number)) {
						name = "";
					}
					if (networkStatusOK()) {
						
						if (getLandName()) {
							new AlertDialog.Builder(getActivity())
									.setTitle("拨号")
									.setMessage(name + "   " + number)
									.setPositiveButton(
											"呼叫",
											new DialogInterface.OnClickListener() {

												public void onClick(
														DialogInterface dialog,
														int which) {

													if (networkStatusOK()) {
														Intent intent = new Intent(
																getActivity(),
																CallPhone.class);
														intent.putExtra(
																"CALLNAME",
																callName.getText()
																		.toString());
														intent.putExtra(
																"CALLNUMBER",
																callNumber
																		.getText()
																		.toString());
														startActivity(intent);
													} else {
														Toast.makeText(
																getActivity(),
																"无网络,无法拨号...",
																Toast.LENGTH_SHORT)
																.show();
													}
													dialog.dismiss();
												}
											})
									.setNegativeButton(
											"返回",
											new DialogInterface.OnClickListener() {

												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.dismiss();
												}
											}).show();

						} else {
							Intent intent = new Intent(getActivity(),
									LoginActivity.class);
							startActivity(intent);
						}

					} else {
						Toast.makeText(getActivity(), "无网络,无法拨号,请检查您的网络",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	private boolean networkStatusOK() {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) MobileApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
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
	

	private void initVV() {
		Uri uri = android.provider.CallLog.Calls.CONTENT_URI;
		String[] projection = { CallLog.Calls.DATE, CallLog.Calls.NUMBER,
				CallLog.Calls.TYPE, CallLog.Calls.CACHED_NAME,
				CallLog.Calls._ID, };
		if (asyncQuery != null) {
			asyncQuery.startQuery(0, null, uri, projection, null, null,
					CallLog.Calls.DEFAULT_SORT_ORDER);
		}
	}
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {

			Cursor cursor1 = getActivity().getContentResolver().query(
					CallLog.Calls.CONTENT_URI, null, null, null, null);
			ContentResolver resolver = getActivity().getContentResolver();
			if (cursor1 != null) {
				cursor1.moveToFirst();
				while (cursor1.moveToNext()) {
					String number = cursor1.getString(cursor1
							.getColumnIndex(CallLog.Calls.NUMBER));
					int id = cursor1.getInt(cursor1
							.getColumnIndex(CallLog.Calls._ID));
					if ("-1".equals(number) || "-2".equals(number)
							|| "".equals(number) || number == null) {
						resolver.delete(CallLog.Calls.CONTENT_URI,
								CallLog.Calls._ID + "=?", new String[] { id
										+ "" });
					}
				}
			}
			if (cursor != null) {
				cursor.moveToFirst();
				if (cursor != null && cursor.getCount() > 0) {
					List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
					SimpleDateFormat sfd = new SimpleDateFormat("MM-dd hh:mm");
					Date date;
					cursor.moveToFirst();
					for (int i = 0; i < cursor.getCount(); i++) {
						cursor.moveToPosition(i);
						date = new Date(cursor.getLong(cursor
								.getColumnIndex(CallLog.Calls.DATE)));
						String number = cursor.getString(cursor
								.getColumnIndex(CallLog.Calls.NUMBER));
						int type = cursor.getInt(cursor
								.getColumnIndex(CallLog.Calls.TYPE));
						String cachedName = cursor.getString(cursor
								.getColumnIndex(CallLog.Calls.CACHED_NAME));
						int id = cursor.getInt(cursor
								.getColumnIndex(CallLog.Calls._ID));

						Map<String, Object> map = new HashMap<String, Object>();
						map.put("id", id);
						map.put("number", number);
						map.put("type", type);
						map.put("date", sfd.format(date));
						map.put("cachedName", cachedName);
						if ("".equals(number) || null == number) {
							map.put("number", "暂无号码");
						}

						if (null == cachedName || "".equals(cachedName)) {
							map.put("cachedName", "未命名");
						}
						lists.add(map);
					}
					if (lists.size() > 0) {
						mListView.setAdapter(new DialAdapter(lists,
								getActivity()));
						list = lists;
					}
				}
			}
			super.onQueryComplete(token, cookie, cursor);
		}
	}
	//==========================================================
	
	private boolean getLandName() {
		LandedDate landedDate = new LandedDate(getActivity());
		String passWord = landedDate.getPassWord();
		String userName = landedDate.getUserName();
		if (passWord != null || userName != null) {
			return true;
		}
		return false;
	}
	
	
	
	public void onEventMainThread(DialEvent de) {
		SharedPreferences settings = getActivity().getSharedPreferences(MobileApplication.getInstance().getResources().getString(R.string.app_name), Activity.MODE_PRIVATE+  Activity.MODE_WORLD_READABLE + Activity.MODE_WORLD_WRITEABLE);
        final String dia_key=settings.getString("User_dkey", "") .toString().trim().toLowerCase()+"";
        if(dia_key.equals("") | dia_key.equals("显示")){
        	dialPadShow();
    	}else{
    		dialPadGone();
    	}
	}
	
	private void dialPadGone() {
		mKeyboard.setVisibility(View.GONE);
		bohao_ad.setVisibility(View.GONE);
	}

	private void dialPadShow() {
		mKeyboard.setVisibility(View.VISIBLE);
		bohao_ad.setVisibility(View.VISIBLE);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
	/**
	 * 
	 * 
	 * @author LongWei
	 * 
	 */
	class GetAdTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result == null) {
				return;
			}
			try {
				System.out.println("result-----------" + result);
				JSONObject jsonObject = new JSONObject(result);
				boolean success = (Boolean) jsonObject.get("success");
				if (success) {
					JSONArray jsonArray = jsonObject.getJSONArray("ads");
					if (jsonArray.length() == 0) {
						String picPath = "assets/img/defaultpic_big.png";
						String url = "";
						Map<String, String> map = new HashMap<String, String>();
						map.put("picPath",
								picPath.replace("localhost", "182.92.157.109"));
						map.put("url", url);
						galleryData.add(map);
						galleryAdapter.notifyDataSetChanged();
						return;
					}
					if (jsonArray.length() > 0) {
						galleryData.clear();
					}
					homepage_header_FlowIndicator.setCount(jsonArray.length());
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject obj = (JSONObject) jsonArray.get(i);
						String picPath = "assets/img/defaultpic_big.png";
						String url = "";

						if (obj.has("PicPath")) {
							if (obj.has("PicPath")) {
								if (obj.get("PicPath").toString()== "null") {
									picPath = "";
								} else {
									picPath = (String) obj.get("PicPath");
								}
							}
						}

						if (obj.has("Url")) {
							if (obj.get("Url").toString() =="null") {
								url = "";
							} else {
								url = (String) obj.get("Url");
							}
						}
						Map<String, String> map = new HashMap<String, String>();
						map.put("picPath",
								picPath.replace("localhost", "182.92.157.109"));
						map.put("url", url);
						galleryData.add(map);

					}

					homepage_header_Gallery
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(
										AdapterView<?> adapterView, View view,
										int position, long l) {
									homepage_header_FlowIndicator
											.setSeletion(position);
								}

								@Override
								public void onNothingSelected(
										AdapterView<?> adapterView) {

								}
							});

					galleryAdapter.notifyDataSetChanged();
					homepage_header_Gallery
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(
										AdapterView<?> adapterView, View view,
										int position, long l) {
									Map<String, String> map = (Map<String, String>) galleryAdapter
											.getItem(position);
									String url = map.get("url");
									if (url=="") {
										return;
									}
									Intent intent = new Intent(
											getActivity(), WebCenter.class);
									intent.putExtra("OpenUrl", url);
									startActivity(intent);
								}
							});
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected String doInBackground(Void... params) {
			String url =RequestUrl.pathMainActivity;
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse;
			try {
				httpResponse = new DefaultHttpClient().execute(httpGet);
				
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					
					String strResult = EntityUtils.toString(httpResponse
							.getEntity());
					return strResult;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

	}
}
