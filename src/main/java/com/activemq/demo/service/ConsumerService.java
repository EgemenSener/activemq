package com.activemq.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class.getName());

    @Value("${spring.activemq.queue}")
    String queue;

    @JmsListener(destination = "myQueue")
    @SendTo("myQueue2")
    public String receiveAndForwardMessageFromQueue(final Message jsonMessage) throws JMSException, InterruptedException {
        String messageData = null;
        System.out.println("Received message " + jsonMessage);
        if (jsonMessage instanceof TextMessage textMessage) {
            messageData = textMessage.getText();
            System.out.println("messageData:" + messageData);
            try {
                // 2 saniye timeout
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.error("Exception ", e);
                throw new InterruptedException(e.getMessage());
            }
        }
        return messageData;
    }

    @JmsListener(destination = "myTopic")
    @SendTo("myNewTopic")
    public String receiveAndForwardMessageFromTopic(final Message jsonMessage) throws JMSException, JsonProcessingException {
        String messageData = null;
        System.out.println("Received message " + jsonMessage);
        if(jsonMessage instanceof TextMessage textMessage) {
            messageData = textMessage.getText();
            System.out.println("messageData:"+messageData);
        }
        return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(messageData);
    }


    @JmsListener(destination = "myTopic")
    public void receiveMessageFromTopic(final Message jsonMessage) throws JMSException {
        System.out.println("Received message in 2nd topic " + jsonMessage);
        if(jsonMessage instanceof TextMessage textMessage) {
            String messageData = textMessage.getText();
            System.out.println("messageData in 2nd listener:"+messageData);
        }
    }

}
