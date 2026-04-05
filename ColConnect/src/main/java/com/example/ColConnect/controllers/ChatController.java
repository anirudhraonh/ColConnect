package com.example.ColConnect.controllers;

import com.example.ColConnect.models.ChatMessage;
import com.example.ColConnect.models.ChatMessageDTO;
import com.example.ColConnect.models.User;
import com.example.ColConnect.repo.ChatRepository;
import com.example.ColConnect.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class ChatController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatRepository chatRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;


    public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/chat")
    public String chatPage(){
        return "ConversationPage";
    }

    @MessageMapping("/chat.send")
    public void send(ChatMessageDTO chatMessageDTO){

        //load users
        User sender = userRepository.findById(chatMessageDTO.getSenderId()).orElseThrow();
        User receiver = userRepository.findById(chatMessageDTO.getReceiverId()).orElseThrow();

        //save msg
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(sender);
        chatMessage.setReceiver(receiver);
        chatMessage.setContent(chatMessageDTO.getContent());
        chatMessage.setTimestamp(LocalDateTime.now());

        chatRepository.save(chatMessage);

        //send msg to receiver only
        simpMessagingTemplate.convertAndSendToUser(receiver.getName(),"/queue/messages",chatMessageDTO);
    }
}
