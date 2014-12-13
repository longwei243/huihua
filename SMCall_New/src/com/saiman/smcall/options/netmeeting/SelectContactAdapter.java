package com.saiman.smcall.options.netmeeting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.saiman.smcall.R;
import com.saiman.smcall.view.QuickAlphabeticBar;

public class SelectContactAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<SelectContactBean> list;
	private HashMap<String, Integer> alphaIndexer;
	private String[] sections;
	private Context ctx;
	
	private static HashMap<Integer,Boolean> isSelected;

	public SelectContactAdapter(Context context, List<SelectContactBean> list,
			QuickAlphabeticBar alpha) {
		
		isSelected = new HashMap<Integer, Boolean>();
		
		this.ctx = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.alphaIndexer = new HashMap<String, Integer>();
		this.sections = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {

			String name = getAlpha(list.get(i).getSortKey());
			if (!alphaIndexer.containsKey(name)) {
				alphaIndexer.put(name, i);
			}
		}

		Set<String> sectionLetters = alphaIndexer.keySet();
		ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
		Collections.sort(sectionList);
		sections = new String[sectionList.size()];
		sectionList.toArray(sections);

		alpha.setAlphaIndexer(alphaIndexer);

		
		for(int i=0; i<list.size();i++) {
            getIsSelected().put(i,false);
        }
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void remove(int position) {
		list.remove(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		WindowManager wm = (WindowManager) ctx
				.getSystemService(Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();

		int height = wm.getDefaultDisplay().getHeight();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.select_contact, null);
			holder = new ViewHolder();
			holder.quickContactBadge = (ImageView) convertView
					.findViewById(R.id.qcb);
			holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.number = (TextView) convertView.findViewById(R.id.number);
			holder.select = (CheckBox) convertView.findViewById(R.id.select);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		android.view.ViewGroup.LayoutParams params = holder.quickContactBadge
				.getLayoutParams();
		params.height = height / 13;
		holder.quickContactBadge.setLayoutParams(params);
		SelectContactBean contact = list.get(position);
		String name = contact.getDesplayName();
		String number = contact.getPhoneNum();
		holder.name.setText(name);
		holder.number.setText(number);
		String currentStr = getAlpha(contact.getSortKey());

		String previewStr = (position - 1) >= 0 ? getAlpha(list.get(
				position - 1).getSortKey()) : " ";

		if (!previewStr.equals(currentStr)) {
			holder.alpha.setVisibility(View.VISIBLE);
			holder.alpha.setText(currentStr);
		} else {
			holder.alpha.setVisibility(View.GONE);
		}
		
		holder.select.setChecked(getIsSelected().get(position));

		return convertView;
	}

	public class ViewHolder {
		ImageView quickContactBadge;
		TextView alpha;
		TextView name;
		TextView number;
		CheckBox select;
	}

	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);

		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}
	
	public static HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer,Boolean> isSelected) {
        SelectContactAdapter.isSelected = isSelected;
    }

}
