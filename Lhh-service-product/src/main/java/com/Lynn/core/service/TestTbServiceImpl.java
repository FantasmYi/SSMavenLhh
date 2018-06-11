package com.Lynn.core.service;


import com.Lynn.core.bean.TestTb;
import com.Lynn.core.dao.TestTbDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by FantasmYii on 2018/3/21.
 */
@Service("testTbService")
@Transactional
public class TestTbServiceImpl implements TestTbService {

	
	@Autowired
	private TestTbDao testTbDao;
	
	public void insertTestTb(TestTb testTb){
		testTbDao.insertTestTb(testTb);
		//throw new RuntimeException();
	}
	
}
