package ru.job4j.social_media_api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.NonNull;
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
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/{messageId}")
    public ResponseEntity<Message> findById(@PathVariable("messageId")
                                            @ValidMessageId
                                            String messageId) {
        return this.messageService.findById(Integer.parseInt(messageId))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<Message> findAll(@PathVariable("userId")
                                 @ValidUserId
                                 String userId, Pageable pageable) {
        return this.messageService.findAll(Integer.parseInt(userId), pageable);
    }

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

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid Message message) {
        if (this.messageService.update(message)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

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
