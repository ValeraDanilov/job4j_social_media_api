package ru.job4j.social_media_api.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.job4j.social_media_api.model.Message;
import ru.job4j.social_media_api.service.MessageService;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/{messageId}")
    public Optional<Message> findById(@PathVariable("messageId") int messageId) {
        return this.messageService.findById(messageId);
    }

    @GetMapping("/{userId}")
    public Page<Message> findAll(@PathVariable("userId") int userId, Pageable pageable) {
        return this.messageService.findAll(userId, pageable);
    }

    @PostMapping
    public void create(Message message) {
        this.messageService.create(message);
    }

    @PutMapping
    public boolean update(Message message) {
        return this.messageService.update(message);
    }

    @DeleteMapping
    public void delete(int messageId) {
        this.messageService.delete(messageId);
    }
}
