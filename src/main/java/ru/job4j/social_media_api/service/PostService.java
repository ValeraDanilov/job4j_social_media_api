package ru.job4j.social_media_api.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.social_media_api.dto.*;
import ru.job4j.social_media_api.mapper.ImageMapper;
import ru.job4j.social_media_api.mapper.PostMapper;
import ru.job4j.social_media_api.mapper.UserMapper;
import ru.job4j.social_media_api.model.Image;
import ru.job4j.social_media_api.model.Post;
import ru.job4j.social_media_api.model.User;
import ru.job4j.social_media_api.repository.ImageRepository;
import ru.job4j.social_media_api.repository.PostRepository;
import ru.job4j.social_media_api.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final ImageMapper imageMapper;
    private final UserMapper userMapper;

    /**
     * Retrieves a post and its associated images by the given post ID.
     *
     * @param postId the ID of the post to be retrieved
     * @return an Optional containing a PostAndImagesDTO object with post details and associated images,
     * or an empty Optional if the post with the specified ID is not found
     */
    public Optional<PostAndImagesDTO> findById(int postId) {
        Optional<Post> findPost = this.postRepository.findById(postId);
        return Optional.ofNullable(createPostAndImageDTO(List.of(findPost.get())).get(0));
    }

    /**
     * Retrieves a paginated list of posts with associated images, ordered by creation date in descending order.
     *
     * @param pageable The pagination information for the query.
     * @return A PagedResponseDTO object containing the paginated list of PostAndImagesDTO objects.
     */
    public PagedResponseDTO<PostAndImagesDTO> findAllByOrderByCreatedDesc(Pageable pageable) {
        Page<Post> postsPage = this.postRepository.findAllByOrderByCreatedDesc(pageable);
        List<PostAndImagesDTO> postWithImagesList = createPostAndImageDTO(postsPage.getContent());
        Page<PostAndImagesDTO> postWithImagesPage = new PageImpl<>(postWithImagesList, pageable, postsPage.getTotalElements());
        return new PagedResponseDTO<>(postWithImagesPage);
    }

    /**
     * Retrieves a list of posts and their associated images for a given user ID.
     *
     * @param userId the ID of the user whose posts are to be retrieved
     * @return a list of PostAndImagesDTO objects containing post details and associated images
     */
    public List<PostAndImagesDTO> findAllPostsByUserId(int userId) {
        List<Post> posts = this.postRepository.findAllPostsByUserId(userId);
        return createPostAndImageDTO(posts);
    }

    /**
     * Retrieves a list of posts with associated images created between the specified start and finish dates.
     *
     * @param startDate  The start date for filtering posts.
     * @param finishDate The finish date for filtering posts.
     * @return A list of PostAndImagesDTO objects representing the posts with images created within the specified date range.
     */
    public List<PostAndImagesDTO> findAllByCreatedBetween(LocalDateTime startDate, LocalDateTime finishDate) {
        List<Post> posts = this.postRepository.findAllByCreatedBetween(startDate, finishDate);
        return createPostAndImageDTO(posts);
    }

    /**
     * Retrieves a paginated list of posts with associated images from the subscriptions of a specified user.
     *
     * @param userId   The ID of the user whose subscriptions are used to fetch posts.
     * @param pageable The pagination information for the result set.
     * @return A PagedResponseDTO containing a paginated list of PostAndImagesDTO objects representing posts with images from user subscriptions.
     */
    public PagedResponseDTO<PostAndImagesDTO> findAllPostFromUserSubscriptions(int userId, Pageable pageable) {
        Page<Post> postsPage = this.userRepository.findById(userId)
                .map(user -> this.postRepository.findAllPostFromUserSubscriptions(user, pageable))
                .orElse(Page.empty());
        List<PostAndImagesDTO> postWithImagesList = createPostAndImageDTO(postsPage.toList());
        Page<PostAndImagesDTO> postWithImagesPage = new PageImpl<>(postWithImagesList, pageable, postsPage.getTotalElements());
        return new PagedResponseDTO<>(postWithImagesPage);
    }

    /**
     * Creates a new post along with associated images in the database.
     * <p>
     * This method takes a PostAndImagesDTO object containing information about the post and its associated images.
     * It maps the DTO objects to entity objects, sets up relationships between them, and saves them in the database.
     * If the user associated with the post exists in the database, it sets the user for the post.
     *
     * @param postAndImagesDto The DTO object containing post information and associated images.
     */
    @Transactional
    public void create(PostAndImagesDTO postAndImagesDto) {
        Post post = this.postMapper.getEntityFromDto(postAndImagesDto);
        List<Image> images = this.imageMapper.getEntityFromDtoList(postAndImagesDto);
        images.forEach(image -> image.setPost(post));
        User user = this.postMapper.getEntityFromDto(postAndImagesDto.getPost().getUser());
        Optional<User> findUser = this.userRepository.findById(user.getId());
        findUser.ifPresent(post::setUser);
        this.postRepository.save(post);
        this.imageRepository.saveAll(images);
    }

    /**
     * This method updates an existing post and its associated images in the database.
     * It takes a PostAndImagesDTO object containing the updated information about the post and its images.
     * The method checks if the post exists in the database, updates the post title and description, and also updates any existing images.
     * It returns true if the post and images were successfully updated, otherwise returns false.
     *
     * @param postAndImagesDto The PostAndImagesDTO object containing the updated post and images information
     * @return true if the post and images were successfully updated, false otherwise
     */
    @Transactional
    public boolean updatePostAndImage(PostAndImagesDTO postAndImagesDto) {
        Post post = this.postMapper.getEntityFromDto(postAndImagesDto);
        List<Image> images = this.imageMapper.getEntityFromDtoList(postAndImagesDto);
        Optional<Post> existingPost = this.postRepository.findById(post.getId());
        if (existingPost.isPresent()) {
            this.postRepository.updatePostTitleAndDescription(post.getTitle(), post.getDescription(), post.getId());
            List<Image> existingImages = this.imageRepository.findImageByPostId(post.getId());
            for (Image image : images) {
                Optional<Image> existingImage = existingImages.stream()
                        .filter(img -> img.getId() == image.getId())
                        .findFirst();
                if (existingImage.isPresent()) {
                    this.imageRepository.updateImage(image.getName(), image.getImg(), image.getId());
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Deletes the post with the specified ID from the database and also deletes any associated images.
     * <p>
     * This method first checks if there are any images associated with the post to be deleted. If images are found,
     * they are deleted using the ImageRepository. Then, the post with the given ID is deleted from the database
     * using the PostRepository. This operation is wrapped in a transaction to ensure data consistency.
     *
     * @param postId the ID of the post to delete
     * @return true if the post and associated images were successfully deleted, false otherwise
     */
    @Transactional
    public boolean delete(int postId) {
        if (!this.imageRepository.findImageByPostId(postId).isEmpty()) {
            this.imageRepository.deleteImagesByPostId(postId);
        }
        return this.postRepository.deleteById(postId);
    }

    /**
     * Deletes the specified images from a post.
     * <p>
     * This method deletes the specified images from the post with the given ID. It checks if the list of images
     * to delete is not empty and if there are images associated with the specified post. If the conditions are met,
     * the method deletes each image from the post using the ImageRepository.
     *
     * @param postId the ID of the post from which to delete the images
     * @param images a list of Image objects to delete from the post
     * @return true if the images were successfully deleted, false otherwise
     */
    @Transactional
    public boolean deleteImageFromPost(int postId, List<Image> images) {
        if (!(images.isEmpty())
                && !(this.imageRepository.findImageByPostId(postId).isEmpty())) {
            images.forEach(img -> this.imageRepository.deleteImageFromPost(postId, img.getId()));
            return true;
        }
        return false;
    }

    /**
     * This method creates a list of PostAndImagesDTO objects from a list of Post entities.
     * It iterates through the list of posts, retrieves user information, post details, and associated images for each post,
     * then creates a PostAndImagesDTO object containing the post and its corresponding images.
     *
     * @param posts The list of Post entities to create PostAndImagesDTO objects from
     * @return A list of PostAndImagesDTO objects containing post details and associated images
     */
    private List<PostAndImagesDTO> createPostAndImageDTO(List<Post> posts) {
        List<PostAndImagesDTO> dtoList = new ArrayList<>();
        for (Post post : posts) {
            UserDTO userDTO = this.userMapper.getDtoFromEntity(post.getUser());
            PostDTO postDTO = this.postMapper.getDtoFromEntity(post);
            postDTO.setUser(userDTO);
            List<Image> images = this.imageRepository.findImageByPostId(post.getId());
            List<ImageDTO> imageDTOList = this.imageMapper.getDtoFromEntityList(images);
            dtoList.add(new PostAndImagesDTO(postDTO, imageDTOList));
        }
        return dtoList;
    }
}
