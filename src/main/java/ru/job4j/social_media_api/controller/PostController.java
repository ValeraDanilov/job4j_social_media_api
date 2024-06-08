package ru.job4j.social_media_api.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.social_media_api.dto.PagedResponseDTO;
import ru.job4j.social_media_api.dto.PostAndImagesDTO;
import ru.job4j.social_media_api.model.Image;
import ru.job4j.social_media_api.service.PostService;
import ru.job4j.social_media_api.validation.ValidPostId;
import ru.job4j.social_media_api.validation.ValidUserId;

import java.time.LocalDateTime;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/post")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{postId}")
    public ResponseEntity<PostAndImagesDTO> findById(@PathVariable("postId")
                                                     @ValidPostId
                                                     String postId) {
        return this.postService.findById(Integer.parseInt(postId))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedResponseDTO<PostAndImagesDTO> findAllByOrderByCreatedDesc(@NonNull Pageable pageable) {
        return this.postService.findAllByOrderByCreatedDesc(pageable);
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<PostAndImagesDTO> findAllPostsByUserId(@PathVariable("userId")
                                                       @ValidUserId
                                                       String userId) {
        return this.postService.findAllPostsByUserId(Integer.parseInt(userId));
    }

    @GetMapping("/date")
    @ResponseStatus(HttpStatus.OK)
    public List<PostAndImagesDTO> findAllByCreatedBetween(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") @NonNull LocalDateTime startDate,
            @RequestParam("finishDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") @NonNull LocalDateTime finishDate) {
        return this.postService.findAllByCreatedBetween(startDate, finishDate);
    }

    @GetMapping("/subscriptions/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public PagedResponseDTO<PostAndImagesDTO> findAllPostFromUserSubscriptions(@PathVariable("userId")
                                                                               @ValidUserId
                                                                               String userId, Pageable pageable) {
        return this.postService.findAllPostFromUserSubscriptions(Integer.parseInt(userId), pageable);
    }

    @PostMapping
    public ResponseEntity<PostAndImagesDTO> create(@RequestBody @NonNull PostAndImagesDTO postAndImagesDto) {
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
    public ResponseEntity<Void> updatePostAndImage(@RequestBody @NonNull PostAndImagesDTO postAndImagesDto) {
        if (this.postService.updatePostAndImage(postAndImagesDto)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable("postId")
                                       @ValidPostId
                                       String postId) {
        if (this.postService.delete(Integer.parseInt(postId))) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("image/{postId}")
    public ResponseEntity<Void> deleteImageFromPost(@PathVariable("postId")
                                                    @ValidPostId
                                                    String postId, @RequestBody List<Image> images) {
        if (this.postService.deleteImageFromPost(Integer.parseInt(postId), images)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
