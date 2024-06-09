package ru.job4j.social_media_api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Schema(description = "The unique identifier of the message")
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    @Schema(description = "The user who sent the message")
    private User user;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @Schema(description = "The user who received the message")
    private User friend;

    @NotBlank(message = "Context should not be empty")
    @Length(min = 2, message = "Context should not be less than 6 characters")
    @Schema(description = "Content of the message", example = "Hello, how are you?")
    private String context;

    @Schema(description = "The date and time when the message was created")
    private LocalDateTime created = LocalDateTime.now();
}
