package com.vanda.beivandalibnetwork.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.vanda.beivandalibnetwork.activity.WebViewActivity;
import com.vanda.beivandalibnetwork.pojo.InformationIndication;
import com.vanda.beivandalibnetwork.pojo.SS;
import com.vanda.beivandalibnetworkdemo.R;
import com.vanda.vandalibnetwork.arrayadapter.PullLoadArrayAdaper;
import com.vanda.vandalibnetwork.daterequest.RequestManager;
import com.vanda.vandalibnetwork.staticdata.StaticData;
import com.vanda.vandalibnetwork.utils.StringUtils;

public class InformationAdapter extends PullLoadArrayAdaper<SS.List> {

	private Context mContext;
	private ViewHold mViewHold;
	private List<SS.List> mArrayListBrandFilter;
	private SimpleDateFormat sdf;

	// private ArrayList<InformationIndication> mArrayListInformation;

	// private ImagePagerAdapter advAdapter;
	// private AutoScrollViewPager advViewPager;
	// private CirclePageIndicator mIndicator;

	public InformationAdapter(Context context, int resource,
			List<SS.List> objects,
			ArrayList<InformationIndication> mArrayListInformation) {
		super(context, resource, objects);
		mContext = context;
		mArrayListBrandFilter = objects;
		// this.mArrayListInformation = mArrayListInformation;
		sdf = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
		// advAdapter = new ImagePagerAdapter(this.mArrayListInformation,
		// mContext);
	}

	// public void setUpdate() {
	// if (advAdapter != null) {
	// advAdapter.notifyDataSetChanged();
	// }
	// }

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.information_list_item_shot, null);
			mViewHold = new ViewHold(convertView);
			convertView.setTag(mViewHold);
		} else {
			mViewHold = (ViewHold) convertView.getTag();
		}
		if (mViewHold.imageRequest != null) {
			mViewHold.imageRequest.cancelRequest();
		}
		mViewHold.viewpagerLay.setVisibility(View.GONE);
		// if (position == 0) {
		// mViewHold.viewpagerLay.setVisibility(View.VISIBLE);
		// if (advViewPager == null) {
		// advViewPager = (AutoScrollViewPager) convertView
		// .findViewById(R.id.home_fragment_viewpager);
		// // ImagePagerIndicatorAdapter advAdapter = new
		// // ImagePagerIndicatorAdapter(
		// // mContext, mArrayListInformation);
		// // advAdapter.setInfiniteLoop(true);
		// advViewPager.setAdapter(advAdapter);
		// // advViewPager.setCycle(true);
		// // advViewPager.startAutoScroll(6000);
		// advViewPager
		// .setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
		// }
		// // advViewPager.setCurrentItem(Integer.MAX_VALUE / 2
		// // - Integer.MAX_VALUE / 2 % mArrayListInformation.size());
		// if (mIndicator == null) {
		// mIndicator = (CirclePageIndicator) convertView
		// .findViewById(R.id.home_fragment_circlepageindicator);
		// mIndicator.setViewPager(advViewPager);
		// }
		// setUpdate();
		// }
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
		// String date = sdf.format(new Date(1000 * Long
		// .parseLong(mArrayListBrandFilter.get(position).date)));
		mViewHold.mTextViewTime.setVisibility(View.GONE);
		// .setText(mArrayListBrandFilter.get(position).date);
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
		public RelativeLayout viewpagerLay;

		public ViewHold(View convertView) {
			mImageViewProImage = (ImageView) convertView
					.findViewById(R.id.image);
			mTextViewProductName = (ShimmerTextView) convertView
					.findViewById(R.id.textview);
			viewpagerLay = (RelativeLayout) convertView
					.findViewById(R.id.viewpager_relayout);
			LayoutParams params = viewpagerLay.getLayoutParams();
			params.width = StaticData.ScreenWidth;
			params.height = params.width * 14 / 29;
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
				bu.putString("url", mArrayListBrandFilter.get(position).web_url);
				bu.putInt("docId", Integer.parseInt(mArrayListBrandFilter
						.get(position).doc_id));
				Intent it = new Intent(mContext, WebViewActivity.class);
				it.putExtras(bu);
				mContext.startActivity(it);
				break;
			}

		}
	}

}
