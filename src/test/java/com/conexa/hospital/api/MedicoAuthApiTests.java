package com.conexa.hospital.api;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.conexa.hospital.models.Medico;
import com.conexa.hospital.models.MedicoAuth;
import com.conexa.hospital.repositories.MedicoRepository;
import com.conexa.hospital.representations.medico.MedicoAuthRequestRepresentation;
import com.conexa.hospital.security.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MedicoAuthApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${application.version}")
    private String applicationVersion;

    private Medico medico;
    private String senha = "1234";

    @BeforeEach
    public void setUp() {
        this.loadInitialData();
    }

    @Test
	void post_authenticateMedico_andReturnsObj() throws Exception {
        MedicoAuthRequestRepresentation representation = this.buildRepresentation(
                                                            medico.getUsuario(), senha);

        mockMvc.perform(
            MockMvcRequestBuilders.post(applicationVersion() + "/medicos/authenticate")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(representation))
                .characterEncoding("utf-8")
        )
        .andExpect(jsonPath("$.token").exists())
        .andExpect(jsonPath("$.medico").value(medico.getNome()))
        .andExpect(jsonPath("$.especialidade").value(medico.getEspecialidade()))
        .andExpect(jsonPath("$.agendamentos_hoje").isArray())
        .andExpect(status().isOk());

	}

    @Test
	void post_authenticateMedicoWithInvalidUsuario_andReturnsAccessDenied() throws Exception {
        MedicoAuthRequestRepresentation representation = this.buildRepresentation(
                                                            "wronguser", senha);

        mockMvc.perform(
            MockMvcRequestBuilders.post(applicationVersion() + "/medicos/authenticate")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(representation))
                .characterEncoding("utf-8")
        )
        .andExpect(status().isForbidden());

	}

    @Test
	void post_authenticateMedicoWithInvalidSenha_andReturnsAccessDenied() throws Exception {
        MedicoAuthRequestRepresentation representation = this.buildRepresentation(
                                                            medico.getUsuario(), "wrongpasswword");

        mockMvc.perform(
            MockMvcRequestBuilders.post(applicationVersion() + "/medicos/authenticate")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(representation))
                .characterEncoding("utf-8")
        )
        .andExpect(status().isForbidden());

	}

    @Test
	void delete_logoutMedico_andReturnsNoContent() throws Exception {
        
        this.addTokenToMedico();
        MedicoAuthRequestRepresentation representation = new MedicoAuthRequestRepresentation();
        representation.setToken(medico.getToken());

        mockMvc.perform(
            MockMvcRequestBuilders.delete(applicationVersion() + "/medicos/logout")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + medico.getToken())
                .content(toJsonString(representation))
                .characterEncoding("utf-8")
        )
        .andExpect(status().isNoContent());

	}

    @Test
	void delete_logoutMedicoWithInvalidAuthorizationToken_andReturnsAccessDenied() throws Exception {
        
        MedicoAuthRequestRepresentation representation = new MedicoAuthRequestRepresentation();
        representation.setToken(this.generateToken());

        mockMvc.perform(
            MockMvcRequestBuilders.delete(applicationVersion() + "/medicos/logout")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + representation.getToken())
                .content(toJsonString(representation))
                .characterEncoding("utf-8")
        )
        .andExpect(status().isForbidden());

	}

    @Test
	void delete_logoutMedicoWithInvalidMedicoToken_andReturnsNotFound() throws Exception {
        
        this.addTokenToMedico();
        MedicoAuthRequestRepresentation representation = new MedicoAuthRequestRepresentation();
        representation.setToken(this.generateInvalidToken());

        mockMvc.perform(
            MockMvcRequestBuilders.delete(applicationVersion() + "/medicos/logout")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + medico.getToken())
                .content(toJsonString(representation))
                .characterEncoding("utf-8")
        )
        .andExpect(status().isNotFound());

	}

    private String applicationVersion() {
        return "/" + this.applicationVersion;
    }

    private MedicoAuthRequestRepresentation buildRepresentation(String usuario, String senha) {
        MedicoAuthRequestRepresentation representation = new MedicoAuthRequestRepresentation();
        representation.setUsuario(usuario);
        representation.setSenha(senha);
        return representation;
    }

    private void addTokenToMedico() {
        String jwt = this.generateToken();
        medico.setToken(jwt);
        this.medicoRepository.save(medico);
    }

    private String generateToken() {
        MedicoAuth medicoAuth = new MedicoAuth(medico);
        return jwtUtil.generateToken(medicoAuth);
    }

    private String generateInvalidToken() {
        Medico medico = new Medico();
        medico.setUsuario("nonexistent_user");
        MedicoAuth medicoAuth = new MedicoAuth(medico);
        return jwtUtil.generateToken(medicoAuth);
    }

    private String toJsonString(MedicoAuthRequestRepresentation representation) {
        try {
            return new ObjectMapper().writeValueAsString(representation);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void loadInitialData() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedSenha = passwordEncoder.encode(senha);
        medico = new Medico(1, "medicoteste@email.com", encodedSenha, "Medico teste", "Pediatra");
        this.medicoRepository.save(medico);
    }

}