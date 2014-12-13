package com.saiman.smcall.options.netmeeting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.saiman.smcall.app.MobileApplication;
import com.saiman.smcall.options.contact.FirstLetterUtil;
import com.saiman.smcall.options.contact.PingYinUtil;
import com.saiman.smcall.R;
import com.saiman.smcall.util.LogUtil;
import com.saiman.smcall.view.QuickAlphabeticBar;

public class AddXuanHaoActivity extends Activity{

	private SelectContactAdapter adapter;
	private ListView contactList;
	private List<SelectContactBean> list;
	private AsyncQueryHandler asyncQueryHandler;
	private QuickAlphabeticBar alphabeticBar;
	private Map<Integer, SelectContactBean> contactIdMap = null;
	private View mView;
	EditText editText;
	private List<SelectContactBean> tempList = new ArrayList<SelectContactBean>();
	private String contentContact = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_xuanhao);
		System.out.println("进入了AddXuanHaoActivity");
		
		tempList.clear();
		mView = LayoutInflater.from(this).inflate(R.layout.activity_xuanhao, null);
		contactList = (ListView)findViewById(R.id.contact_list);
		alphabeticBar = (QuickAlphabeticBar)findViewById(R.id.fast_scroller);
		editText = (EditText) findViewById(R.id.xuanhao_editTextId_ContactList);
		
		System.out.println("准备进入了edittext");
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				LogUtil.i(this.getClass().getSimpleName(), "进入了edittext");
				contentContact = editText.getText().toString();
//				asyncQueryHandler = new MyAsyncQueryHandler(MobileApplication.getInstance().getContentResolver());
				init2(contentContact);
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				LogUtil.i(this.getClass().getSimpleName(), "进入了edittext");
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				LogUtil.i(this.getClass().getSimpleName(), "进入了edittext");
			}
		});
		
		asyncQueryHandler = new MyAsyncQueryHandler(MobileApplication.getInstance().getContentResolver());
		init();
		
		Button xuanhao_btn_back = (Button) findViewById(R.id.xuanhao_btn_back);
		xuanhao_btn_back.setOnClickListener(onClickListener);
		Button xuanhao_btn_ok = (Button) findViewById(R.id.xuanhao_btn_ok);
		xuanhao_btn_ok.setOnClickListener(onClickListener);
		
		
	}
	
	private void init2(String inptxt) {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人的Uri
		String[] projection = { ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY }; // 查询的列
		Pattern p = Pattern.compile("[a-zA-Z]");
		Matcher m = p.matcher(inptxt);
		if (m.find()) {
			// System.out.println("进入了字母查询");
			asyncQueryHandler.startQuery(0, null, uri, projection, null, null, "sort_key COLLATE LOCALIZED asc"); // 按照sort_key升序查询
		} else {
			// System.out.println("没有进入字母查询");
			asyncQueryHandler.startQuery(0, null, uri, projection, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like '%" + inptxt + "%' or " + ContactsContract.CommonDataKinds.Phone.NUMBER
					+ " like '%" + inptxt + "%'", null, "sort_key COLLATE LOCALIZED asc"); // 按照sort_key升序查询

		}
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.xuanhao_btn_ok:
				HashMap<Integer,Boolean> isSelected = SelectContactAdapter.getIsSelected();
				System.out.println("选择的数量是："+isSelected.size());
				for (int i = 0; i < isSelected.size(); i++) {
					System.out.println("选择的值是："+isSelected.get(i));
					if(isSelected.get(i)) {
						System.out.println("进来了一次");
						SelectContactBean cb = list.get(i);
						tempList.add(cb);
					}
				}
				Intent intent = new Intent(AddXuanHaoActivity.this, NMDetailActivity.class);
				intent.putExtra("addxuanhaolist", (Serializable)tempList);
				startActivity(intent);
				finish();
				break;
			case R.id.xuanhao_btn_back:
				finish();
				break;
			}
		}
	};
	
	private void init() {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

		String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY };
		asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc");
		
		
		
	}
	
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {

				contactIdMap = new HashMap<Integer, SelectContactBean>();

				list = new ArrayList<SelectContactBean>();

				Pattern p = Pattern.compile("[a-zA-Z]");
				Matcher m = p.matcher(contentContact);
				if (m.find()) {
					
					cursor.moveToFirst();
					for (int i = 0; i < cursor.getCount(); i++) {

						cursor.moveToPosition(i);
						String name = cursor.getString(1);
						String number = cursor.getString(2);
						String sortKey = cursor.getString(3);
						int contactId = cursor.getInt(4);
						Long photoId = cursor.getLong(5);
						String lookUpKey = cursor.getString(6);
						
						FirstLetterUtil.getFirstLetter(name);
                        System.out.println("---contentContact是拼音字母---" + number);

							if (PingYinUtil.getFirstSpell(name).contains(contentContact.toLowerCase())) {
								SelectContactBean cb = new SelectContactBean();

								cb.setDesplayName(name);
								if (number.contains(" ")) {
									number = number.replace(" ", "");
								}
							
								if (number.startsWith("+86")) {// 去除多余的中国地区号码标志，对这个程序没有影响。
									cb.setPhoneNum(number.substring(3));
								} else {
									cb.setPhoneNum(number);
								}
								cb.setSortKey(sortKey);
								cb.setContactId(contactId);
								cb.setPhotoId(photoId);
								cb.setLookUpKey(lookUpKey);
								list.add(cb);

								contactIdMap.put(contactId, cb);

							}
					}
				} else {
					cursor.moveToFirst();
					for (int i = 0; i < cursor.getCount(); i++) {

						cursor.moveToPosition(i);
						String name = cursor.getString(1);
						String number = cursor.getString(2);
						String sortKey = cursor.getString(3);
						int contactId = cursor.getInt(4);
						Long photoId = cursor.getLong(5);
						String lookUpKey = cursor.getString(6);
						System.out.println("---contentContact不是拼音---" + number);

						SelectContactBean cb = new SelectContactBean();
							cb.setDesplayName(name);
							if (number.contains(" ")) {
								number = number.replace(" ", "");
							}
							if (number.startsWith("+86")) {// 去除多余的中国地区号码标志，对这个程序没有影响。
								cb.setPhoneNum(number.substring(3));
							} else {
								cb.setPhoneNum(number);
							}
							cb.setSortKey(sortKey);
							cb.setContactId(contactId);
							cb.setPhotoId(photoId);
							cb.setLookUpKey(lookUpKey);
							list.add(cb);

							contactIdMap.put(contactId, cb);

					}
				}
				if (list.size() > 0) {
					setAdapter(list);
				}
				cursor.close();
			}
			
			super.onQueryComplete(token, cookie, cursor);
		}

	}
	private void setAdapter(List<SelectContactBean> list) {
		adapter = new SelectContactAdapter(AddXuanHaoActivity.this, list, alphabeticBar);
		contactList.setAdapter(adapter);
		alphabeticBar.init(mView);
		alphabeticBar.setListView(contactList);
		alphabeticBar.setHight(alphabeticBar.getHeight());
		alphabeticBar.setVisibility(View.VISIBLE);
		
		contactList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				SelectContactAdapter.ViewHolder holder = (SelectContactAdapter.ViewHolder) arg1.getTag();
				holder.select.toggle();
				if(holder.select.isChecked()) {
					SelectContactAdapter.getIsSelected().put(position, true);
				}
				
			}
		});
	}
}
