package com.vanda.beivandalibnetwork.url;

import android.annotation.SuppressLint;

@SuppressLint("DefaultLocale")
public class Urls {
	public static final String IMAGE_URL_HEAD = "http://123.57.40.206:6888";
	public static final String DATA_URL_HEAD = "http://123.57.40.206:10010";
	public static final String BRANDFILTER_URL_DATA = DATA_URL_HEAD
			+ "/filterpage/filterprod?compid=%d&recmnd_tp=%d&cat2_id=%d&free_deliver=%d&isnew=%d&ishot=%d&isasc=%d&isdesc=%d&maxorder=%d&maxprice=%d&minprice=%d&page=%d&size=%d";

	// http://192.168.0.100:10010/filterpage/filterprod?compid=0&recmnd_tp=0&cat2_id=0&free_deliver=0&isnew=0
	// & ishot=0 & isasc=0& isdesc= 0& maxorder=0& maxprice=0&minprice=0&
	// page=1&size=20
	public static String getBrandFilterUrlData(int compid, int recmnd_tp,
			int cat2_id, int free_deliver, int isnew, int ishot, int isasc,
			int isdesc, int maxorder, int maxprice, int minprice, int page,
			int fetch) {
		return String.format(BRANDFILTER_URL_DATA, compid, recmnd_tp, cat2_id,
				free_deliver, isnew, ishot, isasc, isdesc, maxorder, maxprice,
				minprice, page, fetch);
	}

	public static final String SS_URL = "http://api.fengniao.com/app_ipad/pic_bbs_list_v2.php?cid=-1&fid=%d&page=%d";

	public static final String getSSUrl(int fid, int page) {
		return String.format(SS_URL, fid, page);
	}

	public static final String DASHI_URL = "http://api.fengniao.com/app_ipad/pic_zutu_list.php?cid=%d&fid=-1&page=%d";

	public static final String getDaShiUrl(int flag, int page) {
		return String.format(DASHI_URL, flag, page);
	}

	public static final String INFORMATION_URL = "http://api.fengniao.com/app_ipad/news_list.php?cid=%d&page=%d";

	public static final String getInformationUrl(int cid, int page) {
		return String.format(INFORMATION_URL, cid, page);
	}

	public static final String INFORMATION_IMAGE_URL = "http://api.fengniao.com/app_ipad/focus_pic.php?mid=%d";

	public static final String getInformationImageUrl(int mid) {
		return String.format(INFORMATION_IMAGE_URL, mid);
	}

	public static final String JINGXUAN_URL = "http://api.fengniao.com/app_ipad/news_jingxuan.php?page=%d";

	public static final String getJingXuanUrl(int page) {
		return String.format(JINGXUAN_URL, page);
	}
}
