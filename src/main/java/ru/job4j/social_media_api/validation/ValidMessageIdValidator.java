package ru.job4j.social_media_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import ru.job4j.social_media_api.repository.MessageRepository;

@AllArgsConstructor
public class ValidMessageIdValidator implements ConstraintValidator<ValidMessageId, Integer> {

    private final MessageRepository messageRepository;

    @Override
    public boolean isValid(Integer messageId, ConstraintValidatorContext context) {
        if (messageId == null) {
            return false;
        }
        if (messageId < 1) {
            return false;
        }
        return this.messageRepository.findById(messageId).isPresent();
    }
}
