package ru.job4j.social_media_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "PostController", description = "PostController management APIs")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(
            summary = "Retrieve a Post by postId",
            description = "Get a Post object by specifying its postId."
                    + " The response is a Post object with postId, title, description, a User object, created and images.",
            tags = {"Post", "get"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of the Post",
                    content = {@Content(schema = @Schema(implementation = PostAndImagesDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/{postId}")
    public ResponseEntity<PostAndImagesDTO> findById(@PathVariable("postId")
                                                     @ValidPostId
                                                     String postId) {
        return this.postService.findById(Integer.parseInt(postId))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Retrieve all Posts ordered by creation date in descending order",
            description = "Get a list of Post objects ordered by creation date in descending order. "
                    + "The response is a PagedResponseDTO containing the list of Post objects with postId, title, description, a User object, created and images.",
            tags = {"Post", "get"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of Posts",
                    content = {@Content(schema = @Schema(implementation = PagedResponseDTO.class), mediaType = "application/json")})
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedResponseDTO<PostAndImagesDTO> findAllByOrderByCreatedDesc(@NonNull Pageable pageable) {
        return this.postService.findAllByOrderByCreatedDesc(pageable);
    }

    @Operation(
            summary = "Retrieve all Posts by User Id",
            description = "Get a list of Post objects associated with the specified user ID. "
                    + "The response is a list of Post objects with postId, title, description, a User object, created and images..",
            tags = {"Post", "get"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of Posts by User Id",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = PostAndImagesDTO.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<PostAndImagesDTO> findAllPostsByUserId(@PathVariable("userId")
                                                       @ValidUserId
                                                       String userId) {
        return this.postService.findAllPostsByUserId(Integer.parseInt(userId));
    }

    @Operation(
            summary = "Retrieve Posts created between specified dates",
            description = "Get a list of Post objects created between the specified start and finish dates. "
                    + "The response is a list of Post objects with postId, title, description, a User object, created and images.",
            tags = {"Post", "get"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of Posts created between dates",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = PostAndImagesDTO.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid date format or missing parameters", content = @Content)
    })
    @GetMapping("/date")
    @ResponseStatus(HttpStatus.OK)
    public List<PostAndImagesDTO> findAllByCreatedBetween(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") @NonNull LocalDateTime startDate,
            @RequestParam("finishDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") @NonNull LocalDateTime finishDate) {
        return this.postService.findAllByCreatedBetween(startDate, finishDate);
    }

    @Operation(
            summary = "Retrieve Posts from User Subscriptions",
            description = "Get a paginated list of Post objects from the subscriptions of the specified user. "
                    + "The response includes postId, title, content, and images.",
            tags = {"Post", "get"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of Posts from user subscriptions",
                    content = {@Content(schema = @Schema(implementation = PagedResponseDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "User not found", content = @Content)
    })
    @GetMapping("/subscriptions/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public PagedResponseDTO<PostAndImagesDTO> findAllPostFromUserSubscriptions(@PathVariable("userId")
                                                                               @ValidUserId
                                                                               String userId, Pageable pageable) {
        return this.postService.findAllPostFromUserSubscriptions(Integer.parseInt(userId), pageable);
    }

    @Operation(
            summary = "Create a new Post",
            description = "Create a new Post object with associated images.",
            tags = {"Post", "create"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully",
                    content = {@Content(schema = @Schema(implementation = PostAndImagesDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid request body or parameters", content = @Content)
    })
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

    @Operation(
            summary = "Update an existing Post with associated images",
            description = "Update an existing Post object with associated images.",
            tags = {"Post", "update"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post updated successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content)
    })
    @PutMapping
    public ResponseEntity<Void> updatePostAndImage(@RequestBody @NonNull PostAndImagesDTO postAndImagesDto) {
        if (this.postService.updatePostAndImage(postAndImagesDto)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Delete a Post by ID",
            description = "Delete a Post object by its ID.",
            tags = {"Post", "delete"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Post deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content)
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable("postId")
                                       @ValidPostId
                                       String postId) {
        if (this.postService.delete(Integer.parseInt(postId))) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Delete Images from a Post by ID",
            description = "Delete specific images from a Post object by its ID.",
            tags = {"Post", "delete"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Images deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Post not found or images not found", content = @Content)
    })
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
