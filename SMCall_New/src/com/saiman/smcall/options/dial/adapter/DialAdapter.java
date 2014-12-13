package com.saiman.smcall.options.dial.adapter;

import java.util.List;
import java.util.Map;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.saiman.smcall.R;

public class DialAdapter extends BaseAdapter {

	private List<Map<String, Object>> list = null;
	private Context layout = null;

	public DialAdapter(List<Map<String, Object>> list, Context layout) {
		super();
		this.list = list;
		this.layout = layout;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int p, View v, ViewGroup arg2) {
		if (v == null) {
			v = LayoutInflater.from(layout).inflate(
					R.layout.fill_dial_listview, null);
		}
		ImageView type = (ImageView) v
				.findViewById(R.id.imageViewId_type_fill_dial);
		TextView name = (TextView) v
				.findViewById(R.id.textViewId_name_fill_dial);
		TextView num = (TextView) v
				.findViewById(R.id.textViewId_number_fill_dial);
		TextView date = (TextView) v
				.findViewById(R.id.textViewId_date_fill_dial);
		Map<String, Object> map = list.get(p);
		String number = map.get("number").toString();
		String datee = map.get("date").toString();
		String cachedName = map.get("cachedName").toString();
		String typee = map.get("type").toString();
		name.setText(cachedName);
		num.setText(number);
		date.setText(datee);
		if ("1".equals(typee)) {
			type.setImageResource(R.drawable.ic_call_log_header_missed_call);
		} else if ("2".equals(typee)) {
			type.setImageResource(R.drawable.ic_call_log_header_outgoing_call);
		} else if ("3".equals(typee)) {
			type.setImageResource(R.drawable.ic_call_log_header_incoming_call);
		}
		return v;
	}
}
