package com.conexa.hospital.controllers.v1;

import com.conexa.hospital.representations.medico.MedicoAuthRequestRepresentation;
import com.conexa.hospital.representations.medico.MedicoAuthResponseRepresentation;
import com.conexa.hospital.services.MedicoAuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/medicos")
public class MedicoResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MedicoAuthService medicoAuthService;
    
    @PostMapping("authenticate")
    public ResponseEntity<MedicoAuthResponseRepresentation> authenticate(@RequestBody MedicoAuthRequestRepresentation request) throws Exception {
        Authentication auth = new UsernamePasswordAuthenticationToken(request.getUsuario(), request.getSenha());
        auth = authenticationManager.authenticate(auth);

        MedicoAuthResponseRepresentation representation = this.medicoAuthService.authenticate(request.getUsuario());
        
        return ResponseEntity.ok(
            representation
        );
    }

    @DeleteMapping("logout")
    public ResponseEntity<?> logout(@RequestBody MedicoAuthRequestRepresentation request) throws Exception {
       this.medicoAuthService.logout(request.getToken());

       return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
