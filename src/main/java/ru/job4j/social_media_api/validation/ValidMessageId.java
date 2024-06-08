package ru.job4j.social_media_api.validation;

import jakarta.validation.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidMessageIdValidator.class)

public @interface ValidMessageId {
    String message() default "Message is not found";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
