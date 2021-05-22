package com.conexa.hospital.api;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.conexa.hospital.models.Medico;
import com.conexa.hospital.models.MedicoAuth;
import com.conexa.hospital.models.Paciente;
import com.conexa.hospital.repositories.MedicoRepository;
import com.conexa.hospital.repositories.PacienteRepository;
import com.conexa.hospital.representations.consulta.ConsultaRequestRepresentation;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ConsultaApiTests {

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
    private Paciente paciente;

    @BeforeEach
    public void setUp() {
        this.loadMedico();
        this.loadPaciente();
    }

    @Test
	void post_createConsulta_andReturnsObj() throws Exception {
        ConsultaRequestRepresentation representation = this.buildConsultaRepresentation();

        String dataHoraAgendamento = this.dataAgendamentoAsString(representation.getDataHoraAtendimento());

        mockMvc.perform(
            MockMvcRequestBuilders.post(applicationVersion() + "/consultas/nova_consulta")
                .header("Authorization", "Bearer " + medico.getToken())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(representation))
                .characterEncoding("utf-8")
        )
        .andExpect(jsonPath("$.id_paciente").value(paciente.getId()))
        .andExpect(jsonPath("$.data_hora_atendimento").value(dataHoraAgendamento))
        .andExpect(status().isCreated());

	}

    @Test
	void post_createInvalidConsulta_andReturnsUnprocessableEntity() throws Exception {
        ConsultaRequestRepresentation representation = this.buildConsultaRepresentation();
        representation.setPacienteRepresentation(null);

        mockMvc.perform(
            MockMvcRequestBuilders.post(applicationVersion() + "/consultas/nova_consulta")
                .header("Authorization", "Bearer " + medico.getToken())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(representation))
                .characterEncoding("utf-8")
        )
        .andExpect(status().isUnprocessableEntity());
	}

    private String applicationVersion() {
        return "/" + this.applicationVersion;
    }

    private ConsultaRequestRepresentation buildConsultaRepresentation() {
        ConsultaRequestRepresentation representation = new ConsultaRequestRepresentation();
        representation.setPacienteRepresentation(this.buildPacienteRepresentation());
        representation.setDataHoraAtendimento(this.dataAgendamento());
        return representation;
    }

    private PacienteRepresentation buildPacienteRepresentation() {
        PacienteRepresentation representation = new PacienteRepresentation();
        representation.setId(paciente.getId());
        representation.setNome(paciente.getNome());
        representation.setCpf(paciente.getCpf());
        representation.setIdade(paciente.getIdade());
        representation.setTelefone(paciente.getTelefone());
        return representation;
    }

    private Date dataAgendamento() {
        String dataFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(dataFormat);
        String dateStartString = new SimpleDateFormat(dataFormat).format(Calendar.getInstance().getTime());
        Date dataAgendamento = null;
        try {
            dataAgendamento = formatter.parse(dateStartString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dataAgendamento;
    }

    private String dataAgendamentoAsString(Date dataHoraAgendamento) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        return df.format(dataHoraAgendamento);
    }

    private String toJsonString(ConsultaRequestRepresentation representation) {
        try {
            return new ObjectMapper().writeValueAsString(representation);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void loadMedico() {
        medico = new Medico(1, "medicoteste@email.com", "1234", "Medico teste", "Pediatra");
        MedicoAuth medicoAuth = new MedicoAuth(medico);
        String jwt = jwtUtil.generateToken(medicoAuth);
        medico.setToken(jwt);
        this.medicoRepository.save(medico);
    }

    private void loadPaciente() {
        paciente = new Paciente(1, "Paciente 1", "000.000.000-00", 20, "(22) 99999-9999");
        this.pacienteRepository.save(paciente);
    }

}
