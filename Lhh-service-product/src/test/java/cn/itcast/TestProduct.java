package cn.itcast;

import com.Lynn.core.bean.product.Product;
import com.Lynn.core.dao.product.ProductDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by FantasmYii on 2018/3/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class TestProduct {
    @Autowired
    private ProductDao productDao;

    @Test
    public void testadd() {
        Product product=productDao.selectByPrimaryKey(441L);
        System.out.println(product);
    }
}
