package com.saiman.smcall.options.dial;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class CallPhoneAsyncTask extends AsyncTask<String, Void, String> {

	private Handler handler = null;

	public CallPhoneAsyncTask(Handler handler) {
		super();
		this.handler = handler;
	}

	@Override
	protected String doInBackground(String... arg0) {
		try {
			URL url = new URL(arg0[0]);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-type",
					"application/x-www-form-urlencoded");
			conn.setDoInput(true);
			conn.connect();
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int len = 0;
				byte[] b = new byte[1024];
				while ((len = is.read(b)) != -1) {
					baos.write(b, 0, len);
					baos.flush();
				}
				byte[] byteArray = baos.toByteArray();
				String s = new String(byteArray);
				is.close();
				baos.close();
				return s;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (result != null) {
			Message message = handler.obtainMessage();
			message.obj = result;
			message.what = 69;
			handler.sendMessage(message);
			Thread.interrupted();
		}
	}
}
