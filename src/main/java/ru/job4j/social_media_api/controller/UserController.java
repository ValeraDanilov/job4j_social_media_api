package ru.job4j.social_media_api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.social_media_api.model.User;
import ru.job4j.social_media_api.service.UserService;
import java.util.List;


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
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAllSubscribers(@PathVariable("userId") int userId) {
        return this.userService.findAllSubscribers(userId);
    }

    @GetMapping("/friend/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAllFriends(@PathVariable("userId") int userId) {
        return this.userService.findAllFriends(userId);
    }

    @GetMapping("/find")
    public ResponseEntity<User> findByUsernameAndPassword(@RequestParam String username, @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return this.userService.findByUsernameAndPassword(user)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
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
    public ResponseEntity<Void> update(@RequestBody User user) {
        if (this.userService.updateUser(user)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean change(@RequestBody User user) {
        return this.userService.updateUser(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeById(@PathVariable("userId") int userId) {
        if (this.userService.deleteUser(userId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
