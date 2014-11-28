package com.vanda.beivandalibnetwork.pojo;

import java.util.ArrayList;

public class ImageViewPoJo {

	public ArrayList<Data> data;

	public class Data {
		public String date;
		public String doc_title;
		public Pic pic;
	}

	public class Pic {
		public String pic_datil;
		public String gq;
		public String sl;
		public String web_url;
	}
	/*
	 * "date": "2014-11-13", "doc_title": "秋之光影", "pic": { "pic_datil":
	 * "最近拍的几片，整理出来与摄友版主分享！点评！ \n", "gq":
	 * "http://img3.fengniao.com/forum/attachpics/803/131/32106067_1000.jpg",
	 * "sl": "", "web_url":
	 * "http://bbs.fengniao.com/forum/pic/slide_115_3523349_67900649.html" }
	 */

}
