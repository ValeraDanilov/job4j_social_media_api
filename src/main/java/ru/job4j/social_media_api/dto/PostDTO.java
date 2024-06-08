package ru.job4j.social_media_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private int id;

    @NotBlank(message = "Title should not be empty")
    @Length(min = 10, message = "Title should not be less than 10 characters")
    private String title;

    @NotBlank(message = "Description should not be empty")
    @Length(min = 10, message = "Description should not be less than 10 characters")
    private String description;

    private UserDTO user;
    private LocalDateTime created = LocalDateTime.now();
}
