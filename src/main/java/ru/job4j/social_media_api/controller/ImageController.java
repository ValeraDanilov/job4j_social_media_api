package ru.job4j.social_media_api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.social_media_api.model.Image;
import ru.job4j.social_media_api.service.ImageService;
import ru.job4j.social_media_api.validation.ValidPostId;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/image")
@AllArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/{postId}")
    public ResponseEntity<List<Image>> create(@RequestBody List<Image> images,
                                              @PathVariable("postId")
                                              @ValidPostId
                                              String postId) {
        this.imageService.create(images, Integer.parseInt(postId));
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(("/{id}"))
                .buildAndExpand(images)
                .toUri();

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(uri)
                .body(images);
    }
}
