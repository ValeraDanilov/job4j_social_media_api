package ru.job4j.social_media_api.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.social_media_api.model.Image;
import ru.job4j.social_media_api.model.Post;
import ru.job4j.social_media_api.model.User;
import ru.job4j.social_media_api.repository.ImageRepository;
import ru.job4j.social_media_api.repository.PostRepository;
import ru.job4j.social_media_api.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {

    private static final String EXCEPTION = "Post not found";
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    /**
     * Finds all posts by user ID.
     *
     * @param user The user to find posts for.
     * @return A list of posts belonging to the user.
     * @throws IllegalArgumentException If the user is not found.
     */
    public List<Post> findAllPostsByUserId(User user) {
        Optional<User> findUser = this.userRepository.findById(user.getId());
        if (findUser.isPresent()) {
            return this.postRepository.findAllPostsByUserId(findUser.get().getId());
        }
        throw new IllegalArgumentException("User not found");
    }

    /**
     * Finds all posts created between the specified start and finish dates.
     *
     * @param startDate The start date to search for posts.
     * @param finishDate The finish date to search for posts.
     * @return A list of posts created between the start and finish dates.
     * @throws IllegalArgumentException If no posts are found within the specified date range.
     */
    public List<Post> findAllByCreatedBetween(LocalDateTime startDate, LocalDateTime finishDate) {
        List<Post> findPosts = this.postRepository.findAllByCreatedBetween(startDate, finishDate);
        if (!findPosts.isEmpty()) {
            return findPosts;
        }
        throw new IllegalArgumentException(EXCEPTION);
    }

    /**
     * Retrieves a page of posts ordered by creation date in descending order.
     *
     * @param pageable The pagination information.
     * @return A page of posts ordered by creation date in descending order.
     * @throws IllegalArgumentException If no posts are found.
     */
    public Page<Post> findAllByOrderByCreatedDesc(Pageable pageable) {
        Page<Post> findPosts = this.postRepository.findAllByOrderByCreatedDesc(pageable);
        if (!findPosts.isEmpty()) {
            return findPosts;
        }
        throw new IllegalArgumentException(EXCEPTION);
    }

    /**
     * Retrieves a page of posts from the subscriptions of the specified user, ordered by creation date in descending order.
     *
     * @param sender The user whose subscriptions' posts are to be retrieved.
     * @param pageable The pagination information.
     * @return A page of posts from the subscriptions of the specified user, ordered by creation date in descending order.
     * @throws IllegalArgumentException If the specified user is not found or if no posts are found.
     */
    public Page<Post> findAllPostFromUserSubscriptions(User sender, Pageable pageable) {
        Optional<User> findUser = this.userRepository.findById(sender.getId());
        if (findUser.isPresent()) {
            Page<Post> findPosts = this.postRepository.findAllPostFromUserSubscriptions(findUser.get(), pageable);
            if (!findPosts.isEmpty()) {
                return findPosts;
            }
            throw new IllegalArgumentException(EXCEPTION);
        }
        throw new IllegalArgumentException("User not found");
    }

    /**
     * create a new Post.
     *
     * @param post The post to be created.
     */
    @Transactional
    public void create(Post post) {
        this.postRepository.save(post);
    }

    /**
     * Update the post title, description, and associated images.
     *
     * @param post  The post with updated title and description
     * @param image The list of images to be updated
     * @throws IllegalArgumentException if the post is not found
     */
    @Transactional
    public void updatePostAndImage(Post post, List<Image> image) {
        if (findByPost(post)) {
            this.postRepository.updatePostTitleAndDescription(post.getTitle(), post.getDescription(), post.getId());
            if (!image.isEmpty() && this.imageRepository.findImageByPostId(post.getId()).isPresent()) {
                image.forEach(img -> this.imageRepository.updateImage(img.getName(), img.getImageData(), img.getId()));
            }
        }
    }

    /**
     * Deletes a post.
     *
     * @param post the post to delete
     * @throws IllegalArgumentException if the post is not found
     */
    @Transactional
    public void delete(Post post) {
        if (findByPost(post)) {
            if (this.imageRepository.findImageByPostId(post.getId()).isPresent()) {
                this.imageRepository.deleteImagesByPostId(post.getId());
            }
            this.postRepository.delete(post);
        }
    }

    /**
     * Deletes an image from a post.
     *
     * @param post  The post from which the image should be deleted.
     * @param image The image to be deleted from the post.
     * @throws IllegalArgumentException if the post is not found
     */
    @Transactional
    public void deleteImageFromPost(Post post, List<Image> image) {
        if (findByPost(post) && !(image.isEmpty())
                && this.imageRepository.findImageByPostId(post.getId()).isPresent()) {
            image.forEach(img -> this.imageRepository.deleteImageFromPost(post.getId(), img.getId()));
        }
    }

    /**
     * Checks if a post exists in the database.
     *
     * @param post The post to check.
     * @return True if the post exists, false otherwise.
     * @throws IllegalArgumentException If the post is not found.
     */
    private boolean findByPost(Post post) {
        if (this.postRepository.findById(post.getId()).isPresent()) {
            return true;
        }
        throw new IllegalArgumentException(EXCEPTION);
    }
}
