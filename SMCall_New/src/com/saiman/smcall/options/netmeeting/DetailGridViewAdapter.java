package com.saiman.smcall.options.netmeeting;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.saiman.smcall.R;

public class DetailGridViewAdapter extends BaseAdapter{

	private Context context;
	private List<SelectContactBean> list;
	
	private static HashMap<Integer,Boolean> isSelected;
	
	public DetailGridViewAdapter(Context context, List<SelectContactBean> list) {
		this.context = context;
		this.list = list;
		
		isSelected = new HashMap<Integer, Boolean>();
		for(int i=0; i<list.size();i++) {
            getIsSelected().put(i,false);
        }
	}
	
	public static HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer,Boolean> isSelected) {
    	DetailGridViewAdapter.isSelected = isSelected;
    }
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.detail_gridview_item, null);
			holder = new ViewHolder();
			
			holder.name = (TextView) convertView.findViewById(R.id.gridview_item_name);
			holder.number = (TextView) convertView.findViewById(R.id.gridview_item_number);
			holder.select = (CheckBox) convertView.findViewById(R.id.gridview_item_select);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		SelectContactBean contact = list.get(position);
		String name = contact.getDesplayName();
		String number = contact.getPhoneNum();
		holder.name.setText(name);
		holder.number.setText(number);
		holder.select.setChecked(getIsSelected().get(position));
		return convertView;
	}
	
	public class ViewHolder {
		
		TextView name;
		TextView number;
		CheckBox select;
	}

}
