package com.Lynn.core.message;

import com.Lynn.core.service.SearchService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * 监听处理类
 * Created by FantasmYii on 2018/4/11.
 */
public class CustomMessageListener implements MessageListener{

    @Autowired
    private SearchService searchService;

    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage am= (ActiveMQTextMessage) message;
        try {
            System.out.println("ActiveMq中的消息是solr："+am.getText());
            //此时已经接受到了来自提供方的消息，要将消息发送给消费方
          searchService.insertProductToSolr(Long.parseLong(am.getText()));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
