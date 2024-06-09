package ru.job4j.social_media_api.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "The unique identifier of the post")
    private int id;

    @NotBlank(message = "Title should not be empty")
    @Length(min = 10, message = "Title should not be less than 10 characters")
    @Schema(description = "Title of the entity", example = "I have questions")
    private String title;

    @NotBlank(message = "Description should not be empty")
    @Length(min = 10, message = "Description should not be less than 10 characters")
    @Schema(description = "Description of the entity", example = "This is a mediator entity")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Schema(description = "The user associated with the entity")
    private User user;

    @Schema(description = "The date and time when the post was created")
    private LocalDateTime created = LocalDateTime.now();
}
