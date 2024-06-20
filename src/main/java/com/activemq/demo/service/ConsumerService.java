package com.activemq.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;

@Component
public class ConsumerService {

    @Value("${spring.activemq.queue}")
    String queue;

    @JmsListener(destination = "myQueue", containerFactory = "myFactory")
    @SendTo("myQueue2")
    public String receiveAndForwardMessageFromQueue(final Message jsonMessage) throws JMSException {
        String messageData = null;
        System.out.println("Received message " + jsonMessage);
        if (jsonMessage instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) jsonMessage;
            messageData = textMessage.getText();
            System.out.println("messageData:" + messageData);
            try {
                // 2 saniye timeout
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return messageData;
    }

    @JmsListener(destination = "myTopic")
    @SendTo("myNewTopic")
    public String receiveAndForwardMessageFromTopic(final Message jsonMessage) throws JMSException, JsonProcessingException {
        String messageData = null;
        System.out.println("Received message " + jsonMessage);
        if(jsonMessage instanceof TextMessage) {
            TextMessage textMessage = (TextMessage)jsonMessage;
            messageData = textMessage.getText();
            System.out.println("messageData:"+messageData);
        }
        return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(messageData);
    }


    @JmsListener(destination = "myTopic")
    public void receiveMessageFromTopic(final Message jsonMessage) throws JMSException {
        String messageData = null;
        System.out.println("Received message in 2nd topic " + jsonMessage);
        if(jsonMessage instanceof TextMessage) {
            TextMessage textMessage = (TextMessage)jsonMessage;
            messageData = textMessage.getText();
            System.out.println("messageData in 2nd listener:"+messageData);
        }
    }

}
