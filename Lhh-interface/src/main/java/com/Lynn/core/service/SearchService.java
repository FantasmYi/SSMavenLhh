package com.Lynn.core.service;

import cn.itcast.common.page.Pagination;
import com.Lynn.core.bean.product.Product;
import org.apache.solr.client.solrj.SolrServerException;

import java.util.List;

public interface SearchService {
	
	
	//全文检索
	//全文检索
	public Pagination selectPaginationByQuery(Integer pageNo,String keyword,Long brandId, String price) throws Exception;
	//保存商品到solr服务器
	public void insertProductToSolr(Long id);
}
