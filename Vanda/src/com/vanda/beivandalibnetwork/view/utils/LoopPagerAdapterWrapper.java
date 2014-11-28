/*
 * Copyright (C) 2013 Leszek Mzyk
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
package com.vanda.beivandalibnetwork.view.utils;

import android.os.Parcelable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * A PagerAdapter wrapper responsible for providing a proper page to
 * LoopViewPager
 * 
 * This class shouldn't be used directly
 */
public class LoopPagerAdapterWrapper extends PagerAdapter {

	private PagerAdapter mAdapter;

	private SparseArray<ToDestroy> mToDestroy = new SparseArray<ToDestroy>();

	private boolean mBoundaryCaching;

	void setBoundaryCaching(boolean flag) {
		mBoundaryCaching = flag;
	}

	LoopPagerAdapterWrapper(PagerAdapter adapter) {
		this.mAdapter = adapter;
	}

	@Override
	public void notifyDataSetChanged() {
		mToDestroy = new SparseArray<ToDestroy>();
		super.notifyDataSetChanged();
	}

	/*
	 * getcount=adapter.getcount+2;position-1;若realCount=4;
	 * 若position=0;则realPosition=3;//这是�?���?��图片
	 * 若position=1;则realPosition=0;//这是第一张图�? 若position=2;则realPostion=1;
	 * 若position=3;则realPostion=2; 若position=4;则realPostion=3;
	 * 若position=5,则realPosition=0;//这是第一张图�?
	 */
	int toRealPosition(int position) {
		int realCount = getRealCount();
		if (realCount == 0)
			return 0;
		int realPosition = (position - 1) % realCount;
		if (realPosition < 0)
			realPosition += realCount;
		return realPosition;
	}

	/*
	 * 得到的是原viewpager的位�?
	 */
	public int toInnerPosition(int realPosition) {
		int position = (realPosition + 1);

		return position;
		// return position;
	}

	private int getRealFirstPosition() {
		return 1;
	}

	private int getRealLastPosition() {
		return getRealFirstPosition() + getRealCount() - 1;
	}

	@Override
	public int getCount() {
		return mAdapter.getCount() + 2;
	}

	public int getRealCount() {
		return mAdapter.getCount();
	}

	public PagerAdapter getRealAdapter() {
		return mAdapter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.PagerAdapter#instantiateItem(android.view.ViewGroup
	 * , int) 每次要销毁之前先保存�?��第一项和�?���?��的数据，这里的位置为1，和 size，当有缓存边缘项时�?重新拿出�?
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		int realPosition = (mAdapter instanceof FragmentPagerAdapter || mAdapter instanceof FragmentStatePagerAdapter) ? position
				: toRealPosition(position);
		// System.out.println("在那个位置："+position);
		// System.out.println("realPosition:"+realPosition);
		if (mBoundaryCaching) {
			ToDestroy toDestroy = mToDestroy.get(position);
			if (toDestroy != null) {
				mToDestroy.remove(position);// 1,4
				// System.out.println("remove:" + position);
				return toDestroy.object;
			}
		}
		return mAdapter.instantiateItem(container, realPosition);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		int realFirst = getRealFirstPosition();
		int realLast = getRealLastPosition();
		int realPosition = (mAdapter instanceof FragmentPagerAdapter || mAdapter instanceof FragmentStatePagerAdapter) ? position
				: toRealPosition(position);

		if (mBoundaryCaching && (position == realFirst || position == realLast)) {
			mToDestroy.put(position, new ToDestroy(container, realPosition,
					object));
			// 这里保存了第一张和第四张的图片
		} else {
			mAdapter.destroyItem(container, realPosition, object);
		}
	}

	/*
	 * Delegate rest of methods directly to the inner adapter.
	 */

	@Override
	public void finishUpdate(ViewGroup container) {
		mAdapter.finishUpdate(container);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return mAdapter.isViewFromObject(view, object);
	}

	@Override
	public void restoreState(Parcelable bundle, ClassLoader classLoader) {
		mAdapter.restoreState(bundle, classLoader);
	}

	@Override
	public Parcelable saveState() {
		return mAdapter.saveState();
	}

	@Override
	public void startUpdate(ViewGroup container) {
		mAdapter.startUpdate(container);
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		mAdapter.setPrimaryItem(container, position, object);
	}

	/*
	 * End delegation
	 */

	/**
	 * Container class for caching the boundary views
	 */
	static class ToDestroy {
		ViewGroup container;
		int position;
		Object object;

		public ToDestroy(ViewGroup container, int position, Object object) {
			this.container = container;
			this.position = position;
			this.object = object;
		}
	}

}