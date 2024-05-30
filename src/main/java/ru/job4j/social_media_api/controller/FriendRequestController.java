package ru.job4j.social_media_api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.job4j.social_media_api.model.FriendRequest;
import ru.job4j.social_media_api.service.FriendRequestService;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/request")
public class FriendRequestController {

    private final FriendRequestService service;

    @GetMapping("/{id}")
    public Optional<FriendRequest> findById(@PathVariable("id") int id) {
        return service.findById(id);
    }

    @PutMapping("/{requestId}/{userId}")
    public boolean deleteFriendAndKeepFollower(@PathVariable("requestId") int requestId, @PathVariable("userId") int userId) {
        return this.service.deleteFriendAndKeepFollower(requestId, userId);
    }

    @DeleteMapping("/{requestId}/{userId}")
    public boolean deleteRequest(@PathVariable("requestId") int requestId, @PathVariable("userId") int userId) {
        return this.service.deleteRequest(requestId, userId);
    }

    @PostMapping
    public void sendRequest(@RequestBody FriendRequest sendRequest) {
        this.service.sendRequest(sendRequest);
    }

    @GetMapping("/accept/{requestId}/{userId}")
    public boolean acceptRequest(@PathVariable("requestId") int requestId, @PathVariable("userId") int userId) {
        return this.service.acceptRequest(requestId, userId);
    }
}
