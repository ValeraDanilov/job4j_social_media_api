package ru.job4j.social_media_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.social_media_api.dto.PostAndImagesDTO;
import ru.job4j.social_media_api.model.Image;
import ru.job4j.social_media_api.service.ImageService;
import ru.job4j.social_media_api.validation.ValidPostId;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/image")
@Tag(name = "ImageController", description = "ImageController management APIs")
@AllArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @Operation(
            summary = "Create a new Post",
            description = "Create a new Post object with associated images.",
            tags = {"Post", "create"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully",
                    content = @Content(schema = @Schema(implementation = PostAndImagesDTO.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request body or parameters", content = @Content)
    })
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
