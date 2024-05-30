package ru.job4j.social_media_api.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.job4j.social_media_api.dto.PagedResponseDTO;
import ru.job4j.social_media_api.dto.PostAndImagesDTO;
import ru.job4j.social_media_api.model.Image;
import ru.job4j.social_media_api.service.PostService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/post")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{postId}")
    public PostAndImagesDTO findById(@PathVariable("postId") int postId) {
        return this.postService.findById(postId);
    }

    @GetMapping
    public PagedResponseDTO<PostAndImagesDTO> findAllByOrderByCreatedDesc(Pageable pageable) {
        return this.postService.findAllByOrderByCreatedDesc(pageable);
    }

    @GetMapping("/user/{userId}")
    public List<PostAndImagesDTO> findAllPostsByUserId(@PathVariable("userId") int userId) {
        return this.postService.findAllPostsByUserId(userId);
    }

    @GetMapping("/date")
    public List<PostAndImagesDTO> findAllByCreatedBetween(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") LocalDateTime startDate,
            @RequestParam("finishDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") LocalDateTime finishDate) {
        return this.postService.findAllByCreatedBetween(startDate, finishDate);
    }

    @GetMapping("/subscriptions/{userId}")
    public PagedResponseDTO<PostAndImagesDTO> findAllPostFromUserSubscriptions(@PathVariable("userId") int userId, Pageable pageable) {
        return this.postService.findAllPostFromUserSubscriptions(userId, pageable);
    }

    @PostMapping
    public void create(@RequestBody PostAndImagesDTO postAndImagesDto) {
        this.postService.create(postAndImagesDto);
    }

    @PutMapping
    public boolean updatePostAndImage(@RequestBody PostAndImagesDTO postAndImagesDto) {
        return this.postService.updatePostAndImage(postAndImagesDto);
    }

    @DeleteMapping("/{postId}")
    public boolean delete(@PathVariable("postId") int postId) {
        return this.postService.delete(postId);
    }

    @DeleteMapping("image/{postId}")
    public void deleteImageFromPost(@PathVariable("postId") int postId, @RequestBody List<Image> images) {
        this.postService.deleteImageFromPost(postId, images);
    }
}
