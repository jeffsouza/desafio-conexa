package com.conexa.hospital;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.conexa.hospital.exceptions.ResourceNotFoundException;
import com.conexa.hospital.models.Medico;
import com.conexa.hospital.models.MedicoAuth;
import com.conexa.hospital.models.Paciente;
import com.conexa.hospital.properties.MessageProperties;
import com.conexa.hospital.repositories.MedicoRepository;
import com.conexa.hospital.representations.consulta.ConsultaResponseRepresentation;
import com.conexa.hospital.representations.medico.MedicoAuthResponseRepresentation;
import com.conexa.hospital.security.utils.JwtUtil;
import com.conexa.hospital.services.ConsultaService;
import com.conexa.hospital.services.MedicoAuthService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MedicoTests {
    
    @Mock
    private JwtUtil jwtUtil;

    @Mock
	private MedicoRepository medicoRepository;

    @InjectMocks
	private MedicoAuthService medicoAuthService;

    @Mock
	private ConsultaService consultaService;

    @Mock
    private MedicoAuth medicoAuth;

    @Mock
    private MessageProperties messageProperties;

    private String jwt;
    private Medico medico;
    private Paciente paciente;
    private Date dataAgendamento;

    @BeforeEach
    public void setUp() {
        this.loadMedico();
        this.loadToken();
        this.loadPaciente();
        this.loadDataAgendamento();
    }

    @Test
	void authenticateMedico_andReturnsMedicoWithBasicInformationsAndTokenAndConsultas() {
        ConsultaResponseRepresentation consultaRepresentation = new ConsultaResponseRepresentation(paciente.getId(), dataAgendamento);
        List<ConsultaResponseRepresentation> consultas = Collections.singletonList(consultaRepresentation);
        MedicoAuthResponseRepresentation repr = new MedicoAuthResponseRepresentation(jwt, medico.getNome(), 
                                                                                    medico.getEspecialidade(),
                                                                                    consultas);

		when(jwtUtil.generateToken(Mockito.any(MedicoAuth.class))).thenReturn(jwt);
        when(medicoRepository.findByUsuario(Mockito.anyString())).thenReturn(Optional.of(medico));
        when(consultaService.retrieveAgendamentosDoDia(medico)).thenReturn(consultas);

        MedicoAuthResponseRepresentation response = medicoAuthService.authenticate(medico.getUsuario());

		assertEquals(response.getToken(), repr.getToken());
        assertEquals(response.getNome(), repr.getNome());
        assertEquals(response.getEspecialidade(), repr.getEspecialidade());
        assertEquals(response.getConsultas().size(), 1);
	}

    @Test
    public void logoutWithValidMedico_andReturnsSuccess() {
        JwtUtil jUtil = new JwtUtil();
        String token = jUtil.generateToken(medicoAuth);
        medico.setToken(token);

        when(this.medicoAuthService.usernameFromToken(token)).thenReturn(medico.getUsuario());
        when(this.medicoRepository.findByUsuario(Mockito.anyString())).thenReturn(Optional.of(medico));
        
        assertDoesNotThrow(() -> {
            this.medicoAuthService.logout(token);
        });
    }

    @Test
    public void logoutWithInvalidMedico_andReturnsError() {
        medico.setToken(jwt);

        when(this.messageProperties.resourceNotFound()).thenReturn(Mockito.anyString());
        when(this.medicoAuthService.usernameFromToken(jwt)).thenReturn(medico.getUsuario());
        when(this.medicoRepository.findByUsuario(Mockito.anyString())).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            this.medicoAuthService.logout(jwt);
        });
    }

    private void loadMedico() {
		medico = new Medico(1, "medico@email.com", "1234", "Medico 1", "Pediatra");
    }

    private void loadToken() {
		JwtUtil jUtil = new JwtUtil();
        this.jwt = jUtil.generateToken(medicoAuth);
    }

    private void loadPaciente() {
		paciente = new Paciente(1, "Paciente 1", "000.000.000-00", 20, "(22) 99999-9999");
    }

    private void loadDataAgendamento() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStartString = "2021-05-20 00:00:00";
        dataAgendamento = null;

        try {
            dataAgendamento = formatter.parse(dateStartString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
