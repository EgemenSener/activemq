package com.activemq.demo.service;

import com.activemq.demo.entity.Department;
import com.activemq.demo.entity.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProducerService {

    @Value("${spring.activemq.topic}")
    String topic;

    @Value("${spring.activemq.queue}")
    String queue;
    private final JmsTemplate jmsTemplate;
    @Autowired
    public ProducerService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendToQueue() {
        for (int i = 0; i < 200; i++) {
            try {
                Employee employee = new Employee("Micheal", "Jackson", 10000L, new Date(), 24 + i);
                String jsonObj = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(employee);
                jmsTemplate.send(queue, messageCreator -> {
                    TextMessage message = messageCreator.createTextMessage();
                    message.setText(jsonObj);
                    return message;
                });
                System.out.println("Sent message: " + jsonObj);
            } catch (Exception ex) {
                System.out.println("ERROR in sending message to queue: " + ex.getMessage());
            }
        }
    }

    public void sendToTopic() {
        try {
            Department department = new Department(1,"HR",100);
            String jsonObj = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(department);
            jmsTemplate.send(topic, messageCreator -> {
                TextMessage message = messageCreator.createTextMessage();
                message.setText(jsonObj);
                return message;
            });
        }
        catch (Exception ex) {
            System.out.println("ERROR in sending message to queue");
        }
    }

}
