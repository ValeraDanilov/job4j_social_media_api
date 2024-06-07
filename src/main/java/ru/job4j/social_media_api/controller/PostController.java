package ru.job4j.social_media_api.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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
    public ResponseEntity<PostAndImagesDTO> findById(@PathVariable("postId") int postId) {
        return this.postService.findById(postId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedResponseDTO<PostAndImagesDTO> findAllByOrderByCreatedDesc(Pageable pageable) {
        return this.postService.findAllByOrderByCreatedDesc(pageable);
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<PostAndImagesDTO> findAllPostsByUserId(@PathVariable("userId") int userId) {
        return this.postService.findAllPostsByUserId(userId);
    }

    @GetMapping("/date")
    @ResponseStatus(HttpStatus.OK)
    public List<PostAndImagesDTO> findAllByCreatedBetween(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") LocalDateTime startDate,
            @RequestParam("finishDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") LocalDateTime finishDate) {
        return this.postService.findAllByCreatedBetween(startDate, finishDate);
    }

    @GetMapping("/subscriptions/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public PagedResponseDTO<PostAndImagesDTO> findAllPostFromUserSubscriptions(@PathVariable("userId") int userId, Pageable pageable) {
        return this.postService.findAllPostFromUserSubscriptions(userId, pageable);
    }

    @PostMapping
    public ResponseEntity<PostAndImagesDTO> create(@RequestBody PostAndImagesDTO postAndImagesDto) {
        this.postService.create(postAndImagesDto);
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(postAndImagesDto.getPost().getId())
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(uri)
                .body(postAndImagesDto);
    }

    @PutMapping
    public ResponseEntity<Void> updatePostAndImage(@RequestBody PostAndImagesDTO postAndImagesDto) {
        if (this.postService.updatePostAndImage(postAndImagesDto)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable("postId") int postId) {
        if (this.postService.delete(postId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("image/{postId}")
    public ResponseEntity<Void> deleteImageFromPost(@PathVariable("postId") int postId, @RequestBody List<Image> images) {
        if (this.postService.deleteImageFromPost(postId, images)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
