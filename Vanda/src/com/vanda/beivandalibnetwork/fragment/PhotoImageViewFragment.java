package com.vanda.beivandalibnetwork.fragment;

import java.io.File;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.nostra13.universalimageloader.cache.disc.impl.FileCountLimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.vanda.beivandalibnetwork.utils.ProgressWheel;
import com.vanda.beivandalibnetworkdemo.R;
import com.vanda.vandalibnetwork.application.AppData;
import com.vanda.vandalibnetwork.utils.CacheUtils;

public class PhotoImageViewFragment extends SherlockFragment {

	private PhotoView mPhotoView;
	private PhotoViewAttacher mAttacher;
	private ProgressWheel mProgressWheel;
	private String imageUrl = null;

	public static PhotoImageViewFragment newInstance(String imageUrl) {
		PhotoImageViewFragment mPhotoImageViewFragment = new PhotoImageViewFragment();
		mPhotoImageViewFragment.imageUrl = imageUrl;
		return mPhotoImageViewFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.photo_imageview, null);
		mPhotoView = (PhotoView) view.findViewById(R.id.photoView);
		mProgressWheel = (ProgressWheel) view.findViewById(R.id.progressWheel);
		mAttacher = new PhotoViewAttacher(mPhotoView);
		mAttacher
				.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
					@Override
					public void onPhotoTap(View view, float x, float y) {
						if (mOnPhotoViewClickListener != null) {
							mOnPhotoViewClickListener.setOnClickEvent();
						}
					}
				});
		mAttacher.setOnViewTapListener(new OnViewTapListener() {

			@Override
			public void onViewTap(View view, float x, float y) {
				if (mOnPhotoViewClickListener != null) {
					mOnPhotoViewClickListener.setOnClickEvent();
				}
			}
		});
		mAttacher.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (mOnPhotoViewClickListener != null) {
					mOnPhotoViewClickListener.downImage();
				}
				return false;
			}
		});
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.ARGB_8888).build();
		File DEFAULT_IMAGE_CACHE_DIR = CacheUtils.getExternalCacheDir(AppData
				.getContext());
		ImageLoaderConfiguration DEFAULT_IMAGE_CONFIGS = new ImageLoaderConfiguration.Builder(
				AppData.sContext)

				.threadPoolSize(3)
				// default
				.discCache(
						new FileCountLimitedDiscCache(DEFAULT_IMAGE_CACHE_DIR,
								100))
				// default
				.discCacheSize(1024 * 1024).discCacheFileCount(200)
				.memoryCacheSize(5 * 1024 * 1024)
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
				// default
				.imageDownloader(new BaseImageDownloader(AppData.sContext)) // default
				.imageDecoder(new BaseImageDecoder(true)) // default
				.defaultDisplayImageOptions(options) // default
				.build();
		ImageLoader.getInstance().init(DEFAULT_IMAGE_CONFIGS);
		ImageLoader.getInstance().displayImage(imageUrl, mPhotoView, options,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						mProgressWheel.setVisibility(View.GONE);
						mAttacher.update();
					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view,
							int current, int total) {
						mProgressWheel.setProgress(360 * current / total);
					}
				});
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mAttacher != null) {
			mAttacher.cleanup();
		}
	}

	private OnPhotoViewClickListener mOnPhotoViewClickListener;

	public void setOnPhotoViewClickListener(
			OnPhotoViewClickListener mOnPhotoViewClickListener) {
		this.mOnPhotoViewClickListener = mOnPhotoViewClickListener;
	}

	public interface OnPhotoViewClickListener {
		public void setOnClickEvent();

		public void downImage();
	}
}
