package com.vanda.beivandalibnetwork.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vanda.beivandalibnetwork.application.VandaApplication;
import com.vanda.beivandalibnetwork.pojo.Category;
import com.vanda.beivandalibnetworkdemo.R;

public class DrawerAdapter extends BaseAdapter {
	private ListView mListView;

	public DrawerAdapter(ListView listView) {
		mListView = listView;
	}

	@Override
	public int getCount() {
		int count = Category.values().length;
		// if (TextUtils.isEmpty(PreferenceUtils.getPrefString(VandaApplication
		// .getContext().getString(R.string.hello_world), null))) {
		// return count - 2;
		// }
		return count - 1;
	}

	@Override
	public Category getItem(int position) {
		return Category.values()[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(VandaApplication.getContext())
					.inflate(R.layout.listitem_drawer, null);
		}
		TextView textView = (TextView) convertView.findViewById(R.id.textView);
		textView.setText(getItem(position).getDisplayName());
		textView.setSelected(mListView.isItemChecked(position));
		return convertView;
	};
}
