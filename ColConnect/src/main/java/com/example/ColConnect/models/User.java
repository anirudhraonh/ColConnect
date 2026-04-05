package com.example.ColConnect.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String pwd;
    String email;

    String college;
    String dept;
    List<String> interests;
    String profilePicture_url;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Post> posts;

    public User(String name, String pwd){
        this.name = name;
        this.pwd = pwd;
    }
}
