package com.activemq.demo.controller;

import com.activemq.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    private final MessageService messageService;
    @Autowired
    public ChatController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public String sendMessage(String message) {
        messageService.sendMessage("chat.messages", message);
        return message;
    }
}
