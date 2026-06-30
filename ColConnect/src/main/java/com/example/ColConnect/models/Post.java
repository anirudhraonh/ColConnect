package com.example.ColConnect.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    String title;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    String text;
    String image_url;
    LocalDateTime createdAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    List<Comment> commentList;


    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}
