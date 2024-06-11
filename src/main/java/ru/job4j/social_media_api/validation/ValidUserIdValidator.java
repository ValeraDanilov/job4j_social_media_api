package ru.job4j.social_media_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import ru.job4j.social_media_api.repository.UserRepository;

@AllArgsConstructor
public class ValidUserIdValidator implements ConstraintValidator<ValidUserId, Integer> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(Integer userId, ConstraintValidatorContext context) {
        if (userId == null) {
            return false;
        }
        if (userId < 1) {
            return false;
        }
        return this.userRepository.findById(userId).isPresent();
    }
}
