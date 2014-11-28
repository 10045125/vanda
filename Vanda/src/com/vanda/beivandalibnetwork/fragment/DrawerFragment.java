package com.vanda.beivandalibnetwork.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vanda.beivandalibnetwork.adapter.DrawerAdapter;
import com.vanda.beivandalibnetwork.pojo.Category;
import com.vanda.beivandalibnetworkdemo.R;

public class DrawerFragment extends Fragment {
	private ListView mListView;

	private DrawerAdapter mAdapter;

	private FragmentInterface mFragmentInterface;

	public interface FragmentInterface {
		public void setCategory(Category mCategory);
	}

	public void setFragmentInterface(FragmentInterface l) {
		mFragmentInterface = l;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_drawer, null);
		mListView = (ListView) contentView.findViewById(R.id.listView);
		mAdapter = new DrawerAdapter(mListView);
		mListView.setAdapter(mAdapter);
		mListView.setItemChecked(0, true);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mListView.setItemChecked(position, true);
				if (mFragmentInterface != null)
					mFragmentInterface.setCategory(Category.values()[position]);
			}
		});
		if (mFragmentInterface != null)
			mFragmentInterface.setCategory(Category.values()[0]);
		return contentView;
	}

	@Override
	public void onResume() {
		super.onResume();
		mAdapter.notifyDataSetChanged();
	}
}
