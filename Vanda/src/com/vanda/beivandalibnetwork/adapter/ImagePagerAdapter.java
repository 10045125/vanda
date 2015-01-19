package com.vanda.beivandalibnetwork.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vanda.beivandalibnetwork.activity.WebViewActivity;
import com.vanda.beivandalibnetwork.pojo.InformationIndication;
import com.vanda.beivandalibnetworkdemo.R;
import com.vanda.vandalibnetwork.daterequest.RequestManager;
import com.viewpagerindicator.IconPagerAdapter;

public class ImagePagerAdapter extends PagerAdapter implements IconPagerAdapter {

	private LayoutInflater inflater;
	private ArrayList<InformationIndication> imagesResource;
	private Drawable mDefaultImageDrawable = new ColorDrawable(Color.argb(255,
			201, 201, 201));
	private int mCount;
	private Context context;

	public ImagePagerAdapter(ArrayList<InformationIndication> images,
			Context ctx) {

		this.context = ctx;
		this.imagesResource = images;
		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (this.imagesResource != null && this.imagesResource.size() == 3) {
			this.imagesResource.add(this.imagesResource.get(0));
		}
		mCount = this.imagesResource.size();
	}

	public void setImageUrls(String[] images) {
		// this.images = images;
		notifyDataSetChanged();
	}

	public void setImageView(View view, int position) {
		// final ImageView mImageView = (ImageView)
		// view.findViewById(R.id.image);
		// imageLoader.displayImage(Urls.URL_IMAGE_PREFIX + images[position],
		// mImageView, DaoImagesApplication.DEFAULT_IMAGE_OPTIONS);
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	// @Override
	// public void setPrimaryItem(View container, int position, Object object) {
	// // setImageView(mViewlist.get(position), position);
	// // notifyDataSetChanged();
	// }

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public int getCount() {
		return mCount;
		// return imagesResource.length;
	}

	@Override
	public Object instantiateItem(ViewGroup view, final int position) {

		View imageLayout = inflater.inflate(R.layout.item_pager_image, view,
				false);
		assert imageLayout != null;
		final ImageView imageView = (ImageView) imageLayout
				.findViewById(R.id.image);
		TextView mTextView = (TextView) imageLayout
				.findViewById(R.id.item_page_image_title);
		if (imagesResource.size() != 0)
			mTextView.setText(imagesResource.get(position).title);
		if (imagesResource.size() != 0)
			RequestManager.loadImage(imagesResource.get(position).pic_src,
					RequestManager.getImageListener(imageView, 0,
							mDefaultImageDrawable, mDefaultImageDrawable));
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(imagesResource.get(position).doc_id == null){
					Toast.makeText(context, "内容不存在！", Toast.LENGTH_SHORT).show();
					return;
				}
				Bundle bu = new Bundle();
				bu.putString("url", imagesResource.get(position).web_url);
				bu.putString("docId",
						imagesResource.get(position).doc_id);
				Intent it = new Intent(context, WebViewActivity.class);
				it.putExtras(bu);
				context.startActivity(it);
			}
		});
		view.addView(imageLayout);
		return imageLayout;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public int getIconResId(int index) {
		return 0;
	}

	public void setCount(ArrayList<InformationIndication> images) {
		this.imagesResource = images;
		if (this.imagesResource != null && this.imagesResource.size() == 3) {
			this.imagesResource.add(this.imagesResource.get(0));
		}
		notifyDataSetChanged();
	}

}
