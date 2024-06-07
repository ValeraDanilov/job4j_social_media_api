package ru.job4j.social_media_api.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.social_media_api.model.Message;
import ru.job4j.social_media_api.service.MessageService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/{messageId}")
    public ResponseEntity<Message> findById(@PathVariable("messageId") int messageId) {
        return this.messageService.findById(messageId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<Message> findAll(@PathVariable("userId") int userId, Pageable pageable) {
        return this.messageService.findAll(userId, pageable);
    }

    @PostMapping
    public ResponseEntity<Message> create(Message message) {
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
    public ResponseEntity<Void> update(Message message) {
        if (this.messageService.update(message)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(int messageId) {
        if (this.messageService.delete(messageId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
