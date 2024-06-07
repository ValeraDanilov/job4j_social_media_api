package ru.job4j.social_media_api.service;


import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.job4j.social_media_api.model.Message;
import ru.job4j.social_media_api.repository.MessageRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Optional<Message> findById(int messageId) {
        return this.messageRepository.findById(messageId);
    }

    public Page<Message> findAll(int userId, Pageable pageable) {
        return this.messageRepository.findAllByUserIdOrderByCreatedDesc(userId, pageable);
    }

    public void create(Message message) {
        this.messageRepository.save(message);
    }

    public boolean update(Message message) {
        Optional<Message> findMessage = this.messageRepository.findById(message.getId());
        Message updateMessage = null;
        if (findMessage.isPresent()) {
            updateMessage = this.messageRepository.save(message);
        }
        return updateMessage == null;
    }

    public boolean delete(int messageId) {
        Optional<Message> findMessage = this.messageRepository.findById(messageId);
        if (findMessage.isPresent()) {
            this.messageRepository.delete(findMessage.get());
            return true;
        }
        return false;
    }
}
