package com.conexa.hospital.services;

import java.util.List;
import java.util.Optional;

import com.conexa.hospital.exceptions.ResourceNotFoundException;
import com.conexa.hospital.models.Medico;
import com.conexa.hospital.models.MedicoAuth;
import com.conexa.hospital.properties.MessageProperties;
import com.conexa.hospital.repositories.MedicoRepository;
import com.conexa.hospital.representations.consulta.ConsultaResponseRepresentation;
import com.conexa.hospital.representations.medico.MedicoAuthResponseRepresentation;
import com.conexa.hospital.security.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MedicoAuthService implements UserDetailsService {

    @Autowired
    private MedicoRepository medicoRepo;

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
	private MessageProperties properties;
    
    @Override
    public MedicoAuth loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Medico> medico = medicoRepo.findByUsuario(username);

        if (medico.isPresent()) {
            MedicoAuth medicoAuth = new MedicoAuth(medico.get());
            return medicoAuth;
        }

        return null;
    }

    public MedicoAuthResponseRepresentation authenticate(String usuario) {
        Optional<Medico> medicoOptional = medicoRepo.findByUsuario(usuario);
        MedicoAuth medicoAuth = null;

        if (!medicoOptional.isPresent()) {
            throw new ResourceNotFoundException(properties.resourceNotFound());
        }
        
        Medico medico = medicoOptional.get();
        medicoAuth = new MedicoAuth(medico);

        String jwt = jwtUtil.generateToken(medicoAuth);
        this.saveToken(medico, jwt);
        List<ConsultaResponseRepresentation> consultas = this.consultaService.retrieveAgendamentosDoDia(medico);

        return new MedicoAuthResponseRepresentation(jwt, medico.getNome(), medico.getEspecialidade(), consultas);
    }

    public void logout(String token) {
        String usuario = this.usernameFromToken(token);
        Optional<Medico> medicoOptional = this.medicoRepo.findByUsuario(usuario);

        if (!medicoOptional.isPresent()) {
            throw new ResourceNotFoundException(properties.resourceNotFound());
        }

        Medico medico = medicoOptional.get();
        medico.setToken(null);
        this.medicoRepo.save(medico);
    }

    public String usernameFromToken(String token) {
        String usuario = jwtUtil.extractUsername(token);
        return usuario;
    }

    private void saveToken(Medico medico, String token) {
        medico.setToken(token);
        this.medicoRepo.save(medico);
    }

}
