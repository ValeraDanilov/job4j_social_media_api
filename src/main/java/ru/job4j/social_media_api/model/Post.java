package ru.job4j.social_media_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "posts")
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @NotBlank(message = "Title should not be empty")
    @Length(min = 10, message = "Title should not be less than 10 characters")
    private String title;

    @NotBlank(message = "Description should not be empty")
    @Length(min = 10, message = "Description should not be less than 10 characters")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDateTime created = LocalDateTime.now();
}
