package com.conexa.hospital;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.conexa.hospital.exceptions.ModelValidationException;
import com.conexa.hospital.models.Paciente;
import com.conexa.hospital.repositories.PacienteRepository;
import com.conexa.hospital.representations.paciente.PacienteRepresentation;
import com.conexa.hospital.services.PacienteService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PacienteTests {

	@InjectMocks
	private PacienteService service;

	@Mock
	private PacienteRepository repository;

	@Test
	void contextLoads() {
	}

	@Test
	void getAllPacientes_andReturnsListOfObj() {

		Paciente p1 = new Paciente(1, "Paciente 1", "000.000.000-00", 20, "(22) 99999-9999");
		Paciente p2 = new Paciente(2, "Paciente 2", "000.000.000-00", 20, "(22) 99999-9999");
		Paciente p3 = new Paciente(3, "Paciente 3", "000.000.000-00", 20, "(22) 99999-9999");
		List<Paciente> pacientes = new ArrayList<Paciente>();
		Collections.addAll(pacientes, p1, p2, p3);

		when(repository.findAll()).thenReturn(pacientes);

		assertEquals(service.retrievePacientes().size(), 3);
	}

	@Test
	void getPaciente_andReturnsObj() {

		Paciente paciente = new Paciente(1, "Paciente 1", "000.000.000-00", 20, "(22) 99999-9999");
		when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(paciente));

		assertEquals(service.retrievePaciente(paciente.getId()), paciente);
	}

	@Test
	void createPaciente_andReturnsCreatedObj() {

		Paciente paciente = new Paciente(1, "Paciente 1", "000.000.000-00", 20, "(22) 99999-9999");
		PacienteRepresentation pacienteRepresentation = PacienteRepresentation.fromPaciente(paciente);
		when(repository.save(Mockito.any(Paciente.class))).thenReturn(paciente);

		assertEquals(service.create(pacienteRepresentation).getId(), pacienteRepresentation.getId());
		assertEquals(service.create(pacienteRepresentation).getNome(), pacienteRepresentation.getNome());
		assertEquals(service.create(pacienteRepresentation).getCpf(), pacienteRepresentation.getCpf());
		assertEquals(service.create(pacienteRepresentation).getIdade(), pacienteRepresentation.getIdade());
		assertEquals(service.create(pacienteRepresentation).getTelefone(), pacienteRepresentation.getTelefone());
	}

	@Test
	void createInvalidPaciente_andReturnsError() {

		Paciente paciente = new Paciente(1, null, "000.000.000-00", 20, "(22) 99999-9999");
		PacienteRepresentation pacienteRepresentation = PacienteRepresentation.fromPaciente(paciente);
		when(repository.save(Mockito.any(Paciente.class))).thenReturn(paciente);

		assertThrows(ModelValidationException.class, () -> {
            service.create(pacienteRepresentation);
        });
	}

	@Test
	void updatePaciente_andReturnsObj() {

		Paciente oldPaciente = new Paciente(1, "Paciente 1", "000.000.000-00", 20, "(22) 99999-9999");
		Paciente newPaciente = new Paciente(1, "Paciente 2", "000.000.000-00", 20, "(22) 99999-9999");
		PacienteRepresentation newPacienteRepresentation = PacienteRepresentation.fromPaciente(newPaciente);
		when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(oldPaciente));
		when(repository.save(Mockito.any(Paciente.class))).thenReturn(newPaciente);

		assertEquals(service.update(oldPaciente.getId(), newPacienteRepresentation).getNome(),
			newPacienteRepresentation.getNome());
	}

}
