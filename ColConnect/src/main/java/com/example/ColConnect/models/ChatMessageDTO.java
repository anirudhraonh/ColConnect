package com.example.ColConnect.models;

import lombok.Data;

@Data
public class ChatMessageDTO {
    Long senderId;
    Long receiverId;
    String content;
}
