package com.conexa.hospital;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import com.conexa.hospital.exceptions.ModelValidationException;
import com.conexa.hospital.models.Consulta;
import com.conexa.hospital.models.Medico;
import com.conexa.hospital.models.MedicoAuth;
import com.conexa.hospital.models.Paciente;
import com.conexa.hospital.repositories.ConsultaRepository;
import com.conexa.hospital.repositories.MedicoRepository;
import com.conexa.hospital.repositories.PacienteRepository;
import com.conexa.hospital.representations.consulta.ConsultaRequestRepresentation;
import com.conexa.hospital.representations.consulta.ConsultaResponseRepresentation;
import com.conexa.hospital.security.utils.JwtUtil;
import com.conexa.hospital.services.ConsultaService;
import com.conexa.hospital.services.MedicoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConsultaTests {

    @Autowired
    private JwtUtil jwtUtil;

    @Mock
	private MedicoService medicoService;

    @InjectMocks
	private ConsultaService consultaService;

    @Mock
	private MedicoRepository medicoRepository;

    @Mock
	private PacienteRepository pacienteRepository;

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
	private ConsultaRequestRepresentation consultaRequest;

    @Mock
	private JwtUtil jwtUtilMock;

    private Medico medico;
    private Paciente paciente;
    private Date dataAgendamento;

    @BeforeEach
    public void setUp() {
        medico = new Medico(1, "medico@email.com", "1234", "Medico 1", "Pediatra");
        paciente = new Paciente(1, "Paciente 1", "000.000.000-00", 20, "(22) 99999-9999");
        this.dataAgendamento = this.dataAgendamento();
    }
    
    @Test
	void addValidConsulta_andReturnsObj() {
        Consulta consulta = new Consulta(medico, paciente, dataAgendamento);

        when(consultaRepository.save(Mockito.any(Consulta.class))).thenReturn(consulta);

        Consulta consultaSaved = consultaService.create(consulta);

        assertEquals(consultaSaved.getPaciente().getId(), consulta.getPaciente().getId());
        assertEquals(consultaSaved.getMedico().getId(), consulta.getMedico().getId());
        assertEquals(consultaSaved.getDataHoraAtendimento(), consulta.getDataHoraAtendimento());
	}

    @Test
	void addConsultaWithoutMedico_andReturnsError() {
        Consulta consulta = new Consulta(null, paciente, dataAgendamento);

        when(consultaRepository.save(Mockito.any(Consulta.class))).thenReturn(consulta);

        assertThrows(ModelValidationException.class, () -> {
            consultaService.create(consulta);
        });
	}

    @Test
	void addConsultaWithoutPaciente_andReturnsError() {
        Consulta consulta = new Consulta(medico, null, dataAgendamento);

        when(consultaRepository.save(Mockito.any(Consulta.class))).thenReturn(consulta);

        assertThrows(ModelValidationException.class, () -> {
            consultaService.create(consulta);
        });
	}

    @Test
	void addConsultaWithoutDataAgendamento_andReturnsError() {
        Consulta consulta = new Consulta(medico, paciente, null);

        when(consultaRepository.save(Mockito.any(Consulta.class))).thenReturn(consulta);

        assertThrows(ModelValidationException.class, () -> {
            consultaService.create(consulta);
        });
	}

    @Test
	void addConsulta_andReturnsObjRepresentation() {
        Medico medico = new Medico(1, "medico@email.com", "1234", "Medico 1", "Pediatra");
        Consulta consulta = new Consulta(medico, paciente, dataAgendamento);
        String token = this.generateToken();

        when(jwtUtilMock.extractUsername(Mockito.anyString())).thenReturn(medico.getUsuario());
        when(medicoRepository.findByUsuario(Mockito.anyString())).thenReturn(Optional.of(medico));
        when(consultaRepository.save(Mockito.any(Consulta.class))).thenReturn(consulta);

        ConsultaResponseRepresentation representation = consultaService.addConsulta(token, consultaRequest);

        assertEquals(representation.getIdPaciente(), consulta.getPaciente().getId());
        assertEquals(representation.getDataHoraAtendimento(), consulta.getDataHoraAtendimento());
	}

    private String generateToken() {
        MedicoAuth medicoAuth = new MedicoAuth(medico);
        return jwtUtil.generateToken(medicoAuth);
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

}
