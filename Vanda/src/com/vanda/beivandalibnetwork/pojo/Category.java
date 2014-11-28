package com.vanda.beivandalibnetwork.pojo;

public enum Category {
	information("资讯"), bbs("论坛"), BBSWnt("论坛美图"), dashi("大师作品"), equipment(
			"器材赏析"), set("关于我");
	private String mDisplayName;

	Category(String displayName) {
		mDisplayName = displayName;
	}

	public String getDisplayName() {
		return mDisplayName;
	}
}
