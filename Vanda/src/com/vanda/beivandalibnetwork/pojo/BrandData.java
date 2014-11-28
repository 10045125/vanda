package com.vanda.beivandalibnetwork.pojo;

import java.util.ArrayList;

public class BrandData {
	public int prodId;
	public String prodname;
	public double salePrice;
	public double parPrice;
	public int hot;
	public int stock;
	public int totalOrders;
	public int totalSale;
	public String imgUrl;
	public int free_deliver;

	public class BrandDataFilter {
		public int count;
		public ArrayList<BrandData> records;
	}

	public class Brand {
		public BrandDataFilter data;
	}

}
