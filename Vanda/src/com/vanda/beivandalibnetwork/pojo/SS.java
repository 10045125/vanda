package com.vanda.beivandalibnetwork.pojo;

import java.util.ArrayList;

public class SS {
	public Totalpage totalpage;
	public Forumname forumname;
	public int cid;
	public ArrayList<List> list;

	public class Totalpage {
		public int totalpage;
		/*
		 * "totalpage": { "totalpage": 537 },
		 */
	}

	public class Forumname {
		public String forumname;
		/*
		 * "forumname": { "forumname": "生活摄影" },
		 */
	}

	public class List {
		public String date;
		public String title;
		public String pic_url;
		public String detail_url;
		public String web_url;
		public String doc_url;
		public int comment_page_num;
		public int comments_num;
		public String more_comment_url;
		public String doc_id;
		/*
		 * "date": "1415931936", "title": "------ 风 吹 芦 苇 ------", "pic_url":
		 * "http://img3.fengniao.com/ipad/803/153/32110416_220165.jpg",
		 * "detail_url":
		 * "http://api.fengniao.com/app_ipad/pic_bbs_detail.php?id=3523753&fid=115"
		 * , "web_url": "http://bbs.fengniao.com/forum/3523753.html"
		 */
	}
}
