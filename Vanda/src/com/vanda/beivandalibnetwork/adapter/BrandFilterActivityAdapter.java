package com.vanda.beivandalibnetwork.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.vanda.beivandalibnetwork.pojo.BrandData;
import com.vanda.beivandalibnetwork.url.Urls;
import com.vanda.beivandalibnetworkdemo.R;
import com.vanda.vandalibnetwork.arrayadapter.PullLoadArrayAdaper;
import com.vanda.vandalibnetwork.daterequest.RequestManager;
import com.vanda.vandalibnetwork.staticdata.StaticData;
import com.vanda.vandalibnetwork.utils.StringUtils;

public class BrandFilterActivityAdapter extends PullLoadArrayAdaper<BrandData> {

	private Context mContext;
	private ViewHold mViewHold;
	private List<BrandData> mArrayListBrandFilter;

	public BrandFilterActivityAdapter(Context context, int resource,
			List<BrandData> objects) {
		super(context, resource, objects);
		mContext = context;
		mArrayListBrandFilter = objects;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.search_item, null);
			mViewHold = new ViewHold();
			mViewHold.mImageViewProImage = (ImageView) convertView
					.findViewById(R.id.brandfilter_iv_product_image);
			mViewHold.mImageViewYiZhiKuai = (ImageView) convertView
					.findViewById(R.id.yizhikuai);

			LayoutParams params = ((RelativeLayout) convertView
					.findViewById(R.id.search_item_ll)).getLayoutParams();
			params.width = (StaticData.ScreenWidth) / 3;
			params.height = params.width * 3 / 4;
			// mViewHold.mImageViewProImage.setLayoutParams(params);
			LayoutParams params1 = mViewHold.mImageViewProImage
					.getLayoutParams();
			params1.width = StaticData.ScreenWidth / 3;
			params1.height = params1.width * 3 / 4;
			mViewHold.mImageViewProImage.setLayoutParams(params1);
			// mViewHold.mImageViewShoppingCar = (ImageView) convertView
			// .findViewById(R.id.brandfilter_iv_shopping_car);
			mViewHold.mRelativeLayoutItem = (RelativeLayout) convertView
					.findViewById(R.id.search_item_rl);
			mViewHold.mTextViewProductButPeople = (TextView) convertView
					.findViewById(R.id.brandfilter_tv_buy_people);
			mViewHold.mTextViewProductMarketPrice = (TextView) convertView
					.findViewById(R.id.brandfilter_tv_product_market_price);
			mViewHold.mTextViewProductName = (TextView) convertView
					.findViewById(R.id.brandfilter_tv_product_name);
			mViewHold.mTextViewProductPrice = (TextView) convertView
					.findViewById(R.id.brandfilter_tv_product_price);
			mViewHold.mTextViewProductMarketPrice.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG);
			convertView.setTag(mViewHold);
		} else {
			mViewHold = (ViewHold) convertView.getTag();
		}
		if (mViewHold.imageRequest != null) {
			mViewHold.imageRequest.cancelRequest();
		}
		if (mArrayListBrandFilter.get(position).free_deliver == 1) {
			mViewHold.mImageViewYiZhiKuai.setVisibility(View.VISIBLE);
		} else {
			mViewHold.mImageViewYiZhiKuai.setVisibility(View.GONE);
		}
		mViewHold.mTextViewProductButPeople.setText(mArrayListBrandFilter
				.get(position).totalSale + "人已购买");
		mViewHold.mTextViewProductName.setText(StringUtils
				.stringFilter(StringUtils.ToDBC(mArrayListBrandFilter
						.get(position).prodname)));
		mViewHold.mTextViewProductPrice.setText("￥ "
				+ mArrayListBrandFilter.get(position).salePrice);
		mViewHold.mTextViewProductMarketPrice.setText("￥ "
				+ mArrayListBrandFilter.get(position).parPrice);
		mViewHold.imageRequest = RequestManager.loadImage(Urls.IMAGE_URL_HEAD
				+ mArrayListBrandFilter.get(position).imgUrl, RequestManager
				.getImageListener(mViewHold.mImageViewProImage,
						0// (StaticData.ScreenWidth) / 4
						, RequestManager.mDefaultImageDrawable,
						RequestManager.mDefaultImageDrawable));
		mViewHold.mRelativeLayoutItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Bundle bu = new Bundle();
				// bu.putInt("prodid",
				// mArrayListBrandFilter.get(position).prodId);
				// Intent it = new Intent(mContext,
				// ProductIntroduceActivity.class);
				// it.putExtras(bu);
				// mContext.startActivity(it);
			}
		});
		return convertView;
	}

	public class ViewHold {
		public RelativeLayout mRelativeLayoutItem;
		public ImageView mImageViewProImage;
		public ImageView mImageViewShoppingCar;
		public ImageView mImageViewYiZhiKuai;
		public TextView mTextViewProductName;
		public TextView mTextViewProductPrice;
		public TextView mTextViewProductMarketPrice;
		public TextView mTextViewProductButPeople;
		public ImageLoader.ImageContainer imageRequest;
	}

	// @Override
	// public int getCount() {
	// return mArrayListBrandFilter.size();
	// }
	//
	// public int getItemCount() {
	// return mArrayListBrandFilter.size();
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// return position;
	// }
}
