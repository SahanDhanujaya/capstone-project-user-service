package com.dhanux.userservice.validation;

/**
 * @author Dhanujaya(Dhanu)
 * @TimeStamp 22/03/2026 23:46
 * @ProjectDetails user-service
 */


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ValidImageValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImage {

    String message() default "Only image files are allowed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
