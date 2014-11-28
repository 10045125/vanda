package com.vanda.beivandalibnetwork.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vanda.beivandalibnetwork.activity.ImageViewFragmentActivity;
import com.vanda.beivandalibnetwork.fragment.PhotoImageViewFragment;
import com.vanda.beivandalibnetwork.pojo.ImageViewPoJo;

public class PhotoViewPageAdapter extends FragmentStatePagerAdapter {

	private ArrayList<ImageViewPoJo.Data> mArrayList;
	private Context context;

	public PhotoViewPageAdapter(Context context, FragmentManager fm,
			ArrayList<ImageViewPoJo.Data> mArrayList) {
		super(fm);
		this.mArrayList = mArrayList;
		this.context = context;
	}

	@Override
	public Fragment getItem(int arg0) {
		PhotoImageViewFragment mPhotoImageViewFragment = PhotoImageViewFragment
				.newInstance(mArrayList.get(arg0).pic.gq);
		mPhotoImageViewFragment
				.setOnPhotoViewClickListener((ImageViewFragmentActivity) context);
		return mPhotoImageViewFragment;
	}

	@Override
	public int getCount() {
		return mArrayList.size();
	}
	// @Override
	// public Object instantiateItem(View collection, int position) {
	// final LayoutInflater inflater = getLayoutInflater();
	// View layout = inflater.inflate(R.layout.my_layout, null);
	// final ImageView image =
	// (ImageView)layout.findViewById(R.id.image_display);
	// final int cPos = position;
	// image.setOnClickListener(new OnClickListener()
	// {
	// @Override
	// public void onClick(View v)
	// {
	// ImageView i = (ImageView)v;
	// if(cPos == 0)
	// {
	// //...
	// }
	// //...
	//
	// }
	// });
	//
	// return layout;
	// }

}
