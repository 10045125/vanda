package com.vanda.beivandalibnetwork.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.vanda.beivandalibnetwork.activity.ImageViewFragmentActivity;
import com.vanda.beivandalibnetwork.pojo.SS;
import com.vanda.beivandalibnetworkdemo.R;
import com.vanda.vandalibnetwork.arrayadapter.PullLoadArrayAdaper;
import com.vanda.vandalibnetwork.daterequest.RequestManager;
import com.vanda.vandalibnetwork.staticdata.StaticData;
import com.vanda.vandalibnetwork.utils.StringUtils;

public class SSAdapter extends PullLoadArrayAdaper<SS.List> {

	private Context mContext;
	private ViewHold mViewHold;
	private List<SS.List> mArrayListBrandFilter;
	private SimpleDateFormat sdf;

	public SSAdapter(Context context, int resource, List<SS.List> objects) {
		super(context, resource, objects);
		mContext = context;
		mArrayListBrandFilter = objects;
		sdf = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.listitem_shot, null);
			mViewHold = new ViewHold(convertView);
			convertView.setTag(mViewHold);
		} else {
			mViewHold = (ViewHold) convertView.getTag();
		}
		if (mViewHold.imageRequest != null) {
			mViewHold.imageRequest.cancelRequest();
		}
		mViewHold.mTextViewProductName.setText(StringUtils
				.stringFilter(StringUtils.ToDBC(mArrayListBrandFilter
						.get(position).title)));
		if (mViewHold.mShimmer.isAnimating()) {
			mViewHold.mShimmer.cancel();
		}
		mViewHold.mShimmer.start(mViewHold.mTextViewProductName);
		mViewHold.imageRequest = RequestManager.loadImage(mArrayListBrandFilter
				.get(position).pic_url, RequestManager.getImageListener(
				mViewHold.mImageViewProImage, StaticData.ScreenWidth,
				RequestManager.mDefaultImageDrawable,
				RequestManager.mDefaultImageDrawable));
		String date = sdf.format(new Date(1000 * Long
				.parseLong(mArrayListBrandFilter.get(position).date)));
		mViewHold.mTextViewTime.setText(date);
		mViewHold.mImageViewProImage.setOnClickListener(new MyOnClickListener(
				mViewHold, position));
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

	private class MyOnClickListener implements View.OnClickListener {
		private ViewHold mViewHold;
		private int position;

		public MyOnClickListener(ViewHold mViewHold, int position) {
			this.mViewHold = mViewHold;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.image:
				Bundle bu = new Bundle();
				bu.putString("url",
						mArrayListBrandFilter.get(position).detail_url);
				bu.putString("title", mArrayListBrandFilter.get(position).title);
				Intent it = new Intent(mContext,
						ImageViewFragmentActivity.class);
				it.putExtras(bu);
				mContext.startActivity(it);
				break;
			}

		}
	}

}
