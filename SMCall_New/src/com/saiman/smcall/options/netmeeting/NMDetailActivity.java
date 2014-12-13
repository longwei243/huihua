package com.saiman.smcall.options.netmeeting;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.saiman.smcall.MainActivity;
import com.saiman.smcall.R;
import com.saiman.smcall.app.MobileApplication;
import com.saiman.smcall.domain.LandedDate;
import com.saiman.smcall.options.login.LoginActivity;
import com.saiman.smcall.request.RequestUrl;
import com.saiman.smcall.util.LogUtil;

public class NMDetailActivity extends Activity{

	private List<SelectContactBean> bohaolist;
	private List<SelectContactBean> addbohaolist;
	private List<SelectContactBean> xuanhaolist;
	private List<SelectContactBean> addxuanhaolist;
	private List<SelectContactBean> templist = new ArrayList<SelectContactBean>();
	
	private static List<SelectContactBean> dataList = new ArrayList<SelectContactBean>();

	GridView nmdetail_gridview;
	DetailGridViewAdapter adapter;
	
	private Button nmdetail_btn_call;
	private Button nmdetail_btn_selectall;
	private Button nmdetail_btn_add;
	
	private String meetid = "";
	
	private String userName;
	private String passWord;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		Intent intent = getIntent();
		if(intent.getSerializableExtra("bohaolist") != null) {
			dataList.clear();
			bohaolist = (List<SelectContactBean>) intent.getSerializableExtra("bohaolist");
			LogUtil.i("拨号传递过来的list：", bohaolist.size()+"");
			
			dataList.addAll(bohaolist);
		}
		if(intent.getSerializableExtra("addbohaolist") != null) {
			addbohaolist = (List<SelectContactBean>) intent.getSerializableExtra("addbohaolist");
			LogUtil.i("add拨号传递过来的list：", addbohaolist.size()+"");
			
			dataList.addAll(addbohaolist);
		}
		if(intent.getSerializableExtra("xuanhaolist") != null) {
			dataList.clear();
			xuanhaolist = (List<SelectContactBean>) intent.getSerializableExtra("xuanhaolist");
			LogUtil.i("选号传递过来的list：", xuanhaolist.size()+"");
			
			dataList.addAll(xuanhaolist);
		}
		if(intent.getSerializableExtra("addxuanhaolist") != null) {
			addxuanhaolist = (List<SelectContactBean>) intent.getSerializableExtra("addxuanhaolist");
			LogUtil.i("add选号传递过来的list：", addxuanhaolist.size()+"");
			
			dataList.addAll(addxuanhaolist);
		}
		
		setContentView(R.layout.activity_nmdetail);
		nmdetail_gridview = (GridView) findViewById(R.id.nmdetail_gridview);
		adapter = new DetailGridViewAdapter(NMDetailActivity.this, dataList);
		nmdetail_gridview.setAdapter(adapter);
		
