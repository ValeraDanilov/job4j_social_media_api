package ru.job4j.social_media_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private int id;

    @NotBlank(message = "Username should not be empty")
    @Length(min = 3, max = 20, message = "Username should be between 3 and 20 characters")
    private String username;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email should not be empty")
    private String email;
}
