package com.vanda.vandalibnetwork.fragmentactivity;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.haarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.vanda.vandalibnetwork.R;
import com.vanda.vandalibnetwork.arrayadapter.CardsAnimationAdapter;
import com.vanda.vandalibnetwork.arrayadapter.PullLoadArrayAdaper;
import com.vanda.vandalibnetwork.daterequest.GsonRequest;
import com.vanda.vandalibnetwork.daterequest.RequestManager;
import com.vanda.vandalibnetwork.listview.ListViewUtils;
import com.vanda.vandalibnetwork.utils.Pagination;
import com.vanda.vandalibnetwork.view.LoadingFooter;

/**
 * @author vanda 伍中联
 *         这个是为了简便Fragment包含listiew要刷新的情况，此ListView使用官方提供的刷新控件，需要最新的V_4包。需要注意几点
 *         1. 子类需要实例化以及实例化的顺序为 initArrayListData
 *         ()，initListViewData()，initPullLoadArrayAdaperData
 *         ()，initData()确保数据对象被创建 2. mArrayList
 *         只是作为数据的子载体，而mList是一个数据集合，子载体会被添加到数据集合中，
 *         所以在创建Arraydapter时，需要向其将mList参数传入。
 * @param <T>
 *            数据解析类
 * @param <K>
 *            ListView 的条目数据集合中载体
 */
