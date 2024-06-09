package ru.job4j.social_media_api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "friend_requests")
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Schema(description = "The unique identifier of the friend request")
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    @Schema(description = "The user who sent the friend request")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @Schema(description = "The user who received the friend request")
    private User receiver;

    @Schema(description = "The status of the friend request (true for accepted, false for pending)")
    private boolean status;
}
