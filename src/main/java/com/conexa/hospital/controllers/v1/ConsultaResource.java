package com.conexa.hospital.controllers.v1;

import com.conexa.hospital.representations.consulta.ConsultaRequestRepresentation;
import com.conexa.hospital.representations.consulta.ConsultaResponseRepresentation;
import com.conexa.hospital.services.ConsultaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/consultas")
public class ConsultaResource {

    @Autowired
    private ConsultaService consultaService;
    
    @PostMapping("nova_consulta")
    public ResponseEntity<ConsultaResponseRepresentation> addConsulta(@RequestHeader (name="Authorization") String token,
                                                                      @RequestBody ConsultaRequestRepresentation request) {
        ConsultaResponseRepresentation consultaRepresentation = null;
        token = token.replace("Bearer ", "");

        try {
            consultaRepresentation = this.consultaService.addConsulta(token, request);
        } catch (Exception e) {
            return new ResponseEntity<ConsultaResponseRepresentation>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        
        return new ResponseEntity<ConsultaResponseRepresentation>(consultaRepresentation, HttpStatus.CREATED);
    }

}
