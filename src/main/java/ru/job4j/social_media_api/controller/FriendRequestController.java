package ru.job4j.social_media_api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.social_media_api.model.FriendRequest;
import ru.job4j.social_media_api.service.FriendRequestService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/request")
public class FriendRequestController {

    private final FriendRequestService service;

    @GetMapping("/{id}")
    public ResponseEntity<FriendRequest> findById(@PathVariable("id") int id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{requestId}/{userId}")
    public ResponseEntity<Void> deleteFriendAndKeepFollower(@PathVariable("requestId") int requestId, @PathVariable("userId") int userId) {
        if (this.service.deleteFriendAndKeepFollower(requestId, userId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{requestId}/{userId}")
    public ResponseEntity<Void> deleteRequest(@PathVariable("requestId") int requestId, @PathVariable("userId") int userId) {
        if (this.service.deleteRequest(requestId, userId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<FriendRequest> sendRequest(@RequestBody FriendRequest sendRequest) {
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
    public ResponseEntity<Void> acceptRequest(@PathVariable("requestId") int requestId, @PathVariable("userId") int userId) {
        if (this.service.acceptRequest(requestId, userId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
