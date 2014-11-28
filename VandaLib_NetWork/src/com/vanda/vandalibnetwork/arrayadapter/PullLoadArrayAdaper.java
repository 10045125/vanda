package com.vanda.vandalibnetwork.arrayadapter;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class PullLoadArrayAdaper<T> extends ArrayAdapter<T> {

	protected boolean pullLoad;

	public boolean isPullLoad() {
		return pullLoad;
	}

	public void setPullLoad(boolean pullLoad) {
		this.pullLoad = pullLoad;
	}

	public PullLoadArrayAdaper(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	public PullLoadArrayAdaper(Context context, int resource,
			int textViewResourceId, List<T> objects) {
		super(context, resource, textViewResourceId, objects);
	}

	public PullLoadArrayAdaper(Context context, int resource,
			int textViewResourceId, T[] objects) {
		super(context, resource, textViewResourceId, objects);
	}

	public PullLoadArrayAdaper(Context context, int resource,
			int textViewResourceId) {
		super(context, resource, textViewResourceId);
	}

	public PullLoadArrayAdaper(Context context, int textViewResourceId,
			List<T> objects) {
		super(context, textViewResourceId, objects);
	}

	public PullLoadArrayAdaper(Context context, int textViewResourceId,
			T[] objects) {
		super(context, textViewResourceId, objects);
	}
}
