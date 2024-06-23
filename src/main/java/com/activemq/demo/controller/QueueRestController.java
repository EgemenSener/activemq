package com.activemq.demo.controller;

import com.activemq.demo.service.ProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/queue")
public class QueueRestController {

    private final ProducerService producerService;
    @Autowired
    public QueueRestController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/sendToQueue")
    public void sendToQueue() throws JsonProcessingException {
        producerService.sendToQueue();
    }
}
