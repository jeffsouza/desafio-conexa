package com.conexa.hospital.services;

import java.util.List;
import java.util.Optional;

import com.conexa.hospital.exceptions.ResourceNotFoundException;
import com.conexa.hospital.models.Paciente;
import com.conexa.hospital.properties.MessageProperties;
import com.conexa.hospital.repositories.PacienteRepository;
import com.conexa.hospital.representations.paciente.PacienteRepresentation;
import com.conexa.hospital.services.utils.ModelValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository repository;

    @Autowired
    private MessageProperties properties;
    
    public List<PacienteRepresentation> retrievePacientes() {
        return PacienteRepresentation.fromPaciente(this.repository.findAll());
    }

    public PacienteRepresentation retrievePacienteRepresentation(int id) {
        Optional<Paciente> paciente = this.repository.findById(id);

        if (!paciente.isPresent()) {
            throw new ResourceNotFoundException(properties.resourceNotFound());
        }
        
        return PacienteRepresentation.fromPaciente(paciente.get());
    }

    public Paciente retrievePaciente(int id) {
        Optional<Paciente> paciente = this.repository.findById(id);

        if (!paciente.isPresent()) {
            throw new ResourceNotFoundException(properties.resourceNotFound());
        }
        
        return paciente.get();
    }

    public PacienteRepresentation create(PacienteRepresentation pacienteRepresentation) {
        Paciente paciente = pacienteRepresentation.toPaciente(new Paciente());
        ModelValidator.validate(paciente);
        return PacienteRepresentation.fromPaciente(repository.save(paciente));
    }

    public PacienteRepresentation update(int id, PacienteRepresentation pacienteRepresentation) {
        Optional<Paciente> paciente = this.repository.findById(id);

        if (!paciente.isPresent()) {
            throw new ResourceNotFoundException(properties.resourceNotFound());
        }

        return paciente
           .map(record -> {
                record = pacienteRepresentation.toPaciente(record);
                ModelValidator.validate(record);
                PacienteRepresentation result = PacienteRepresentation.fromPaciente(repository.save(record));
                return result;
           }).orElse(null);
    }

}
