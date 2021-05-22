package com.conexa.hospital;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.conexa.hospital.models.Paciente;
import com.conexa.hospital.repositories.PacienteRepository;
import com.conexa.hospital.services.PacienteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PacienteValidationTests {

	@InjectMocks
	private PacienteService service;

	@Mock
	private PacienteRepository repository;

	private Validator validator;

	@BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

	@Test
	void contextLoads() {
	}

	@Test
	void createPacienteWithoutNome_andReturnsValidationError() {
		Paciente paciente = new Paciente(1, null, "000.000.000-00", 20, "(22) 99999-9999");
		Set<ConstraintViolation<Paciente>> violations = validator.validate(paciente);
		
		assertFalse(violations.isEmpty());
	}

	@Test
	void createPacienteWithoutCpf_andReturnsValidationError() {
		Paciente paciente = new Paciente(1, "Paciente 1", null, 20, "(22) 99999-9999");
		Set<ConstraintViolation<Paciente>> violations = validator.validate(paciente);
		
		assertFalse(violations.isEmpty());
	}

	@Test
	void createPacienteWithInvalidIdade_andReturnsValidationError() {
		Paciente paciente = new Paciente(1, "Paciente 1", "000.000.000-00", 0, "(22) 99999-9999");
		Set<ConstraintViolation<Paciente>> violations = validator.validate(paciente);
		
		assertFalse(violations.isEmpty());
	}

	@Test
	void createPacienteWithoutTelefone_andReturnsValidationError() {
		Paciente paciente = new Paciente(1, "Paciente 1", "000.000.000-00", 20, null);
		Set<ConstraintViolation<Paciente>> violations = validator.validate(paciente);
		
		assertFalse(violations.isEmpty());
	}

	@Test
	void createValidPaciente_andReturnsObj() {
		Paciente paciente = new Paciente(1, "Paciente 1", "000.000.000-00", 20, "(22) 99999-9999");
		Set<ConstraintViolation<Paciente>> violations = validator.validate(paciente);
		
		assertTrue(violations.isEmpty());
	}

}

