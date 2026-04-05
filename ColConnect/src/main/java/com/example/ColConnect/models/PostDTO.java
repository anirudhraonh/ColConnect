package com.example.ColConnect.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class PostDTO {
    private String title;
    private String text;
    private MultipartFile image; // uploaded file
}