package com.vanda.beivandalibnetwork.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.vanda.beivandalibnetwork.pojo.ImageViewPoJo;
import com.vanda.beivandalibnetworkdemo.R;
import com.vanda.vandalibnetwork.daterequest.RequestManager;
import com.vanda.vandalibnetwork.staticdata.StaticData;

public class ImageViewAdapter extends BaseAdapter {

	private ArrayList<ImageViewPoJo.Data> mArrayList;
	private Context context;

	public ImageViewAdapter(Context context,
			ArrayList<ImageViewPoJo.Data> mArrayList) {
		this.context = context;
		this.mArrayList = mArrayList;
	}

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

	private ViewHold mViewHold;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.listitem_shot, null);
			mViewHold = new ViewHold(convertView);
			convertView.setTag(mViewHold);
		} else {
			mViewHold = (ViewHold) convertView.getTag();
		}
		RequestManager.loadImage(mArrayList.get(position).pic.gq,
				RequestManager.getImageListener(mViewHold.mImageViewProImage,
						StaticData.ScreenWidth,
						RequestManager.mDefaultImageDrawable,
						RequestManager.mDefaultImageDrawable));
		return convertView;
	}

	public class ViewHold {
		public RelativeLayout mRelativeLayoutItem;
		public ImageView mImageViewProImage;
		public ImageView mImageViewShoppingCar;
		public ImageView mImageViewYiZhiKuai;
		public ShimmerTextView mTextViewProductName;
		public TextView mTextViewProductPrice;
		public TextView mTextViewTime;
		public TextView mTextViewProductButPeople;
		public ImageLoader.ImageContainer imageRequest;
		public Shimmer mShimmer;

		public ViewHold(View convertView) {
			mImageViewProImage = (ImageView) convertView
					.findViewById(R.id.image);
			mTextViewProductName = (ShimmerTextView) convertView
					.findViewById(R.id.textview);
			mTextViewTime = (TextView) convertView.findViewById(R.id.time);
			mShimmer = new Shimmer();
		}
	}
}
