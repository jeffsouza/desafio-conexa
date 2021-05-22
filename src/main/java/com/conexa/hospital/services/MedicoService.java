package com.conexa.hospital.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.conexa.hospital.models.Medico;
import com.conexa.hospital.repositories.MedicoRepository;
import com.conexa.hospital.representations.medico.MedicoRepresentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    public MedicoRepresentation retrieveMedicoRepresentation(int id) {
        Optional<Medico> medico = this.medicoRepository.findById(id);

        if (!medico.isPresent()) {
            throw new EntityNotFoundException();
        }

        return MedicoRepresentation.fromMedico(medico.get());
    }

    public Medico retrieveMedico(int id) {
        Optional<Medico> medico = this.medicoRepository.findById(id);

        if (!medico.isPresent()) {
            throw new EntityNotFoundException();
        }

        return medico.get();
    }
    
}
