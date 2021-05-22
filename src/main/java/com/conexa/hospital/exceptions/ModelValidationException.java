package com.conexa.hospital.exceptions;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class ModelValidationException extends ConstraintViolationException {

    public ModelValidationException(Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(constraintViolations);
    }

}
