package ru.job4j.social_media_api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.job4j.social_media_api.model.User;
import ru.job4j.social_media_api.service.UserService;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> findAll() {
        return this.userService.findAll();
    }

    @GetMapping("/{userId}")
    public List<User> findAllSubscribers(@PathVariable("userId") int userId) {
        return this.userService.findAllSubscribers(userId);
    }

    @GetMapping("/friend/{userId}")
    public List<User> findAllFriends(@PathVariable("userId") int userId) {
        return this.userService.findAllFriends(userId);
    }

    @GetMapping("/find")
    public Optional<User> findByUsernameAndPassword(@RequestParam String username, @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return this.userService.findByUsernameAndPassword(user);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return this.userService.create(user);
    }

    @PutMapping
    public boolean update(@RequestBody User user) {
        return this.userService.updateUser(user);
    }

    @PatchMapping
    public boolean change(@RequestBody User user) {
        return this.userService.updateUser(user);
    }

    @DeleteMapping("/{userId}")
    public boolean removeById(@PathVariable("userId") int userId) {
        return this.userService.deleteUser(userId);
    }
}
