package com.conexa.hospital.services.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.conexa.hospital.exceptions.ModelValidationException;

public class ModelValidator {

    public static void validate(Object model) {
		List<String> errorMessage = new ArrayList<String>();
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(model);

		for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
			errorMessage.add(constraintViolation.getMessage());
		}

		if (errorMessage.size() > 0) {
			throw new ModelValidationException(constraintViolations);
		}
	}

}
