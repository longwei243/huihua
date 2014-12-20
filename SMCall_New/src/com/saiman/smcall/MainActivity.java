package com.saiman.smcall;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.saiman.smcall.app.MobileApplication;
import com.saiman.smcall.domain.LandedDate;
import com.saiman.smcall.event.DialEvent;
import com.saiman.smcall.options.contact.ContactFragment;
import com.saiman.smcall.options.dial.DialFragment;
import com.saiman.smcall.options.guanwang.GuanWangFragment;
import com.saiman.smcall.options.im.IMFragment;
import com.saiman.smcall.options.login.LoginActivity;
import com.saiman.smcall.options.shop.ShopFragment;
import com.saiman.smcall.request.RequestUrl;
import com.saiman.smcall.R;
import com.saiman.smcall.util.LogUtil;
import com.saiman.smcall.view.ChangeColorIconWithTextView;

import de.greenrobot.event.EventBus;

public class MainActivity extends FragmentActivity implements OnPageChangeListener, OnClickListener{

	private ViewPager mViewPager;
	private List<Fragment> mTabs = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;

	private List<ChangeColorIconWithTextView> mTabIndicator = new ArrayList<ChangeColorIconWithTextView>();
	private Fragment fragment_dial;
	private Fragment fragment_contact;
	private Fragment fragment_guanwang;
	private Fragment fragment_shop;
	private Fragment fragment_im;
	
	SharedPreferences MyPreferences;
	SharedPreferences.Editor editor;
	
	private int userId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyPreferences = getSharedPreferences(MobileApplication.getInstance().getResources().getString(R.string.app_name), Activity.MODE_PRIVATE + +Activity.MODE_WORLD_READABLE + Activity.MODE_WORLD_WRITEABLE);
		editor = MyPreferences.edit();
		EventBus.getDefault().register(this);
		
		setContentView(R.layout.activity_main);

		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

		initDatas();

		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		
		
	}

	private void initDatas() {
		fragment_dial = new DialFragment();
		mTabs.add(fragment_dial);
		
		fragment_contact = new ContactFragment();
		mTabs.add(fragment_contact);
		
		fragment_guanwang = new GuanWangFragment();
		mTabs.add(fragment_guanwang);
		
		fragment_shop = new ShopFragment();
		mTabs.add(fragment_shop);
		
		fragment_im = new IMFragment();
		mTabs.add(fragment_im);
		
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
		{

			@Override
			public int getCount()
			{
				return mTabs.size();
			}

			@Override
			public Fragment getItem(int arg0)
			{
				return mTabs.get(arg0);
			}
		};

		initTabIndicator();

	}

	private void initTabIndicator()
	{
		ChangeColorIconWithTextView one = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_one);
		ChangeColorIconWithTextView two = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_two);
		ChangeColorIconWithTextView three = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_three);
		ChangeColorIconWithTextView four = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_four);
		ChangeColorIconWithTextView five = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_five);

		mTabIndicator.add(one);
		mTabIndicator.add(two);
		mTabIndicator.add(three);
		mTabIndicator.add(four);
		mTabIndicator.add(five);

		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);
		five.setOnClickListener(this);

		one.setIconAlpha(1.0f);
	}

	@Override
	public void onPageSelected(int arg0)
	{
		if(arg0 != 0) {
			
			editor.putString("User_t1", "移动");
			editor.commit();
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels)
	{
		if (positionOffset > 0)
		{
			ChangeColorIconWithTextView left = mTabIndicator.get(position);
			ChangeColorIconWithTextView right = mTabIndicator.get(position + 1);

			left.setIconAlpha(1 - positionOffset);
			right.setIconAlpha(positionOffset);
		}

	}

	@Override
	public void onPageScrollStateChanged(int state)
	{

	}

	@Override
	public void onClick(View v)
	{
		resetOtherTabs();

		switch (v.getId())
		{
		case R.id.id_indicator_one:
			SharedPreferences settings = getSharedPreferences(MobileApplication.getInstance().getResources().getString(R.string.app_name), Activity.MODE_PRIVATE + Activity.MODE_WORLD_READABLE + Activity.MODE_WORLD_WRITEABLE);
			final String dia_key = settings.getString("User_dkey", "").toString().trim().toLowerCase() + "";
			final String dia_t1 = settings.getString("User_t1", "").toString().trim().toLowerCase() + "";

			if (dia_t1.equals("移动")) {
				mTabIndicator.get(0).setIconAlpha(1.0f);
				mViewPager.setCurrentItem(0, false);

				editor.putString("User_t1", "当前");
				editor.commit();
			} else {
				if (dia_key.equals("") | dia_key.equals("显示")) {
					editor.putString("User_dkey", "隐藏");
					editor.commit();
					mTabIndicator.get(0).setIcon(R.drawable.tab_dial_down_n);
					mTabIndicator.get(0).setIconAlpha(1.0f);
				} else {
					editor.putString("User_dkey", "显示");
					editor.commit();
					mTabIndicator.get(0).setIcon(R.drawable.tab_dial_n);
					mTabIndicator.get(0).setIconAlpha(1.0f);
				}
			}
			EventBus.getDefault().post(new DialEvent());
			break;
		case R.id.id_indicator_two:
			mTabIndicator.get(1).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(1, false);
			
			editor.putString("User_t1", "移动");
			editor.commit();
			break;
		case R.id.id_indicator_three:
			mTabIndicator.get(2).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(2, false);
			editor.putString("User_t1", "移动");
			editor.commit();
			break;
		case R.id.id_indicator_four:
			mTabIndicator.get(3).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(3, false);
			editor.putString("User_t1", "移动");
			editor.commit();
			break;
		case R.id.id_indicator_five:
			mTabIndicator.get(4).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(4, false);
			editor.putString("User_t1", "移动");
			editor.commit();
			break;

		}

	}

	private void resetOtherTabs()
	{
		for (int i = 0; i < mTabIndicator.size(); i++)
		{
			mTabIndicator.get(i).setIconAlpha(0);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
	public void onEventMainThread(Integer i) {
		
	}
}