public abstract class BaseSwipeRefreshSherlockFragmentActivity<T, K> extends
		BaseFragmentActivity<T> implements SwipeRefreshLayout.OnRefreshListener {

	protected SwipeRefreshLayout mSwipeRefreshLayout;
	protected ListView mListView;
	protected PullLoadArrayAdaper<K> mPullLoadArrayAdaper;
	protected ArrayList<K> mArrayList;
	protected List<K> mList;
	protected Pagination mPagination = new Pagination(20); // use for pagination
	protected int mDataItemCount = 0;
	protected LoadingFooter mLoadingFooter;

	protected void initSwipeLayoutData(int resId) {
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(resId);
		mSwipeRefreshLayout.setColorScheme(R.color.holo_blue_bright,
				R.color.holo_green_light, R.color.holo_orange_light,
				R.color.holo_red_light);
	}

	protected void initListViewData(int resId) {
		mListView = (ListView) findViewById(resId);
	}

	protected void initPullLoadArrayAdaperData(
			PullLoadArrayAdaper<K> mPullLoadArrayAdaper) {
		this.mPullLoadArrayAdaper = mPullLoadArrayAdaper;
	}

	protected void initArrayListData() {
		this.mList = new ArrayList<K>();
		this.mArrayList = new ArrayList<K>();
	}

	protected void setArrayListData(ArrayList<K> mArrayList) {
		this.mArrayList = mArrayList;
	}

	protected void setDataItemCount(int mDataItemCount) {
		this.mDataItemCount = mDataItemCount;
	}

	protected void initData() {
		AnimationAdapter animationAdapter = new CardsAnimationAdapter(
				mPullLoadArrayAdaper);
		animationAdapter.setListView(mListView);
		mLoadingFooter = new LoadingFooter(this);
		mListView.addFooterView(mLoadingFooter.getView());
		mListView.setAdapter(animationAdapter);
		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (mLoadingFooter.getState() == LoadingFooter.State.Loading
						|| mLoadingFooter.getState() == LoadingFooter.State.TheEnd) {
					return;
				}
				if (firstVisibleItem + visibleItemCount >= totalItemCount
						&& totalItemCount != 0
						&& totalItemCount != mListView.getHeaderViewsCount()
								+ mListView.getFooterViewsCount()
						&& mListView.getCount() > 0) {
					loadNextPage();
				}
			}
		});
	}

	@Override
	public void onStart() {
		mSwipeRefreshLayout
				.setOnRefreshListener(BaseSwipeRefreshSherlockFragmentActivity.this);
		super.onStart();
	}

	/**
	 * 这个方法必须处理父类的mArrayList和mDataItemCount
	 */
	protected abstract void addArrayListData(T response);

	@Override
	protected void processData(T response) {
		super.processData(response);
		addArrayListData(response);
		mPagination = new Pagination(mArrayList.size(), mDataItemCount, 20);
		setRefreshMode(mPagination);
		mPullLoadArrayAdaper.addAll(mArrayList);
		mPullLoadArrayAdaper.notifyDataSetChanged();
	}

	protected abstract String getRefDataUrl(int page, int size);

	protected void loadData(final boolean nextPage) {
		if (!mSwipeRefreshLayout.isRefreshing() && !nextPage) {
			mSwipeRefreshLayout.setRefreshing(true);
		}
		String url = getRefDataUrl(mPagination.page
				+ (nextPage == true ? 1 : 0), mPagination.size);
		if (nextPage) {
			mPagination.page += 1;
		}
		Request<T> mLoadRequestData = new GsonRequest<T>(Method.GET, url,
				getResponseDataClass(), null, new Response.Listener<T>() {

					@Override
					public void onResponse(T response) {
						RequestManager
								.cancelAll(BaseSwipeRefreshSherlockFragmentActivity.this);
						if (!nextPage) {
							mSwipeRefreshLayout.setRefreshing(false);
							mPullLoadArrayAdaper.clear();
							mArrayList.clear();
						} else {
							mArrayList.clear();
						}
						addArrayListData(response);
						if (!nextPage) {
							mPagination = null;
							mPagination = new Pagination(mArrayList.size(),
									mDataItemCount, 20);
							setRefreshMode(mPagination);
						} else {
							mPagination.updateLoaded(mArrayList.size());
							setRefreshMode(mPagination);
						}
						mPullLoadArrayAdaper.addAll(mArrayList);
						mPullLoadArrayAdaper.notifyDataSetChanged();
						if (mOnPullDownRefresh != null)
							mOnPullDownRefresh.onPullDownRefreshComplete();
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						RequestManager
								.cancelAll(BaseSwipeRefreshSherlockFragmentActivity.this);
						if (!nextPage) {
							mSwipeRefreshLayout.setRefreshing(false);
						} else {
							mLoadingFooter.getView().setVisibility(View.GONE);
						}
						if (mOnPullDownRefresh != null)
							mOnPullDownRefresh.onPullDownRefreshComplete();
					}
				});
		RequestManager.addRequest(mLoadRequestData,
				BaseSwipeRefreshSherlockFragmentActivity.this);
	}

	protected void setRefreshMode(Pagination page) {
		mLoadingFooter.setState(page.hasMore() ? LoadingFooter.State.Idle
				: LoadingFooter.State.TheEnd, 3000);
	}

	protected void loadNextPage() {
		// mLoadingFooter.setState(LoadingFooter.State.Loading);
		if (mOnPullDownRefresh != null)
			mOnPullDownRefresh.onPullDownRefreshing();
		loadData(true);
	}

	protected void loadFirstPage(final boolean nextPage) {
		loadData(false);
	}

	public void loadFirstPageAndScrollToTop() {
		ListViewUtils.smoothScrollListViewToTop(mListView);
		mPagination.page = 1;
		loadFirstPage(false);
	}

	@Override
	public void onRefresh() {
		if (mOnPullDownRefresh != null)
			mOnPullDownRefresh.onPullDownRefreshing();
		mPagination.page = 1;
		loadFirstPage(false);
	}

	protected OnPullDownRefresh mOnPullDownRefresh;

	public void setOnPullDownRefresh(OnPullDownRefresh mOnPullDownRefresh) {
		this.mOnPullDownRefresh = mOnPullDownRefresh;
	}

	public interface OnPullDownRefresh {
		public void onPullDownRefreshComplete();

		public void onPullDownRefreshing();
	}

}
