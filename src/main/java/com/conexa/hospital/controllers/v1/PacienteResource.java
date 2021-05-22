package com.conexa.hospital.controllers.v1;

import java.util.List;

import com.conexa.hospital.representations.paciente.PacienteRepresentation;
import com.conexa.hospital.services.PacienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/pacientes")
public class PacienteResource {

    @Autowired
    private PacienteService service;
    
    @GetMapping("")
    public ResponseEntity<List<PacienteRepresentation>> pacientes() {
        List<PacienteRepresentation> pacienteRepresentations = this.service.retrievePacientes();
        return ResponseEntity.ok(pacienteRepresentations);
    }

    @GetMapping("{id}")
    public ResponseEntity<PacienteRepresentation> paciente(@PathVariable(name="id") Integer id) {
        PacienteRepresentation pacienteRepresentation = this.service.retrievePacienteRepresentation(id);
        return ResponseEntity.ok(pacienteRepresentation);
    }

    @PostMapping("")
    public ResponseEntity<PacienteRepresentation> create(@RequestBody PacienteRepresentation representation) {
        PacienteRepresentation pacienteRepresentation = this.service.create(representation);
        return new ResponseEntity<PacienteRepresentation>(pacienteRepresentation, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<PacienteRepresentation> update(@PathVariable(name="id") Integer id,
                                                         @RequestBody PacienteRepresentation representation) {
        PacienteRepresentation pacienteRepresentation = this.service.update(id, representation);
        return ResponseEntity.ok(pacienteRepresentation);                                                  
    }

}
