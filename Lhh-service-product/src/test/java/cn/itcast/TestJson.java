package cn.itcast;

import com.Lynn.core.bean.user.Buyer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.StringWriter;

/**
 * 测试购物车部分json
 * Created by FantasmYii on 2018/3/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class TestJson {

    @Test
    public void testjson() throws IOException {
        Buyer buyer=new Buyer();
        buyer.setUsername("Lynn");
        ObjectMapper objectMapper=new ObjectMapper();
        //null不能转
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        StringWriter writer=new StringWriter();
        objectMapper.writeValue(writer,buyer);
        System.out.println(writer.toString());
        //转回对象
        Buyer buyer1=objectMapper.readValue(writer.toString(),Buyer.class);
        System.out.println(buyer1);
    }
}
