package com.vanda.beivandalibnetwork.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.haarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.vanda.beivandalibnetwork.activity.FragmentMainActivity;
import com.vanda.beivandalibnetwork.adapter.ImagePagerAdapter;
import com.vanda.beivandalibnetwork.adapter.JingxuanAdapter;
import com.vanda.beivandalibnetwork.pojo.InformationIndication;
import com.vanda.beivandalibnetwork.pojo.JingXuan;
import com.vanda.beivandalibnetwork.url.Urls;
import com.vanda.beivandalibnetwork.utils.HttpUtils;
import com.vanda.beivandalibnetwork.view.utils.AutoScrollViewPager;
import com.vanda.beivandalibnetworkdemo.R;
import com.vanda.vandalibnetwork.arrayadapter.CardsAnimationAdapter;
import com.vanda.vandalibnetwork.listview.ListViewUtils;
import com.vanda.vandalibnetwork.staticdata.StaticData;
import com.vanda.vandalibnetwork.utils.Pagination;
import com.vanda.vandalibnetwork.view.LoadingFooter;
import com.viewpagerindicator.CirclePageIndicator;

public class JingXuanFragment extends SherlockFragment implements
		PullToRefreshAttacher.OnRefreshListener {
	protected JingxuanAdapter mPullLoadArrayAdaper;
	protected ArrayList<JingXuan> mArrayList;
	protected List<JingXuan> mList;
	protected ListView mListView;
	private Context context;

	public Pagination mPagination = new Pagination(20); // use for pagination
	protected PullToRefreshAttacher mPullToRefreshAttacher;
	public int mDataItemCount = 0;
	protected LoadingFooter mLoadingFooter;
	private AsyncTask<String, String, String> mAsyncTask;
	private AsyncTask<String, String, String> mAsyncTask1;

	public static JingXuanFragment newInstance(Context context, int item) {
		JingXuanFragment mJingXuanFragment = new JingXuanFragment();
		mJingXuanFragment.context = context;
		return mJingXuanFragment;
	}

	@Override
	public void onDestroy() {
		if (mAsyncTask != null)
			mAsyncTask.cancel(true);
		if (mAsyncTask1 != null)
			mAsyncTask1.cancel(true);
		super.onDestroy();
	}

	@Override
	public void onResume() {
		Log.i("JingXuanFragment onResume", "onResume");
		super.onResume();
	}

	private ImagePagerAdapter advAdapter;
	private AutoScrollViewPager advViewPager;
	private CirclePageIndicator mIndicator;
	View head;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_shot, null);
		mArrayList = new ArrayList<JingXuan>();
		mList = new ArrayList<JingXuan>();
		mArrayListInformation = new ArrayList<InformationIndication>();
		mListView = (ListView) view.findViewById(R.id.listView);
		mPullLoadArrayAdaper = new JingxuanAdapter(getActivity(),
				R.layout.jingxuan_item, mList, mArrayListInformation);
		mPullToRefreshAttacher = ((FragmentMainActivity) context)
				.getPullToRefreshAttacher();
		initData();
		startGetData();
		// startExecuteRequest(Method.GET);
		return view;
	}

	private ArrayList<InformationIndication> mArrayListInformation;

	private void startGetData() {
		mAsyncTask = new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				String json = null;
				try {
					json = HttpUtils.get(Urls.getInformationImageUrl(19928));
					Gson gson = new Gson();
					JsonParser parser = new JsonParser();
					JsonArray Jarray = parser.parse(json).getAsJsonArray();
					mArrayListInformation.clear();
					for (JsonElement obj : Jarray) {
						InformationIndication cse = gson.fromJson(obj,
								InformationIndication.class);
						mArrayListInformation.add(cse);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return "error";
				}
				return "ok";
			}

			@Override
			protected void onPostExecute(String result) {
				if (result.equals("ok")) {
					advAdapter = new ImagePagerAdapter(mArrayListInformation,
							getSherlockActivity());

					head = LayoutInflater.from(getSherlockActivity()).inflate(
							R.layout.viewpager_indicator, null);
					RelativeLayout rl = (RelativeLayout) head
							.findViewById(R.id.viewpager_relayout);
					LayoutParams params = rl.getLayoutParams();
					params.width = StaticData.ScreenWidth;
					params.height = params.width * 14 / 29;
					advViewPager = (AutoScrollViewPager) head
							.findViewById(R.id.home_fragment_viewpager);
					advViewPager.setAdapter(advAdapter);
					advViewPager
							.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
					mIndicator = (CirclePageIndicator) head
							.findViewById(R.id.home_fragment_circlepageindicator);
					mIndicator.setViewPager(advViewPager);
					mListView.addHeaderView(head);
					// ((JingxuanAdapter) mPullLoadArrayAdaper).setUpdate();
					advAdapter.notifyDataSetChanged();
					startEx();
				}
				super.onPostExecute(result);
			}
		}.execute();
	}

	public void initData() {
		mPullToRefreshAttacher.setRefreshableView(mListView, this);
		mLoadingFooter = new LoadingFooter(getActivity());
		View header = new View(getActivity());
		mListView.addHeaderView(header);
		mListView.addFooterView(mLoadingFooter.getView());
		// mAdapter = newAdapter();
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

	private int count;

	private void startEx() {
		mAsyncTask1 = new AsyncTask<String, String, String>() {

			String json = null;
			JSONObject mJSONObject;

			@Override
			protected String doInBackground(String... params) {
				try {
					json = HttpUtils.get(Urls.getJingXuanUrl(1));
					try {
						mJSONObject = new JSONObject(json);
						JSONArray localJSONArray1 = mJSONObject
								.getJSONArray("160120");// 160120
						JingXuan mJingXuan = new JingXuan();
						mJingXuan.totalpage = mJSONObject.getJSONObject(
								"totalpage").getInt("totalpage");
						count = mJingXuan.totalpage;
						mJingXuan.mArrayListData = new ArrayList<JingXuan.Data>();
						mJingXuan.mArrayListData_ = new ArrayList<JingXuan.Data>();
						for (int i = 0; i < localJSONArray1.length(); i++) {
							JingXuan.Data data = mJingXuan.new Data();
							data.author = localJSONArray1.getJSONObject(i)
									.getString("author");
							data.pic_url = localJSONArray1.getJSONObject(i)
									.getString("pic_url");
							data.title = localJSONArray1.getJSONObject(i)
									.getString("title");
							data.web_url = localJSONArray1.getJSONObject(i)
									.getString("web_url");
							data.date = localJSONArray1.getJSONObject(i)
									.getString("date");
							data.doc_id = localJSONArray1.getJSONObject(i)
									.getString("doc_id");
							mJingXuan.mArrayListData.add(data);
						}
						JSONArray localJSONArray2 = mJSONObject
								.getJSONArray("280280");// 160120
						for (int i = 0; i < localJSONArray2.length(); i++) {
							JingXuan.Data data = mJingXuan.new Data();
							data.pic_url = localJSONArray2.getJSONObject(i)
									.getString("pic_url");
							data.title = localJSONArray2.getJSONObject(i)
									.getString("title");
							data.web_url = localJSONArray2.getJSONObject(i)
									.getString("web_url");
							data.doc_id = localJSONArray2.getJSONObject(i)
									.getString("doc_id");
							mJingXuan.mArrayListData_.add(data);
						}
						mArrayList.add(mJingXuan);
					} catch (JSONException e) {
						e.printStackTrace();
						return "error";
					}
				} catch (IOException e) {
					e.printStackTrace();
					return "error";
				}
				return "ok";
			}

			protected void onPostExecute(String result) {
				if (result.equals("ok")) {
					mDataItemCount = count;
					mPagination = new Pagination(mArrayList.size(),
							mDataItemCount, 20);
					setRefreshMode(mPagination);
					mPullLoadArrayAdaper.addAll(mArrayList);
					mPullLoadArrayAdaper.notifyDataSetChanged();
				}
			};
		}.execute();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mPullToRefreshAttacher.getHeaderTransformer().onConfigurationChanged(
				getActivity());
	}

	protected void addArrayListData(JingXuan response) {

	};

	protected String getRefDataUrl(int page, int size) {
		return Urls.getJingXuanUrl(page);
	}

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
		final String url = getRefDataUrl(mPagination.page
				+ (nextPage == true ? 1 : 0), mPagination.size);
		if (nextPage) {
			mPagination.page += 1;
		}
		new AsyncTask<String, String, String>() {

			String json = null;
			JSONObject mJSONObject;

			@Override
			protected String doInBackground(String... params) {
				try {
					mArrayList.clear();
					json = HttpUtils.get(url);
				} catch (IOException e) {
					e.printStackTrace();
					return "error";
				}
				return json;
			}

			protected void onPostExecute(String result) {
				if (!result.equals("error")) {
					if (!nextPage) {
						mPullToRefreshAttacher.setRefreshComplete();
						mPullLoadArrayAdaper.clear();
						mArrayList.clear();
					} else {
						mArrayList.clear();
					}
					try {
						mJSONObject = new JSONObject(json);
						JSONArray localJSONArray1 = mJSONObject
								.getJSONArray("160120");// 160120
						JingXuan mJingXuan = new JingXuan();
						mJingXuan.totalpage = mJSONObject.getJSONObject(
								"totalpage").getInt("totalpage");
						count = mJingXuan.totalpage;
						mJingXuan.mArrayListData = new ArrayList<JingXuan.Data>();
						mJingXuan.mArrayListData_ = new ArrayList<JingXuan.Data>();
						for (int i = 0; i < localJSONArray1.length(); i++) {
							JingXuan.Data data = mJingXuan.new Data();
							data.author = localJSONArray1.getJSONObject(i)
									.getString("author");
							data.pic_url = localJSONArray1.getJSONObject(i)
									.getString("pic_url");
							data.title = localJSONArray1.getJSONObject(i)
									.getString("title");
							data.web_url = localJSONArray1.getJSONObject(i)
									.getString("web_url");
							data.date = localJSONArray1.getJSONObject(i)
									.getString("date");
							data.doc_id = localJSONArray1.getJSONObject(i)
									.getString("doc_id");
							mJingXuan.mArrayListData.add(data);
						}
						JSONArray localJSONArray2 = mJSONObject
								.getJSONArray("280280");// 160120
						for (int i = 0; i < localJSONArray2.length(); i++) {
							JingXuan.Data data = mJingXuan.new Data();
							data.pic_url = localJSONArray2.getJSONObject(i)
									.getString("pic_url");
							data.title = localJSONArray2.getJSONObject(i)
									.getString("title");
							data.web_url = localJSONArray2.getJSONObject(i)
									.getString("web_url");
							data.doc_id = localJSONArray2.getJSONObject(i)
									.getString("doc_id");
							mJingXuan.mArrayListData_.add(data);
						}
						mArrayList.add(mJingXuan);
					} catch (JSONException e) {
						e.printStackTrace();
						if (!nextPage) {
							mPullToRefreshAttacher.setRefreshComplete();
						}
						return;
					}
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
			};
		}.execute();
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