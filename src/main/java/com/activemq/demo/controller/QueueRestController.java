package com.activemq.demo.controller;

import com.activemq.demo.entity.Employee;
import com.activemq.demo.service.ProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/queue")
public class QueueRestController {

    @Value("${spring.activemq.queue}")
    String queue;

    private final ProducerService producerService;
    @Autowired
    public QueueRestController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/sendToQueue")
    public void sendToQueue() throws JsonProcessingException {
        Employee employee = new Employee("Micheal", "Jackson", 10000L, new Date(), 24);
        String jsonObj = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(employee);
        producerService.sendToQueue(queue, 100, jsonObj);
    }

    @PostMapping("/sendToTopic")
    public void sendToTopic() throws JsonProcessingException {
        producerService.sendToTopic();
    }
}
