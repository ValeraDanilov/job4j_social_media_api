package ru.job4j.social_media_api.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.social_media_api.model.Image;
import ru.job4j.social_media_api.model.Post;
import ru.job4j.social_media_api.repository.ImageRepository;
import ru.job4j.social_media_api.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ImageService {

    private ImageRepository imageRepository;
    private PostRepository postRepository;

    /**
     * Associates a list of images with a post identified by its ID.
     *
     * @param images The list of images to associate with the post.
     * @param postId The ID of the post to which the images will be associated.
     */
    public void create(List<Image> images, int postId) {
        Optional<Post> post = this.postRepository.findById(postId);
        post.ifPresent(value -> images.forEach(image -> image.setPost(value)));
        this.imageRepository.saveAll(images);
    }
}
