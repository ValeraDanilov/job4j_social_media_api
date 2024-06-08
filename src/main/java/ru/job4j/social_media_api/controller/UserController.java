package ru.job4j.social_media_api.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.social_media_api.model.User;
import ru.job4j.social_media_api.service.UserService;
import ru.job4j.social_media_api.validation.ValidUserId;

import java.util.List;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAll() {
        return this.userService.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> get(@PathVariable("userId")
                                    @ValidUserId
                                    String userId) {
        return userService.getUserById(Integer.parseInt(userId))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/follower/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAllSubscribers(@PathVariable("userId")
                                         @ValidUserId
                                         String userId) {
        return this.userService.findAllSubscribers(Integer.parseInt(userId));
    }

    @GetMapping("/friend/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAllFriends(@PathVariable("userId")
                                     @ValidUserId
                                     String userId) {
        return this.userService.findAllFriends(Integer.parseInt(userId));
    }

    @GetMapping("/find")
    public ResponseEntity<User> findByUsernameAndPassword(@RequestParam
                                                          @NonNull
                                                          @Length(min = 3, max = 20, message = "Username should be between 3 and 20 characters")
                                                          String username,
                                                          @RequestParam
                                                          @NonNull
                                                          @Length(min = 6, message = "Password should not be less than 6 characters")
                                                          String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return this.userService.findByUsernameAndPassword(user)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        this.userService.create(user);
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(uri)
                .body(user);
    }

    @PutMapping
    public ResponseEntity<Void> update(@Valid @RequestBody User user) {
        if (this.userService.updateUser(user)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean change(@Valid @RequestBody User user) {
        return this.userService.updateUser(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeById(@PathVariable("userId")
                                           @ValidUserId
                                           String userId) {
        if (this.userService.deleteUser(Integer.parseInt(userId))) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
