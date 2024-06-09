package ru.job4j.social_media_api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "images")
@ToString
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Schema(description = "The unique identifier of the image")
    private int id;

    @Schema(description = "The name of the image")
    private String name;

    @Schema(description = "Binary data of the image")
    private byte[] img;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @Schema(description = "The post to which the image belongs")
    private Post post;
}
