package com.conexa.hospital.api;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.conexa.hospital.models.Medico;
import com.conexa.hospital.models.MedicoAuth;
import com.conexa.hospital.models.Paciente;
import com.conexa.hospital.repositories.MedicoRepository;
import com.conexa.hospital.repositories.PacienteRepository;
import com.conexa.hospital.representations.paciente.PacienteRepresentation;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class PacienteApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Value("${application.version}")
    private String applicationVersion;

    private Medico medico;
    private Paciente p1;
    private Paciente p2;

    @BeforeEach
    public void setUp() {
        this.loadMedico();
        this.loadInitialData();
    }
    
    @Test
	void get_pacientes_andReturnsObjects() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get(applicationVersion() + "/pacientes")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + this.medico.getToken())
                
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$").isArray())
        .andExpect(status().isOk());

	}

    @Test
	void get_paciente_andReturnsObj() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get(applicationVersion() + "/pacientes/" + p1.getId())
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + this.medico.getToken())
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.nome").value("Paciente 1"))
        .andExpect(status().isOk());

	}

    @Test
	void post_createPaciente_andReturnsObj() throws Exception {
        PacienteRepresentation representation = this.buildRepresentation(0, "Paciente xpto", 
                                                    "222.222.222-22", 10, "(22) 22222-2222");

        mockMvc.perform(
            MockMvcRequestBuilders.post(applicationVersion() + "/pacientes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(representation))
                .characterEncoding("utf-8")
        )
        .andExpect(jsonPath("$.nome").value("Paciente xpto"))
        .andExpect(jsonPath("$.cpf").value("222.222.222-22"))
        .andExpect(jsonPath("$.idade").value(10))
        .andExpect(jsonPath("$.telefone").value("(22) 22222-2222"))
        .andExpect(status().isCreated());

	}

    @Test
	void post_createInvalidPaciente_andReturnsUnprocessableEntity() throws Exception {
        PacienteRepresentation representation = this.buildRepresentation(0, null,
                                                    "222.222.222-22", 10, "(22) 22222-2222");

        mockMvc.perform(
            MockMvcRequestBuilders.post(applicationVersion() + "/pacientes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(representation))
                .characterEncoding("utf-8")
        )
        .andExpect(status().isUnprocessableEntity());

	}

    @Test
	void put_updatePaciente_andReturnsObj() throws Exception {
        PacienteRepresentation representation = this.buildRepresentation(p1.getId(), "Paciente 11", 
                                                    p1.getCpf(), p1.getIdade(), p1.getTelefone());

        mockMvc.perform(
            MockMvcRequestBuilders.put(applicationVersion() + "/pacientes/" + p1.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + this.medico.getToken())
                .content(toJsonString(representation))
                .characterEncoding("utf-8")
        )
        .andExpect(jsonPath("$.nome").value("Paciente 11"))
        .andExpect(jsonPath("$.cpf").value(p1.getCpf()))
        .andExpect(jsonPath("$.idade").value(p1.getIdade()))
        .andExpect(jsonPath("$.telefone").value(p1.getTelefone()))
        .andExpect(status().isOk());
	}

    @Test
	void put_updateInvalidPaciente_andReturnsUnprocessableEntity() throws Exception {
        PacienteRepresentation representation = this.buildRepresentation(p1.getId(), null, 
                                                    p1.getCpf(), p1.getIdade(), p1.getTelefone());

        mockMvc.perform(
            MockMvcRequestBuilders.put(applicationVersion() + "/pacientes/" + p1.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + this.medico.getToken())
                .content(toJsonString(representation))
                .characterEncoding("utf-8")
        )
        .andExpect(status().isUnprocessableEntity());
	}

    private String applicationVersion() {
        return "/" + this.applicationVersion;
    }

    private void loadMedico() {
        medico = new Medico(1, "medicoteste@email.com", "1234", "Medico teste", "Pediatra");
        MedicoAuth medicoAuth = new MedicoAuth(medico);
        String jwt = jwtUtil.generateToken(medicoAuth);
        medico.setToken(jwt);
        this.medicoRepository.save(medico);
    }

    private PacienteRepresentation buildRepresentation(int id, String nome, String cpf,
                                                    int idade, String telefone) {
        PacienteRepresentation representation = new PacienteRepresentation();
        representation.setId(id);
        representation.setNome(nome);
        representation.setCpf(cpf);
        representation.setIdade(idade);
        representation.setTelefone(telefone);
        return representation;
    }

    private String toJsonString(PacienteRepresentation representation) {
        try {
            return new ObjectMapper().writeValueAsString(representation);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void loadInitialData() {
        p1 = new Paciente(1, "Paciente 1", "000.000.000-00", 20, "(22) 99999-9999");
        p2 = new Paciente(2, "Paciente 2", "111.111.111-11", 25, "(23) 88888-8888");
        this.pacienteRepository.save(p1);
        this.pacienteRepository.save(p2);
    }

}
