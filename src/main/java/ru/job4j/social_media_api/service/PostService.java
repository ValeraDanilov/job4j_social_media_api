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
     * Retrieves the post and associated images by the given post ID.
     *
     * @param postId The ID of the post to retrieve.
     * @return A PostAndImagesDTO object containing the post and its associated images, if found.
     */
    public PostAndImagesDTO findById(int postId) {
        Optional<Post> findPost = this.postRepository.findById(postId);
        PostAndImagesDTO postAndImagesDTO = new PostAndImagesDTO();
        if (findPost.isPresent()) {
            postAndImagesDTO = createPostAndImageDTO(List.of(findPost.get())).get(0);
        }
        return postAndImagesDTO;
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
     * Retrieves a list of posts with associated images created by a specific user.
     *
     * @param userId The ID of the user for whom to retrieve the posts.
     * @return A list of PostAndImagesDTO objects representing the posts with images created by the user.
     */
    public List<PostAndImagesDTO> findAllPostsByUserId(int userId) {
        Optional<User> findUser = this.userRepository.findById(userId);
        List<Post> posts = new ArrayList<>();
        if (findUser.isPresent()) {
            posts = this.postRepository.findAllPostsByUserId(findUser.get().getId());
        }
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
     * This method deletes a post and its associated images from the database based on the provided post ID.
     * It first checks if the post exists in the database, deletes any associated images if present, and then deletes the post itself.
     * Returns true if the post and images were successfully deleted, otherwise returns false.
     *
     * @param postId The ID of the post to be deleted
     * @return true if the post and images were successfully deleted, false otherwise
     */
    @Transactional
    public boolean delete(int postId) {
        Optional<Post> existingPost = this.postRepository.findById(postId);
        if (existingPost.isPresent()) {
            if (!this.imageRepository.findImageByPostId(postId).isEmpty()) {
                this.imageRepository.deleteImagesByPostId(postId);
            }
            this.postRepository.deletePostById(postId);
            return true;
        }
        return false;
    }

    /**
     * This method deletes the specified image(s) from a post based on the provided post ID and list of images.
     * It first checks if the post exists in the database, if the list of images is not empty, and if the images are associated with the post.
     * Then it iterates through the list of images and deletes each image from the post.
     *
     * @param postId The ID of the post from which the images should be deleted
     * @param images The list of images to be deleted from the post
     */
    @Transactional
    public void deleteImageFromPost(int postId, List<Image> images) {
        Optional<Post> existingPost = this.postRepository.findById(postId);
        if (existingPost.isPresent() && !(images.isEmpty())
                && !(this.imageRepository.findImageByPostId(postId).isEmpty())) {
            images.forEach(img -> this.imageRepository.deleteImageFromPost(postId, img.getId()));
        }
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
