package ru.job4j.social_media_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.social_media_api.model.Message;
import ru.job4j.social_media_api.service.MessageService;
import ru.job4j.social_media_api.validation.ValidMessageId;
import ru.job4j.social_media_api.validation.ValidUserId;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/message")
@Tag(name = "MessageController", description = "MessageController management APIs")
public class MessageController {

    private final MessageService messageService;

    @Operation(
            summary = "Retrieve a Message by messageId",
            description = "Get a Message object by specifying its messageId. "
                    + "The response is Message object with messageId, the User object, the User object, context, and created.",
            tags = {"Message", "find"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message found successfully",
                    content = @Content(schema = @Schema(implementation = Message.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Message not found", content = @Content)
    })
    @GetMapping("/{messageId}")
    public ResponseEntity<Message> findById(@PathVariable("messageId")
                                            @ValidMessageId
                                            String messageId) {
        return this.messageService.findById(Integer.parseInt(messageId))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Retrieve a Message by messageId",
            description = "Get a Message object by specifying its messageId. "
                    + "The response is Message object with messageId, the User object, the User object, context, and created.",
            tags = {"Message", "find"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message found successfully",
                    content = @Content(schema = @Schema(implementation = Message.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Message not found", content = @Content)
    })
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<Message> findAll(@PathVariable("userId")
                                 @ValidUserId
                                 String userId, Pageable pageable) {
        return this.messageService.findAll(Integer.parseInt(userId), pageable);
    }

    @Operation(
            summary = "Create a new Message",
            description = "Create a new Message object by providing the required details.",
            tags = {"Message", "create"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message created successfully",
                    content = @Content(schema = @Schema(implementation = Message.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid input for creating Message", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Message> create(@RequestBody @Valid Message message) {
        this.messageService.create(message);
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(message.getId())
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(uri)
                .body(message);
    }

    @Operation(
            summary = "Update an existing Message",
            description = "Update an existing Message object by providing the updated details.",
            tags = {"Message", "update"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message updated successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Message not found for updating", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid Message message) {
        if (this.messageService.update(message)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Delete a Message by ID",
            description = "Delete a Message object by providing its ID.",
            tags = {"Message", "delete"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Message deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Message not found for deletion", content = @Content)
    })
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> delete(@PathVariable("messageId")
                                       @ValidMessageId
                                       String messageId) {
        if (this.messageService.delete(Integer.parseInt(messageId))) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
