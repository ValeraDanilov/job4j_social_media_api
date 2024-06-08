package ru.job4j.social_media_api.model;

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
    private int id;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User friend;

    @NotBlank(message = "Context should not be empty")
    @Length(min = 2, message = "Context should not be less than 6 characters")
    private String context;

    private LocalDateTime created = LocalDateTime.now();
}
