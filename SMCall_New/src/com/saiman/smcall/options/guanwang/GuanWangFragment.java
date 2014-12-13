package com.saiman.smcall.options.guanwang;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.saiman.smcall.R;
import com.saiman.smcall.app.MobileApplication;
import com.saiman.smcall.request.RequestUrl;

public class GuanWangFragment extends Fragment{

	private WebView mWebView;

	private String Now_Url;
	private String Now_Url_def;
	private TextView titles;
	private ImageButton ibt_back, ibt_sx;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_guanwang, null);
		init(view);
		return view;
	}
	private void init(View view) {
		titles = (TextView) view.findViewById(R.id.topbar_title);
		titles.setText(R.string.main_weiguanwang);
		ibt_back = (ImageButton) view.findViewById(R.id.ImageBtn_back);
		ibt_sx = (ImageButton) view.findViewById(R.id.ImageBtn_SX);
		ibt_back.setOnClickListener(BtnClick);
		ibt_sx.setOnClickListener(BtnClick);
		
		mWebView = (WebView) view.findViewById(R.id.webView1);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);

		String path = RequestUrl.pathGuanwang;
		Now_Url = "" + path;
		Now_Url_def = "" + path;
		
		mWebView.setWebChromeClient(new ChromeClient());
		mWebView.setWebViewClient(new WebViewClient() {
			private Dialog progressDialog;

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Now_Url = url;
				view.loadUrl(url);

				return true;
			}

			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
//				progressDialog = ProgressDialog.show(getActivity(), null, "正在加载数据，请稍后……");
//				progressDialog.setCancelable(true);
			}

			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
//				progressDialog.cancel();

			}
		});
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		mWebView.loadUrl(Now_Url);
		
	}
	
	private View.OnClickListener BtnClick = new View.OnClickListener() {

		public void onClick(View v) {

			if (v.getId() == ibt_back.getId()) {
				mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
				if (mWebView.canGoBack()) {
					mWebView.goBack();
				}
//				mWebView.loadUrl(Now_Url_def);
			} else if (v.getId() == ibt_sx.getId()) {
//				mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//				mWebView.loadUrl(Now_Url);
				showShare();
			}

		}
	};
	/**
	 * 一键分享的方法
	 */
	private void showShare() {
		final OnekeyShare oks = new OnekeyShare();
		oks.setNotification(R.drawable.ic_launcher, MobileApplication.getInstance().getString(R.string.app_name));
		//这个是最重要的，设置需要分享的文本内容
		oks.setText("微官网");
		oks.setUrl(Now_Url_def);
		//设置是否直接分享，false为不直接分享，推荐
		oks.setSilent(false);
		
		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();
		
		oks.setImagePath("");
		oks.setImageUrl("");

		oks.show(MobileApplication.getInstance());
	}
	class ChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {

			// 动态在标题栏显示进度条

			// HomeWebActivity.this.setProgress(newProgress*100);

			super.onProgressChanged(view, newProgress);

		}

		public void onReceivedTitle(WebView view, String title) {

			// 设置当前activity的标题栏
			// TextView titles = (TextView) findViewById(R.id.topbar_title) ;
			// titles.setText(title);
			super.onReceivedTitle(view, title);

		}

	}

}
