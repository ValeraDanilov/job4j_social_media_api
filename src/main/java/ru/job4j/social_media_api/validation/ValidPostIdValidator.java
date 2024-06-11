package ru.job4j.social_media_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import ru.job4j.social_media_api.repository.PostRepository;

@AllArgsConstructor
public class ValidPostIdValidator implements ConstraintValidator<ValidPostId, Integer> {

    private final PostRepository postRepository;

    @Override
    public boolean isValid(Integer postId, ConstraintValidatorContext context) {
        if (postId == null) {
            return false;
        }
        if (postId < 1) {
            return false;
        }
        return this.postRepository.findById(postId).isPresent();
    }
}
