package ru.job4j.social_media_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import ru.job4j.social_media_api.repository.FriendRequestRepository;

@AllArgsConstructor
public class ValidFriendRequestIdValidator implements ConstraintValidator<ValidFriendRequestId, String> {

    private final FriendRequestRepository friendRequestRepository;

    @Override
    public boolean isValid(String requestId, ConstraintValidatorContext constraintValidatorContext) {
        if (requestId == null || !requestId.matches("\\d+")) {
            return false;
        }
        int intRequestId = Integer.parseInt(requestId);
        if (intRequestId < 1) {
            return false;
        }
        return this.friendRequestRepository.findById(intRequestId).isPresent();
    }
}
