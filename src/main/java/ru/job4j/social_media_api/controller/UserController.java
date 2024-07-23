package ru.job4j.social_media_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.job4j.social_media_api.model.User;
import ru.job4j.social_media_api.service.UserService;
import ru.job4j.social_media_api.validation.ValidUserId;

import java.util.List;

@Validated
@AllArgsConstructor
@RestController
@Tag(name = "UserController", description = "UserController management APIs")
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Retrieve all Users",
            description = "Get a list of all User objects in the system.",
            tags = {"User", "find"}
    )
    @ApiResponse(responseCode = "200", description = "List of all users retrieved successfully", content = @Content)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAll() {
        return this.userService.findAll();
    }

    @Operation
            (
                    summary = "Retrieve a User by userId",
                    description = "Get a User object by specifying its userId. The response is User object with userId, username, email and password.",
                    tags = {"User", "get"}
            )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{userId}")
    public ResponseEntity<User> get(@PathVariable("userId")
                                    @ValidUserId
                                    int userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation
            (
                    summary = "Retrieve all subscribers for a User by userId",
                    description = "Get a list of User objects who are subscribers of the specified userId."
                            + " The response is a list of User objects with userId, username, email, and password.",
                    tags = {"User", "get"}
            )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
                    {@Content(array = @ArraySchema(schema = @Schema(implementation = User.class)), mediaType = "application/json")})
    })
    @GetMapping("/follower/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAllSubscribers(@PathVariable("userId")
                                         @ValidUserId
                                         int userId) {
        return this.userService.findAllSubscribers(userId);
    }

    @Operation
            (
                    summary = "Retrieve all friends for a User by userId",
                    description = "Get a list of User objects who are friends of the specified userId."
                            + " The response is a list of User objects with userId, username, email, and password.",
                    tags = {"User", "get"}
            )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
                    {@Content(array = @ArraySchema(schema = @Schema(implementation = User.class)), mediaType = "application/json")})
    })
    @GetMapping("/friend/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAllFriends(@PathVariable("userId")
                                     @ValidUserId
                                     int userId) {
        return this.userService.findAllFriends(userId);
    }

    @Operation
            (
                    summary = "Find a User by username and password",
                    description = "Find a User object by the specified username and password. If the user is found,"
                            + " return the User object with userId, username, email and password; otherwise return User not found.",
                    tags = {"User", "get"}
            )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
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

    @Operation
            (
                    summary = "Update an existing User",
                    description = "Update an existing User with the provided details."
                            + " Returns a 200 OK response code if the User was updated successfully, or a 404 Not Found response code if the User does not exist.",
                    tags = {"User", "update"}
            )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping
    public ResponseEntity<Void> update(@Valid @RequestBody User user) {
        if (this.userService.updateUser(user)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation
            (
                    summary = "Change details of an existing User",
                    description = "Change details of an existing User with the provided data. Returns a 200 OK response code if the User details were changed successfully."
            )
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean change(@Valid @RequestBody User user) {
        return this.userService.updateUser(user);
    }

    @Operation
            (
                    summary = "Remove user by ID", description = "Deletes a user based on the provided user ID"
            )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeById(@PathVariable("userId")
                                           @ValidUserId
                                           int userId) {
        if (this.userService.deleteUser(userId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
