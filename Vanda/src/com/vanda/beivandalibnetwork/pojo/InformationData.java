package com.vanda.beivandalibnetwork.pojo;

import java.util.ArrayList;

public class InformationData {

	public int cid;
	public ArrayList<Information> list;
	public TotalPage totalpage;

	public class TotalPage {
		public String totalpage;
		// "totalpage": 607
	}

	public class Information {
		public String title;
		public String pic_url;
		public String doc_url;
		public int comment_page_num;
		public int comments_num;
		public String more_comment_url;
		public String doc_id;
		public String web_url;
		/*
		 * "title": "婺源牵手北京电影学院共建影视创作基地", "pic_url":
		 * "http://img2.fengniao.com/article/11_280x210/223/likrjp7elCD2.jpg",
		 * "doc_url":
		 * "http://api.fengniao.com/app_ipad/news_doc.php?docid=4908311&isPad=1"
		 * , "comment_page_num": 0, "comments_num": "0", "more_comment_url":
		 * "http://api.fengniao.com/app_ipad/news_doc_comments.php?docid=4908311&isPad=1"
		 * , "doc_id": "4908311", "web_url":
		 * "http://image.fengniao.com/490/4908311.html"
		 */
	}
}
