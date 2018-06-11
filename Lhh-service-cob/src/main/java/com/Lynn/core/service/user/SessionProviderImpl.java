package com.Lynn.core.service.user;

import com.Lynn.common.web.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * 将用户名保存到solr中
 * Created by FantasmYii on 2018/4/17.
 */
@Service("sessionProvider")
public class SessionProviderImpl implements SessionProvider {

    @Autowired
    private Jedis jedis;
    private Integer exp = 30;

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    @Override
    public void setAttribuerForUsername(String name, String value) {
        //用户名
        jedis.set(name + ":" + Constants.USER_NAME, value);
        //过期时间
        jedis.expire(name + ":" + Constants.USER_NAME, 60 * exp);
    }

    //从redis中取出用户名
    @Override
    public String getAttributeForUsername(String name) {
        String value = jedis.get(name + ":" + Constants.USER_NAME);
        if (value != null) {
            //过期时间从最后一次访问后开始计算，则每次访问时都要将过期时间重置一下
            jedis.expire(name + ":" + Constants.USER_NAME, 60 * exp);
        }
        return value;
    }
}
