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

    /**
     * Retrieves a message by its unique identifier.
     *
     * @param messageId the unique identifier of the message to be retrieved
     * @return an Optional containing the message if found, or an empty Optional if not found
     */
    public Optional<Message> findById(int messageId) {
        return this.messageRepository.findById(messageId);
    }

    /**
     * Retrieves a paginated list of messages for a specified user, ordered by creation date in descending order.
     *
     * @param userId   the unique identifier of the user whose messages are to be retrieved
     * @param pageable the pagination information
     * @return a page of messages belonging to the specified user, ordered by creation date in descending order
     */
    public Page<Message> findAll(int userId, Pageable pageable) {
        return this.messageRepository.findAllByUserIdOrderByCreatedDesc(userId, pageable);
    }

    /**
     * Creates a new message and saves it to the repository.
     *
     * @param message the message object to be created and saved
     */
    public void create(Message message) {
        this.messageRepository.save(message);
    }

    /**
     * Updates an existing message if it exists.
     *
     * @param message the message object containing updated information
     * @return true if the message was successfully updated, false otherwise
     */
    public boolean update(Message message) {
        Optional<Message> findMessage = this.messageRepository.findById(message.getId());
        if (findMessage.isPresent()) {
            this.messageRepository.save(message);
            return true;
        }
        return false;
    }

    /**
     * Deletes a message by its unique identifier.
     *
     * @param messageId the unique identifier of the message to be deleted
     * @return true if the message was successfully deleted, false otherwise
     */
    public boolean delete(int messageId) {
        return this.messageRepository.deleteById(messageId);
    }
}
