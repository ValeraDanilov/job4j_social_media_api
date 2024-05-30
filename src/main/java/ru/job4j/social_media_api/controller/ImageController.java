package ru.job4j.social_media_api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.job4j.social_media_api.model.Image;
import ru.job4j.social_media_api.service.ImageService;

import java.util.List;

@RestController
@RequestMapping("/api/image")
@AllArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/{postId}")
    public void create(@RequestBody List<Image> images, @PathVariable("postId") int postId) {
        this.imageService.create(images, postId);
    }
}
