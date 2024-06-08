package ru.job4j.social_media_api.controller;

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
public class FriendRequestController {

    private final FriendRequestService service;

    @GetMapping("/{id}")
    public ResponseEntity<FriendRequest> findById(@PathVariable("id")
                                                  @ValidFriendRequestId
                                                  String id) {
        return service.findById(Integer.parseInt(id))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{requestId}/{userId}")
    public ResponseEntity<Void> deleteFriendAndKeepFollower(@PathVariable("requestId")
                                                            @ValidFriendRequestId
                                                            String requestId,
                                                            @PathVariable("userId")
                                                            @ValidUserId
                                                            String userId) {
        if (this.service.deleteFriendAndKeepFollower(Integer.parseInt(requestId), Integer.parseInt(userId))) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{requestId}/{userId}")
    public ResponseEntity<Void> deleteRequest(@PathVariable("requestId")
                                              @ValidFriendRequestId
                                              String requestId,
                                              @PathVariable("userId")
                                              @ValidUserId
                                              String userId) {
        if (this.service.deleteRequest(Integer.parseInt(requestId), Integer.parseInt(userId))) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

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

    @GetMapping("/accept/{requestId}/{userId}")
    public ResponseEntity<Void> acceptRequest(@PathVariable("requestId")
                                              @ValidFriendRequestId
                                              String requestId,
                                              @PathVariable("userId")
                                              @ValidUserId
                                              String userId) {
        if (this.service.acceptRequest(Integer.parseInt(requestId), Integer.parseInt(userId))) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
