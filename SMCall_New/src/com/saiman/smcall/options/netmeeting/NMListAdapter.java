package com.saiman.smcall.options.netmeeting;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saiman.smcall.R;
import com.saiman.smcall.domain.Group;
public class NMListAdapter extends BaseAdapter{
	List<Group> groupList;
	Context context;
	public NMListAdapter() {
		// TODO Auto-generated constructor stub
	}
	
	public NMListAdapter(Context context, List<Group> groupList) {
		this.context = context;
		this.groupList = groupList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return groupList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return groupList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if(convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.nm_list_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.nm_list_item_name);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.name.setText(groupList.get(position).getGroupName());
		return convertView;
	}

	public class ViewHolder{
		TextView name;
	}
	
}
