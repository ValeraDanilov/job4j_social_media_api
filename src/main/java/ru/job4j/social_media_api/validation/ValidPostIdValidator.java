package ru.job4j.social_media_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import ru.job4j.social_media_api.repository.PostRepository;

@AllArgsConstructor
public class ValidPostIdValidator implements ConstraintValidator<ValidPostId, String> {

    private final PostRepository postRepository;

    @Override
    public boolean isValid(String postId, ConstraintValidatorContext context) {
        if (postId == null || !postId.matches("\\d+")) {
            return false;
        }
        int postIdInt = Integer.parseInt(postId);
        if (postIdInt < 1) {
            return false;
        }
        return this.postRepository.findById(postIdInt).isPresent();
    }
}
