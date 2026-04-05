package com.example.ColConnect.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class ChatMessage {

    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    Conversation conversation;

    @ManyToOne
    User sender;

    @ManyToOne
    User receiver;

    String content;

    LocalDateTime timestamp;
}
