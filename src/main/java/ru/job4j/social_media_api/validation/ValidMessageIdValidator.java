package ru.job4j.social_media_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import ru.job4j.social_media_api.repository.MessageRepository;

@AllArgsConstructor
public class ValidMessageIdValidator implements ConstraintValidator<ValidMessageId, String> {

    private final MessageRepository messageRepository;

    @Override
    public boolean isValid(String messageId, ConstraintValidatorContext context) {
        if (messageId == null || !messageId.matches("\\d+")) {
            return false;
        }
        int userIdInt = Integer.parseInt(messageId);
        if (userIdInt < 1) {
            return false;
        }
        return this.messageRepository.findById(userIdInt).isPresent();
    }
}
