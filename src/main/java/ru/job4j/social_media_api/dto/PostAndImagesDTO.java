package ru.job4j.social_media_api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostAndImagesDTO {
    private PostDTO post;
    private List<ImageDTO> images;
}
