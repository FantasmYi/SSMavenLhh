package cn.itcast;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.io.IOException;

/**
 * Created by FantasmYii on 2018/4/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class TestSolr {

    @Autowired
    private SolrServer solrServer;

    @Test
    public void test() throws IOException, SolrServerException {
        //链接solr服务器,下面这部分交给了spring框架进行管理 solr.xml
      /*  String baseURL = "http://192.168.200.128:8080/solr";
        SolrServer solrServer = new HttpSolrServer(baseURL);*/
        //保存内容

        SolrInputDocument solrInputFields=new SolrInputDocument();
        solrInputFields.setField("id",5);
        solrInputFields.setField("name","Lynn1");
        //添加，提交
        solrServer.add(solrInputFields);
        solrServer.commit();
    }

}
