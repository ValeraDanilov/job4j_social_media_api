package ru.job4j.social_media_api.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidFriendRequestIdValidator.class)
public @interface ValidFriendRequestId {
    String message() default "FriendRequest is not found";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
