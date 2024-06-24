package com.activemq.demo.service;

import com.activemq.demo.entity.Department;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {
    private static final Logger logger = LoggerFactory.getLogger(ProducerService.class.getName());

    @Value("${spring.activemq.topic}")
    String topic;

    private final JmsTemplate jmsTemplate;
    @Autowired
    public ProducerService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendToQueue(String destination, Integer count, String message) {
        for (int i = 0; i < count; i++) {
            jmsTemplate.convertAndSend(destination, message);
            logger.info("Sent message: " + message);
        }
    }

    public void sendToTopic() throws JsonProcessingException {
        Department department = new Department(1,"HR",100);
        String jsonObj = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(department);
        jmsTemplate.send(topic, messageCreator -> {
            TextMessage message = messageCreator.createTextMessage();
            message.setText(jsonObj);
            return message;
        });
    }
}
