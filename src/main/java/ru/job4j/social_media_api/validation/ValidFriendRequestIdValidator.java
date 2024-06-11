package ru.job4j.social_media_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import ru.job4j.social_media_api.repository.FriendRequestRepository;

@AllArgsConstructor
public class ValidFriendRequestIdValidator implements ConstraintValidator<ValidFriendRequestId, Integer> {

    private final FriendRequestRepository friendRequestRepository;

    @Override
    public boolean isValid(Integer requestId, ConstraintValidatorContext constraintValidatorContext) {
        if (requestId == null) {
            return false;
        }
        if (requestId < 1) {
            return false;
        }
        return this.friendRequestRepository.findById(requestId).isPresent();
    }
}
