package com.vanda.beivandalibnetwork.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.salvage.RecyclingPagerAdapter;
import com.vanda.beivandalibnetwork.activity.WebViewActivity;
import com.vanda.beivandalibnetwork.pojo.InformationIndication;
import com.vanda.beivandalibnetworkdemo.R;
import com.vanda.vandalibnetwork.daterequest.RequestManager;

public class ImagePagerIndicatorAdapter extends RecyclingPagerAdapter {

	private Context context;
	private List<InformationIndication> imageIdList;

	private int size;
	private boolean isInfiniteLoop;

	public ImagePagerIndicatorAdapter(Context context,
			List<InformationIndication> imageIdList) {
		this.context = context;
		this.imageIdList = imageIdList;
		this.size = imageIdList.size();
		isInfiniteLoop = false;
		if (this.imageIdList != null && this.imageIdList.size() == 3) {
			this.imageIdList.add(this.imageIdList.get(0));
		}
	}

	@Override
	public int getCount() {
		// Infinite loop
		return isInfiniteLoop ? Integer.MAX_VALUE : imageIdList.size();
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	public int getPosition(int position) {
		return isInfiniteLoop ? position % size : position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup container) {
		ViewHolder holder;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.item_pager_image, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		if (imageIdList.size() != 0) {
			holder.mTextView
					.setText(imageIdList.get(getPosition(position)).title);
			RequestManager.loadImage(
					imageIdList.get(getPosition(position)).pic_src,
					RequestManager.getImageListener(holder.imageView, 0,
							RequestManager.mDefaultImageDrawable,
							RequestManager.mDefaultImageDrawable));
		}
		holder.imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bu = new Bundle();
				bu.putString("url", imageIdList.get(position).web_url);
				bu.putInt("docId",
						Integer.parseInt(imageIdList.get(position).doc_id));
				Intent it = new Intent(context, WebViewActivity.class);
				it.putExtras(bu);
				context.startActivity(it);
			}
		});
		return view;
	}

	private static class ViewHolder {

		public ImageView imageView;
		public TextView mTextView;

		public ViewHolder(View view) {
			imageView = (ImageView) view.findViewById(R.id.image);
			mTextView = (TextView) view
					.findViewById(R.id.item_page_image_title);
		}

	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop
	 *            the isInfiniteLoop to set
	 */
	public ImagePagerIndicatorAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}
}
