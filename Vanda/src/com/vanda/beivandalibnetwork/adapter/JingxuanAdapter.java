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
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.vanda.beivandalibnetwork.activity.WebViewActivity;
import com.vanda.beivandalibnetwork.pojo.InformationIndication;
import com.vanda.beivandalibnetwork.pojo.JingXuan;
import com.vanda.beivandalibnetworkdemo.R;
import com.vanda.vandalibnetwork.arrayadapter.PullLoadArrayAdaper;
import com.vanda.vandalibnetwork.daterequest.RequestManager;
import com.vanda.vandalibnetwork.staticdata.StaticData;
import com.vanda.vandalibnetwork.utils.StringUtils;

public class JingxuanAdapter extends PullLoadArrayAdaper<JingXuan> {

	private Context mContext;
	private ViewHold mViewHold;
	private List<JingXuan> mArrayListBrandFilter;
	private SimpleDateFormat sdf;
	private ArrayList<InformationIndication> mArrayListInformation;

	// private ImagePagerAdapter advAdapter;
	// private AutoScrollViewPager advViewPager;
	// private CirclePageIndicator mIndicator;

	public JingxuanAdapter(Context context, int resource,
			List<JingXuan> objects,
			ArrayList<InformationIndication> mArrayListInformation) {
		super(context, resource, objects);
		mContext = context;
		mArrayListBrandFilter = objects;
		sdf = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
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
					R.layout.jingxuan_item, null);
			mViewHold = new ViewHold(convertView);
			convertView.setTag(mViewHold);
		} else {
			mViewHold = (ViewHold) convertView.getTag();
		}
		if (mViewHold.imageRequest != null) {
			mViewHold.imageRequest.cancelRequest();
		}
		mViewHold.viewpagerLay.setVisibility(View.GONE);
		mViewHold.mTextViewProductTitle_01.setText(StringUtils
				.stringFilter(StringUtils.ToDBC(mArrayListBrandFilter
						.get(position).mArrayListData_.get(0).title)));
		mViewHold.mTextViewProductTitle_02.setText(StringUtils
				.stringFilter(StringUtils.ToDBC(mArrayListBrandFilter
						.get(position).mArrayListData_.get(1).title)));
		mViewHold.time_01.setVisibility(View.GONE);
		mViewHold.time_02.setVisibility(View.GONE);
		RequestManager
				.loadImage(mArrayListBrandFilter.get(position).mArrayListData_
						.get(0).pic_url, RequestManager.getImageListener(
						mViewHold.mImageViewProImage_01, 0,
						RequestManager.mDefaultImageDrawable,
						RequestManager.mDefaultImageDrawable));
		RequestManager
				.loadImage(mArrayListBrandFilter.get(position).mArrayListData_
						.get(1).pic_url, RequestManager.getImageListener(
						mViewHold.mImageViewProImage_02, 0,
						RequestManager.mDefaultImageDrawable,
						RequestManager.mDefaultImageDrawable));

		mViewHold.mImageViewProImage_01
				.setOnClickListener(new MyOnClickListener(mViewHold, 1,
						position));
		mViewHold.mImageViewProImage_02
				.setOnClickListener(new MyOnClickListener(mViewHold, 2,
						position));

		mViewHold.mTextViewItemTitle_01.setText(StringUtils
				.stringFilter(StringUtils.ToDBC(mArrayListBrandFilter
						.get(position).mArrayListData.get(0).title)));
		mViewHold.mTextViewItemTitle_02.setText(StringUtils
				.stringFilter(StringUtils.ToDBC(mArrayListBrandFilter
						.get(position).mArrayListData.get(1).title)));
		mViewHold.mTextViewItemTitle_03.setText(StringUtils
				.stringFilter(StringUtils.ToDBC(mArrayListBrandFilter
						.get(position).mArrayListData.get(2).title)));
		mViewHold.mTextViewItemTitle_04.setText(StringUtils
				.stringFilter(StringUtils.ToDBC(mArrayListBrandFilter
						.get(position).mArrayListData.get(3).title)));

		mViewHold.mTextViewItemTime_01.setText(mArrayListBrandFilter
				.get(position).mArrayListData.get(0).date);
		mViewHold.mTextViewItemTime_02.setText(mArrayListBrandFilter
				.get(position).mArrayListData.get(1).date);
		mViewHold.mTextViewItemTime_03.setText(mArrayListBrandFilter
				.get(position).mArrayListData.get(2).date);
		mViewHold.mTextViewItemTime_04.setText(mArrayListBrandFilter
				.get(position).mArrayListData.get(3).date);

		mViewHold.mTextViewItemAuthor_01.setText(mArrayListBrandFilter
				.get(position).mArrayListData.get(0).author);
		mViewHold.mTextViewItemAuthor_02.setText(mArrayListBrandFilter
				.get(position).mArrayListData.get(1).author);
		mViewHold.mTextViewItemAuthor_03.setText(mArrayListBrandFilter
				.get(position).mArrayListData.get(2).author);
		mViewHold.mTextViewItemAuthor_04.setText(mArrayListBrandFilter
				.get(position).mArrayListData.get(3).author);

		RequestManager
				.loadImage(mArrayListBrandFilter.get(position).mArrayListData
						.get(0).pic_url, RequestManager.getImageListener(
						mViewHold.mImageViewItem_01,
						StaticData.ScreenWidth / 3,
						RequestManager.mDefaultImageDrawable,
						RequestManager.mDefaultImageDrawable));
		RequestManager
				.loadImage(mArrayListBrandFilter.get(position).mArrayListData
						.get(1).pic_url, RequestManager.getImageListener(
						mViewHold.mImageViewItem_02,
						StaticData.ScreenWidth / 3,
						RequestManager.mDefaultImageDrawable,
						RequestManager.mDefaultImageDrawable));
		RequestManager
				.loadImage(mArrayListBrandFilter.get(position).mArrayListData
						.get(2).pic_url, RequestManager.getImageListener(
						mViewHold.mImageViewItem_03,
						StaticData.ScreenWidth / 3,
						RequestManager.mDefaultImageDrawable,
						RequestManager.mDefaultImageDrawable));
		RequestManager
				.loadImage(mArrayListBrandFilter.get(position).mArrayListData
						.get(3).pic_url, RequestManager.getImageListener(
						mViewHold.mImageViewItem_04,
						StaticData.ScreenWidth / 3,
						RequestManager.mDefaultImageDrawable,
						RequestManager.mDefaultImageDrawable));

		mViewHold.mRelativeLayoutItem_01
				.setOnClickListener(new MyOnClickListener(mViewHold, 1,
						position));
		mViewHold.mRelativeLayoutItem_02
				.setOnClickListener(new MyOnClickListener(mViewHold, 2,
						position));
		mViewHold.mRelativeLayoutItem_03
				.setOnClickListener(new MyOnClickListener(mViewHold, 3,
						position));
		mViewHold.mRelativeLayoutItem_04
				.setOnClickListener(new MyOnClickListener(mViewHold, 4,
						position));

		return convertView;
	}

	public class ViewHold {
		public RelativeLayout mRelativeLayoutItem_01;
		public RelativeLayout mRelativeLayoutItem_02;
		public RelativeLayout mRelativeLayoutItem_03;
		public RelativeLayout mRelativeLayoutItem_04;
		public ImageView mImageViewProImage_01, mImageViewProImage_02;
		public ShimmerTextView mTextViewProductTitle_01,
				mTextViewProductTitle_02;
		public TextView time_01, time_02;
		public ImageView mImageViewItem_01, mImageViewItem_02,
				mImageViewItem_03, mImageViewItem_04;
		public TextView mTextViewItemTitle_01, mTextViewItemTitle_02,
				mTextViewItemTitle_03, mTextViewItemTitle_04;
		public TextView mTextViewItemTime_01, mTextViewItemTime_02,
				mTextViewItemTime_03, mTextViewItemTime_04;
		public TextView mTextViewItemAuthor_01, mTextViewItemAuthor_02,
				mTextViewItemAuthor_03, mTextViewItemAuthor_04;
		public ImageLoader.ImageContainer imageRequest;
		public Shimmer mShimmer;
		public RelativeLayout viewpagerLay;

		public ViewHold(View convertView) {
			viewpagerLay = (RelativeLayout) convertView
					.findViewById(R.id.viewpager_relayout);
			LayoutParams params = viewpagerLay.getLayoutParams();
			params.width = StaticData.ScreenWidth;
			params.height = params.width * 14 / 29;
			mShimmer = new Shimmer();
			mImageViewProImage_01 = (ImageView) convertView.findViewById(
					R.id.layout_listitem_shot_01).findViewById(R.id.image);
			mImageViewProImage_02 = (ImageView) convertView.findViewById(
					R.id.layout_listitem_shot_02).findViewById(R.id.image);

			LayoutParams params_1 = mImageViewProImage_01.getLayoutParams();
			params_1.width = StaticData.ScreenWidth / 2;
			params_1.height = params_1.width;

			LayoutParams params_2 = mImageViewProImage_02.getLayoutParams();
			params_2.width = StaticData.ScreenWidth / 2;
			params_2.height = params_2.width;

			mTextViewProductTitle_01 = (ShimmerTextView) convertView
					.findViewById(R.id.layout_listitem_shot_01).findViewById(
							R.id.textview);
			mTextViewProductTitle_02 = (ShimmerTextView) convertView
					.findViewById(R.id.layout_listitem_shot_02).findViewById(
							R.id.textview);
			time_01 = (TextView) convertView.findViewById(
					R.id.layout_listitem_shot_01).findViewById(R.id.time);
			time_02 = (TextView) convertView.findViewById(
					R.id.layout_listitem_shot_02).findViewById(R.id.time);
			mImageViewItem_01 = (ImageView) convertView.findViewById(
					R.id.layout_jingxuan_child_item_01).findViewById(
					R.id.image_jingxuan);
			mImageViewItem_02 = (ImageView) convertView.findViewById(
					R.id.layout_jingxuan_child_item_02).findViewById(
					R.id.image_jingxuan);
			mImageViewItem_03 = (ImageView) convertView.findViewById(
					R.id.layout_jingxuan_child_item_03).findViewById(
					R.id.image_jingxuan);
			mImageViewItem_04 = (ImageView) convertView.findViewById(
					R.id.layout_jingxuan_child_item_04).findViewById(
					R.id.image_jingxuan);
			mTextViewItemTitle_01 = (TextView) convertView.findViewById(
					R.id.layout_jingxuan_child_item_01).findViewById(
					R.id.product_name);
			mTextViewItemTitle_02 = (TextView) convertView.findViewById(
					R.id.layout_jingxuan_child_item_02).findViewById(
					R.id.product_name);
			mTextViewItemTitle_03 = (TextView) convertView.findViewById(
					R.id.layout_jingxuan_child_item_03).findViewById(
					R.id.product_name);
			mTextViewItemTitle_04 = (TextView) convertView.findViewById(
					R.id.layout_jingxuan_child_item_04).findViewById(
					R.id.product_name);

			mTextViewItemTime_01 = (TextView) convertView.findViewById(
					R.id.layout_jingxuan_child_item_01).findViewById(R.id.time);
			mTextViewItemTime_02 = (TextView) convertView.findViewById(
					R.id.layout_jingxuan_child_item_02).findViewById(R.id.time);
			mTextViewItemTime_03 = (TextView) convertView.findViewById(
					R.id.layout_jingxuan_child_item_03).findViewById(R.id.time);
			mTextViewItemTime_04 = (TextView) convertView.findViewById(
					R.id.layout_jingxuan_child_item_04).findViewById(R.id.time);

			mTextViewItemAuthor_01 = (TextView) convertView.findViewById(
					R.id.layout_jingxuan_child_item_01).findViewById(R.id.name);
			mTextViewItemAuthor_02 = (TextView) convertView.findViewById(
					R.id.layout_jingxuan_child_item_02).findViewById(R.id.name);
			mTextViewItemAuthor_03 = (TextView) convertView.findViewById(
					R.id.layout_jingxuan_child_item_03).findViewById(R.id.name);
			mTextViewItemAuthor_04 = (TextView) convertView.findViewById(
					R.id.layout_jingxuan_child_item_04).findViewById(R.id.name);
			mRelativeLayoutItem_01 = (RelativeLayout) convertView
					.findViewById(R.id.layout_jingxuan_child_item_01);/*
																	 * .findViewById
																	 * ( R.id.
																	 * rl_listitem_shot
																	 * );
																	 */
			mRelativeLayoutItem_02 = (RelativeLayout) convertView
					.findViewById(R.id.layout_jingxuan_child_item_02);
			mRelativeLayoutItem_03 = (RelativeLayout) convertView
					.findViewById(R.id.layout_jingxuan_child_item_03);
			mRelativeLayoutItem_04 = (RelativeLayout) convertView
					.findViewById(R.id.layout_jingxuan_child_item_04);
		}
	}

	private class MyOnClickListener implements View.OnClickListener {
		@SuppressWarnings("unused")
		private ViewHold mViewHold;
		private int position;
		private int id;

		public MyOnClickListener(ViewHold mViewHold, int id, int position) {
			this.mViewHold = mViewHold;
			this.position = position;
			this.id = id;
		}

		@Override
		public void onClick(View v) {
			Bundle bu = new Bundle();
			Intent it = new Intent(mContext, WebViewActivity.class);
			// if (mArrayListBrandFilter.get(position).mArrayListData_.get(id -
			// 1).doc_id == null
			// || "".equals(mArrayListBrandFilter.get(position).mArrayListData_
			// .get(id - 1).doc_id)) {
			// Toast.makeText(mContext, "文章介绍不存在！", Toast.LENGTH_SHORT).show();
			// return;
			// }
			switch (v.getId()) {
			case R.id.image:

				bu.putString("url",
						mArrayListBrandFilter.get(position).mArrayListData_
								.get(id - 1).web_url);
				bu.putInt(
						"docId",
						Integer.parseInt(mArrayListBrandFilter.get(position).mArrayListData_
								.get(id - 1).doc_id != "" ? mArrayListBrandFilter
								.get(position).mArrayListData_.get(id - 1).doc_id
								: "0"));

				it.putExtras(bu);
				mContext.startActivity(it);
				break;
			case R.id.layout_jingxuan_child_item_01:
				bu.putString("url",
						mArrayListBrandFilter.get(position).mArrayListData
								.get(0).web_url);
				bu.putInt(
						"docId",
						Integer.parseInt(mArrayListBrandFilter.get(position).mArrayListData
								.get(0).doc_id != "" ? mArrayListBrandFilter
								.get(position).mArrayListData.get(0).doc_id
								: "0"));
				it.putExtras(bu);
				mContext.startActivity(it);
				break;
			case R.id.layout_jingxuan_child_item_02:
				bu.putString("url",
						mArrayListBrandFilter.get(position).mArrayListData
								.get(1).web_url);
				bu.putInt(
						"docId",
						Integer.parseInt(mArrayListBrandFilter.get(position).mArrayListData
								.get(1).doc_id != "" ? mArrayListBrandFilter
								.get(position).mArrayListData.get(1).doc_id
								: "0"));
				it.putExtras(bu);
				mContext.startActivity(it);
				break;
			case R.id.layout_jingxuan_child_item_03:
				bu.putString("url",
						mArrayListBrandFilter.get(position).mArrayListData
								.get(2).web_url);
				bu.putInt(
						"docId",
						Integer.parseInt(mArrayListBrandFilter.get(position).mArrayListData
								.get(2).doc_id != "" ? mArrayListBrandFilter
								.get(position).mArrayListData.get(2).doc_id
								: "0"));
				it.putExtras(bu);
				mContext.startActivity(it);
				break;
			case R.id.layout_jingxuan_child_item_04:
				bu.putString("url",
						mArrayListBrandFilter.get(position).mArrayListData
								.get(3).web_url);
				bu.putInt(
						"docId",
						Integer.parseInt(mArrayListBrandFilter.get(position).mArrayListData
								.get(3).doc_id != "" ? mArrayListBrandFilter
								.get(position).mArrayListData.get(3).doc_id
								: "0"));
				it.putExtras(bu);
				mContext.startActivity(it);
				break;
			}
		}
	}

}