		nmdetail_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				DetailGridViewAdapter.ViewHolder holder = (DetailGridViewAdapter.ViewHolder) arg1.getTag();
				holder.select.toggle();
				if(holder.select.isChecked()) {
					DetailGridViewAdapter.getIsSelected().put(position, true);
				}
			}
		});
		
		nmdetail_btn_back = (Button) findViewById(R.id.nmdetail_btn_back);
		nmdetail_btn_back.setOnClickListener(clickListener);
		nmdetail_btn_call = (Button) findViewById(R.id.nmdetail_btn_call);
		nmdetail_btn_call.setOnClickListener(clickListener);
		nmdetail_btn_selectall = (Button) findViewById(R.id.nmdetail_btn_selectall);
		nmdetail_btn_selectall.setOnClickListener(clickListener);
		nmdetail_btn_add = (Button) findViewById(R.id.nmdetail_btn_add);
		nmdetail_btn_add.setOnClickListener(clickListener);
		
		nmdetail_btn_add.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				menu.setHeaderTitle("添加");
			    menu.add(0, 1, Menu.NONE, "直接拨号");
			    menu.add(0, 2, Menu.NONE, "通讯录选号");
			}
			
		});
		
		
	}
	
	private boolean bool = true;
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.nmdetail_btn_call:
				templist.clear();
				for (int i = 0; i < dataList.size(); i++) {
					if(DetailGridViewAdapter.getIsSelected().get(i)){
						templist.add(dataList.get(i));
					}	
				}
				if(templist.size() != 0) {
					if("".equals(meetid)) {
						//创建会议并呼叫
						createMeeting();
					}else {
						joinMeeting(meetid);
					}
					
				}else{
					Toast.makeText(NMDetailActivity.this, "请选择人员后呼叫", 100).show();
				}
				break;
			case R.id.nmdetail_btn_selectall:
				if(bool) {
					for (int i = 0; i < dataList.size(); i++) {
						DetailGridViewAdapter.getIsSelected().put(i, true);
						adapter.notifyDataSetChanged();
					}
					bool = false;
					nmdetail_btn_selectall.setText("反选");
					
				}else {
					for (int i = 0; i < dataList.size(); i++) {
						DetailGridViewAdapter.getIsSelected().put(i, false);
						adapter.notifyDataSetChanged();
					}
					bool = true;
					nmdetail_btn_selectall.setText("全选");
				}
				
				break;
			case R.id.nmdetail_btn_add:
				 v.showContextMenu();
				break;
			case R.id.nmdetail_btn_back:
				finish();
				break;
			}
		}
		
		private void createMeeting() {
			LandedDate landedDate = new LandedDate(NMDetailActivity.this);
			userName = landedDate.getUserName();
			passWord = landedDate.getPassWord();
			LogUtil.i(this.getClass().getSimpleName(), "开始创建会议了");
			String path = RequestUrl.createMeetingURL.replace("[手机号]", userName)
									.replace("[密码]", passWord)
									.replace("[主叫号码]", userName)
									.replace("[主叫姓名]", userName)
									.replace("[主持人手机号]", userName)
									.replace("[标题]", "1");
			MobileApplication.requestQueue.add(new StringRequest(Method.GET, path.toString(), 
					new Response.Listener<String>() {
	
						@Override
						public void onResponse(String response) {
							LogUtil.i(this.getClass().getSimpleName(), "创建会议返回数据："+response);
							if (response != null) {
								try {
									JSONObject obj = new JSONObject(response);
									int code = (Integer) obj.get("retCode");
									if(code == 0){
										meetid = (String) obj.get("meetid");
										joinMeeting(meetid);
									}else if(code == -1003){
										Toast.makeText(NMDetailActivity.this, "余额不足，创建会议失败", 100).show();
									}else{
										Toast.makeText(NMDetailActivity.this, "创建会议失败：", 100).show();
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
							LogUtil.i(this.getClass().getSimpleName(), "创建会议失败：");
						}
					}));
		}
	};
	private Button nmdetail_btn_back;
	
	
	private void joinMeeting(String meetid) {
		LogUtil.i(this.getClass().getSimpleName(), "开始加入会议了");
		for (int i = 0; i < templist.size(); i++) {
			String phoneNum	 = templist.get(i).getPhoneNum();
			final String name = templist.get(i).getDesplayName();
			String path = RequestUrl.joinMeetingURL.replace("[会议id]", meetid)
					.replace("[被叫号码]", phoneNum)
					.replace("[主叫号码]", userName);
			MobileApplication.requestQueue.add(new StringRequest(Method.GET, path.toString(), 
					new Response.Listener<String>() {

				@Override
				public void onResponse(String response) {
					LogUtil.i(this.getClass().getSimpleName(), "加入会议返回数据："+response);
					if (response != null) {
						try {
							JSONObject obj = new JSONObject(response);
							int code = obj.getInt("retCode");
							if(code == 0) {
								Toast.makeText(NMDetailActivity.this, name+"加入会议成功", 100).show();
							}else {
								Toast.makeText(NMDetailActivity.this, "加入会议失败", 100).show();
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
		}
		
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    // 得到当前被选中的item信息
	    AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
	    
	    switch(item.getItemId()) {
	    case 1:
	        Intent intent1 = new Intent(NMDetailActivity.this, AddBoHaoActivity.class);
	        startActivity(intent1);
//	        finish();
	    	break;
	    case 2:
	    	Intent intent2 = new Intent(NMDetailActivity.this, AddXuanHaoActivity.class);
	        startActivity(intent2);
//	        finish();
	    	break;
	    default:
	        return super.onContextItemSelected(item);
	    }
	    return true;
	}

	
}
