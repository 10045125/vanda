package com.vanda.beivandalibnetwork.pojo;

public enum FragmentIds {
	popular("Popular"), everyone("Everyone"), debuts("sunnsoft"), following(
			"Following"), likes("Likes");
	private String mDisplayName;

	FragmentIds(String displayName) {
		mDisplayName = displayName;
	}

	public String getDisplayName() {
		return mDisplayName;
	}
}
