package ru.job4j.social_media_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import ru.job4j.social_media_api.repository.UserRepository;

@AllArgsConstructor
public class ValidUserIdValidator implements ConstraintValidator<ValidUserId, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String userId, ConstraintValidatorContext context) {
        if (userId == null || !userId.matches("\\d+")) {
            return false;
        }
        int userIdInt = Integer.parseInt(userId);
        if (userIdInt < 1) {
            return false;
        }
        return this.userRepository.findById(userIdInt).isPresent();
    }
}
