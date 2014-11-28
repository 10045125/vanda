/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.viewpagerindicator;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class TabPageIndicator extends HorizontalScrollView implements
		PageIndicator {
	/** Title text used when no title is provided by the adapter. */
	private static final CharSequence EMPTY_TITLE = "";

	/**
	 * Interface for a callback when the selected tab has been reselected.
	 */
	public interface OnTabReselectedListener {
		/**
		 * Callback when the selected tab has been reselected.
		 *
		 * @param position
		 *            Position of the current center item.
		 */
		void onTabReselected(int position);
	}

	private Runnable mTabSelector;

	public interface TabClickListenerPosition {
		public void goToClick(int position);
	}

	private TabClickListenerPosition l;

	public void setTabClickListenerPositionInterface(TabClickListenerPosition l) {
		this.l = l;
	}

	private final class TabClickListener implements OnClickListener {

		private int position;

		public TabClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View view) {
			if (l != null) {
				l.goToClick(position);
			}
			TabView tabView = (TabView) view;
			final int oldSelected = mViewPager.getCurrentItem();
			// Log.i("indicator->oldSlect_id:", "" + oldSelected);
			final int newSelected = tabView.getIndex();
			// Log.i("indicator->newSlect_id:", "" + newSelected);
			// if (!isStyle) {
			// TabView oldTabView = (TabView) mTabLayout
			// .getChildAt(oldSelected);
			// oldTabView.setTextSize(mTextSizeDef);
			// TabView newTabView = (TabView) mTabLayout
			// .getChildAt(newSelected);
			// newTabView.setTextSize(mTextSizeSelect);
			// }
			mViewPager.setCurrentItem(newSelected);
			if (oldSelected == newSelected && mTabReselectedListener != null) {
				mTabReselectedListener.onTabReselected(newSelected);
			}
		}

	}

	private final OnClickListener mTabClickListener = new OnClickListener() {
		public void onClick(View view) {
			TabView tabView = (TabView) view;
			final int oldSelected = mViewPager.getCurrentItem();
			// Log.i("indicator->oldSlect_id:", "" + oldSelected);
			final int newSelected = tabView.getIndex();
			// Log.i("indicator->newSlect_id:", "" + newSelected);
			if (!isStyle) {
				TabView oldTabView = (TabView) mTabLayout
						.getChildAt(oldSelected);
				oldTabView.setTextSize(mTextSizeDef);
				TabView newTabView = (TabView) mTabLayout
						.getChildAt(newSelected);
				newTabView.setTextSize(mTextSizeSelect);
			}
			mViewPager.setCurrentItem(newSelected);
			if (oldSelected == newSelected && mTabReselectedListener != null) {
				mTabReselectedListener.onTabReselected(newSelected);
			}
		}
	};

	private final IcsLinearLayout mTabLayout;

	private ViewPager mViewPager;
	private ViewPager.OnPageChangeListener mListener;

	private int mMaxTabWidth;
	private int mSelectedTabIndex;
	private int mOldTabIndex;
	private int mItemCount;

	private int mTextSizeDef = 14;
	private int mTextSizeSelect = 16;

	private boolean isStyle = true;
	private int textColor = 0;
	private boolean isSetTextColor = false;

	private boolean left = false;
	private boolean right = false;
	private boolean isScrolling = false;
	private int lastValue = -1;
	private ChangeViewCallback changeViewCallback = null;

	private OnTabReselectedListener mTabReselectedListener;

	public TabPageIndicator(Context context) {
		this(context, null);
	}

	public TabPageIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		setHorizontalScrollBarEnabled(false);
		mTabLayout = new IcsLinearLayout(context,
				R.attr.vpiTabPageIndicatorStyle);
		addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT,// MATCH_PARENT
				WRAP_CONTENT));
	}

	public void setOnTabReselectedListener(OnTabReselectedListener listener) {
		mTabReselectedListener = listener;
	}

	public void setTabTextSizeDefault(int defualtTextSize) {
		mTextSizeDef = defualtTextSize;
	}

	public void setTabTextSizeSelect(int selectTextSize) {
		mTextSizeSelect = selectTextSize;
	}

	public void isStyle(boolean isStyle) {
		this.isStyle = isStyle;
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
		setFillViewport(lockedExpanded);

		final int childCount = mTabLayout.getChildCount();
		if (childCount > 1
				&& (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
			if (childCount > 2) {
				mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
			} else {
				mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
			}
		} else {
			mMaxTabWidth = -1;
		}

		final int oldWidth = getMeasuredWidth();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int newWidth = getMeasuredWidth();

		if (lockedExpanded && oldWidth != newWidth) {
			// Recenter the tab display if we're at a new (scrollable) size.
			setCurrentItem(mSelectedTabIndex);
		}
	}

	private void animateToTab(final int position) {
		Log.i("indicator->mItemCount_id:", "" + mItemCount);
		// if (!isStyle) {
		// if (mOldTabIndex <= mItemCount && mOldTabIndex >= 0) {
		// final TabView oldTabView = (TabView) mTabLayout
		// .getChildAt(mOldTabIndex);
		// if (oldTabView != null) {
		// oldTabView.setTextSize(mTextSizeDef);
		// }
		// }
		// final TabView newTabView = (TabView) mTabLayout
		// .getChildAt(position);
		// newTabView.setTextSize(mTextSizeSelect);
		// }

		final View tabView = mTabLayout.getChildAt(position);

		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
		mTabSelector = new Runnable() {
			public void run() {
				final int scrollPos = tabView.getLeft()
						- (getWidth() - tabView.getWidth()) / 2;
				smoothScrollTo(scrollPos, 0);
				mTabSelector = null;
			}
		};
		post(mTabSelector);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mTabSelector != null) {
			// Re-post the selector we saved
			post(mTabSelector);
		}
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
	}

	public void setTextColor(int color) {
		isSetTextColor = true;
		textColor = color;
	}

	private void addTab(int index, CharSequence text, int iconResId) {
		final TabView tabView = new TabView(getContext());
		tabView.mIndex = index;
		tabView.setFocusable(true);
		// tabView.setOnClickListener(mTabClickListener);
		tabView.setOnClickListener(new TabClickListener(index));
		if (!isStyle) {
			tabView.setTextSize(mTextSizeDef);
			if (isSetTextColor) {
				tabView.setTextAppearance(getContext(), textColor);
			}
		}
		tabView.setText(text);
		// tabView.setOnFocusChangeListener(new OnFocusChangeListener() {
		//
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// Log.i("setOnFocusChangeListener", "setOnFocusChangeListener");
		// if (!hasFocus)
		// ((TextView) v).setTextSize(mTextSizeDef);
		// else
		// ((TextView) v).setTextSize(mTextSizeSelect);
		// }
		// });

		if (iconResId != 0) {
			tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
		}
		mItemCount++;
		mTabLayout.addView(tabView, new LinearLayout.LayoutParams(0,
				MATCH_PARENT, 1));
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		if (mListener != null) {

			mListener.onPageScrollStateChanged(arg0);

		}
		if (arg0 == 1) {
			isScrolling = true;

		} else {
			isScrolling = false;

		}

		// Log.i("meityitianViewPager",
		// "meityitianViewPager  onPageScrollStateChanged : arg0:" + arg0);
		if (arg0 == 2) {
			// Log.i("meityitianViewPager",
			// "meityitianViewPager  onPageScrollStateChanged  direction left ? "
			// + left);
			// Log.i("meityitianViewPager",
			// "meityitianViewPager  onPageScrollStateChanged  direction right ? "
			// + right);
			// notify ....
			if (changeViewCallback != null) {
				changeViewCallback.changeView(left, right);
			}
			right = left = false;
		}

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if (isScrolling) {
			if (lastValue > arg2) {
				// 递增，向左侧滑动
				mOldTabIndex = arg0 + 1;
				right = true;
				left = false;
			} else if (lastValue < arg2) {
				// 递减，向右侧滑动
				mOldTabIndex = arg0;
				right = false;
				left = true;
			} else if (lastValue == arg2) {
				right = left = false;
			}
		}
		// Log.i("meityitianViewPager",
		// "meityitianViewPager onPageScrolled  last :arg2  ," + lastValue
		// + ":" + arg2);
		lastValue = arg2;
		if (mListener != null) {
			mListener.onPageScrolled(arg0, arg1, arg2);
		}
	}

	@Override
	public void onPageSelected(int arg0) {
		if (changeViewCallback != null) {
			changeViewCallback.getCurrentPageIndex(arg0);
		}
		setCurrentItem(arg0);
		if (mListener != null) {
			mListener.onPageSelected(arg0);
		}
	}

	@Override
	public void setViewPager(ViewPager view) {
		if (mViewPager == view) {
			return;
		}
		if (mViewPager != null) {
			mViewPager.setOnPageChangeListener(null);
		}
		final PagerAdapter adapter = view.getAdapter();
		if (adapter == null) {
			throw new IllegalStateException(
					"ViewPager does not have adapter instance.");
		}
		mViewPager = view;
		// mItemCount = mViewPager.getChildCount();
		// Log.i("setViewPage_id:", "" + mItemCount);
		view.setOnPageChangeListener(this);
		notifyDataSetChanged();
	}

	public void notifyDataSetChanged() {
		mTabLayout.removeAllViews();
		PagerAdapter adapter = mViewPager.getAdapter();
		IconPagerAdapter iconAdapter = null;
		if (adapter instanceof IconPagerAdapter) {
			iconAdapter = (IconPagerAdapter) adapter;
		}
		final int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			CharSequence title = adapter.getPageTitle(i);
			if (title == null) {
				title = EMPTY_TITLE;
			}
			int iconResId = 0;
			if (iconAdapter != null) {
				iconResId = iconAdapter.getIconResId(i);
			}
			addTab(i, title, iconResId);
		}

		if (mSelectedTabIndex > count) {
			mSelectedTabIndex = count - 1;
		}
		setCurrentItem(mSelectedTabIndex);
		requestLayout();
	}

	@Override
	public void setViewPager(ViewPager view, int initialPosition) {
		setViewPager(view);
		setCurrentItem(initialPosition);
	}

	@Override
	public void setCurrentItem(int item) {
		if (mViewPager == null) {
			throw new IllegalStateException("ViewPager has not been bound.");
		}
		mSelectedTabIndex = item;
		mViewPager.setCurrentItem(item);

		final int tabCount = mTabLayout.getChildCount();
		for (int i = 0; i < tabCount; i++) {
			final View child = mTabLayout.getChildAt(i);
			final boolean isSelected = (i == item);
			child.setSelected(isSelected);
			if (isSelected) {
				animateToTab(item);
			}
		}
	}

	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mListener = listener;
	}

	private class TabView extends TextView {
		private int mIndex;

		public TabView(Context context) {
			super(context, null, R.attr.vpiTabPageIndicatorStyle);
		}

		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);

			// Re-measure if we went beyond our maximum size.
			if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
				super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth,
						MeasureSpec.EXACTLY), heightMeasureSpec);
			}
		}

		public int getIndex() {
			return mIndex;
		}
	}

	/**
	 * 得到是否向右侧滑动
	 * 
	 * @return true 为右滑动
	 */
	public boolean getMoveRight() {
		return right;
	}

	/**
	 * 得到是否向左侧滑动
	 * 
	 * @return true 为左做滑动
	 */
	public boolean getMoveLeft() {
		return left;
	}

	/**
	 * 滑动状态改变回调
	 * 
	 * @author zxy
	 * 
	 */
	public interface ChangeViewCallback {
		/**
		 * 切换视图 ？决定于left和right 。
		 * 
		 * @param left
		 * @param right
		 */
		public void changeView(boolean left, boolean right);

		public void getCurrentPageIndex(int index);
	}

	/**
	 * set ...
	 * 
	 * @param callback
	 */
	public void setChangeViewCallback(ChangeViewCallback callback) {
		changeViewCallback = callback;
	}

}
