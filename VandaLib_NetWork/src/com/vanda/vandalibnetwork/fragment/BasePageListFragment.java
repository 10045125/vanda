package com.vanda.vandalibnetwork.fragment;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
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

public abstract class BasePageListFragment<T, K> extends BaseFragment<T>
		implements PullToRefreshAttacher.OnRefreshListener {
	protected PullLoadArrayAdaper<K> mPullLoadArrayAdaper;
	protected ArrayList<K> mArrayList;
	protected List<K> mList;
	protected ListView mListView;
	private Context context;

	public Pagination mPagination = new Pagination(20); // use for pagination
	protected PullToRefreshAttacher mPullToRefreshAttacher;
	public int mDataItemCount = 0;
	protected LoadingFooter mLoadingFooter;

	public BasePageListFragment(Context context) {
		this.context = context;
	}

	protected void initListViewData(View view, int resId) {
		mListView = (ListView) view.findViewById(resId);
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

	public void initData() {
		mPullToRefreshAttacher.setRefreshableView(mListView, this);
		Log.i("initData mPullToRefreshAttacher", "" + mPullToRefreshAttacher);
		mLoadingFooter = new LoadingFooter(getActivity());
		View header = new View(getActivity());
		mListView.addHeaderView(header);
		mListView.addFooterView(mLoadingFooter.getView());
		AnimationAdapter animationAdapter = new CardsAnimationAdapter(
				mPullLoadArrayAdaper);
		animationAdapter.setListView(mListView);
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
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mPullToRefreshAttacher.getHeaderTransformer().onConfigurationChanged(
				getActivity());
	}

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
		Log.i("mPullToRefreshAttacher", "" + mPullToRefreshAttacher);
		if (mPullToRefreshAttacher == null) {
			PullToRefreshAttacher.Options options = new PullToRefreshAttacher.Options();
			options.headerInAnimation = R.anim.pulldown_fade_in;
			options.headerOutAnimation = R.anim.pulldown_fade_out;
			options.refreshScrollDistance = 0.3f;
			options.headerLayout = R.layout.pulldown_header;
			mPullToRefreshAttacher = new PullToRefreshAttacher(
					(Activity) context, options);
		}
		if (!mPullToRefreshAttacher.isRefreshing() && !nextPage) {
			mPullToRefreshAttacher.setRefreshing(true);
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
						RequestManager.cancelAll(BasePageListFragment.this);
						if (!nextPage) {
							mPullToRefreshAttacher.setRefreshComplete();
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
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						RequestManager.cancelAll(BasePageListFragment.this);
						if (!nextPage) {
							mPullToRefreshAttacher.setRefreshComplete();
						}
					}
				});
		RequestManager.addRequest(mLoadRequestData, BasePageListFragment.this);
	}

	protected void setRefreshMode(Pagination page) {
		mLoadingFooter.setState(page.hasMore() ? LoadingFooter.State.Idle
				: LoadingFooter.State.TheEnd, 3000);
	}

	protected void loadNextPage() {
		mLoadingFooter.setState(LoadingFooter.State.Loading);
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
	public void onRefreshStarted(View view) {
		mPagination.page = 1;
		loadFirstPage(false);
	}

}
