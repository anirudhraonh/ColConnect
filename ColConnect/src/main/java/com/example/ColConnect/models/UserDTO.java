package com.example.ColConnect.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UserDTO {

    Long id;
    String profilePic_url;

    @NotBlank(message = "Username cannot be empty!")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters.")
    String username;
    @NotBlank(message = "Password cannot be empty!")
    @Size(min = 8, max = 20,message = "Password needs to be 8 to 20 characters long!")
    String password;
    @NotBlank(message = "Need an email id.")
    String email;

    String college;
    String dept;
    List<String> interests;
    MultipartFile profilePicture;
}
