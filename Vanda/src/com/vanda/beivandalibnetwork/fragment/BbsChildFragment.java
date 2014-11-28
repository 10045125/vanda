package com.vanda.beivandalibnetwork.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.vanda.beivandalibnetwork.pojo.Bbs;
import com.vanda.beivandalibnetworkdemo.R;

public class BbsChildFragment extends SherlockFragment {

	private int id;
	private ListView mListView;
	private ArrayList<Bbs.ItemData> mArrayList;

	public static BbsChildFragment newInstance(int flag,
			ArrayList<Bbs.ItemData> mArrayList) {
		BbsChildFragment mBbsChildFragment = new BbsChildFragment();
		mBbsChildFragment.id = flag;
		mBbsChildFragment.mArrayList = mArrayList;
		return mBbsChildFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.bbs_listview, null);
		mListView = (ListView) view.findViewById(R.id.bbs_listview);
		mListView.setAdapter(new MyAdapter());
		return mListView;
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mArrayList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getSherlockActivity())
						.inflate(R.layout.my_item_relative, null);
			}

			TextView mTextView = (TextView) convertView
					.findViewById(R.id.my_item_relative_tv);
			mTextView.setText(mArrayList.get(position).title);

			return convertView;
		}
	}

}
