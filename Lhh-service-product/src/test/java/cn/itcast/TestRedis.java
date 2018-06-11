package cn.itcast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

/**
 * Created by FantasmYii on 2018/3/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class TestRedis {

    @Autowired
    private Jedis jedis;
    @Test
    public void testJedis(){
        jedis.set("ooo","aaa");
    }


    @Test
    public void test(){
        Jedis jedis=new Jedis("192.168.200.128",6379);
        Long pno=jedis.incr("pno");
        System.out.println("pno:"+pno);
        jedis.close();
    }
}
