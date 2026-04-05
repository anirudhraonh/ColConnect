package com.example.ColConnect.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Conversation {
    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    User user1;

    @ManyToOne
    User user2;
}
