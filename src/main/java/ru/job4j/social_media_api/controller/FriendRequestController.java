package ru.job4j.social_media_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.social_media_api.model.FriendRequest;
import ru.job4j.social_media_api.service.FriendRequestService;
import ru.job4j.social_media_api.validation.ValidFriendRequestId;
import ru.job4j.social_media_api.validation.ValidUserId;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/request")
@Tag(name = "FriendRequestController", description = "FriendRequestController management APIs")
public class FriendRequestController {

    private final FriendRequestService service;

    @Operation(
            summary = "Retrieve a FriendRequest by ID",
            description = "Get a FriendRequest object by specifying its ID. The response is a FriendRequest object with details.",
            tags = {"FriendRequest", "get"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "FriendRequest retrieved successfully",
                    content = {@Content(schema = @Schema(implementation = FriendRequest.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "FriendRequest not found", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{id}")
    public ResponseEntity<FriendRequest> findById(@PathVariable("id")
                                                  @ValidFriendRequestId
                                                  int id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete a friend relationship and keep the user as a follower",
            description = "Delete a friend relationship based on the FriendRequest ID and keep the specified user as a follower.",
            tags = {"FriendRequest", "delete"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend relationship deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "FriendRequest not found", content = @Content)
    })
    @PutMapping("/{requestId}/{userId}")
    public ResponseEntity<Void> deleteFriendAndKeepFollower(@PathVariable("requestId")
                                                            @ValidFriendRequestId
                                                            int requestId,
                                                            @PathVariable("userId")
                                                            @ValidUserId
                                                            int userId) {
        if (this.service.deleteFriendAndKeepFollower(requestId, userId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Delete a friend request",
            description = "Delete a friend request based on the FriendRequest ID and user ID.",
            tags = {"FriendRequest", "delete"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Friend request deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "FriendRequest not found", content = @Content)
    })
    @DeleteMapping("/{requestId}/{userId}")
    public ResponseEntity<Void> deleteRequest(@PathVariable("requestId")
                                              @ValidFriendRequestId
                                              int requestId,
                                              @PathVariable("userId")
                                              @ValidUserId
                                              int userId) {
        if (this.service.deleteRequest(requestId, userId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Send a FriendRequest",
            description = "Send a FriendRequest by providing the request details. The response contains the sent FriendRequest object.",
            tags = {"FriendRequest", "send"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "FriendRequest sent successfully",
                    content = {@Content(schema = @Schema(implementation = FriendRequest.class), mediaType = "application/json")})
    })
    @PostMapping
    public ResponseEntity<FriendRequest> sendRequest(@RequestBody @NonNull FriendRequest sendRequest) {
        this.service.sendRequest(sendRequest);
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(sendRequest.getId())
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(uri)
                .body(sendRequest);
    }

    @Operation(
            summary = "Accept a FriendRequest",
            description = "Accept a FriendRequest by providing the request ID and user ID. Returns HTTP status 200 OK if the request is accepted.",
            tags = {"FriendRequest", "accept"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "FriendRequest accepted successfully"),
            @ApiResponse(responseCode = "404", description = "FriendRequest not found")
    })
    @GetMapping("/accept/{requestId}/{userId}")
    public ResponseEntity<Void> acceptRequest(@PathVariable("requestId")
                                              @ValidFriendRequestId
                                              int requestId,
                                              @PathVariable("userId")
                                              @ValidUserId
                                              int userId) {
        if (this.service.acceptRequest(requestId, userId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
