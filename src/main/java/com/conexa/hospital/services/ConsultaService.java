package com.conexa.hospital.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.conexa.hospital.models.Consulta;
import com.conexa.hospital.models.Medico;
import com.conexa.hospital.models.Paciente;
import com.conexa.hospital.repositories.ConsultaRepository;
import com.conexa.hospital.repositories.MedicoRepository;
import com.conexa.hospital.repositories.PacienteRepository;
import com.conexa.hospital.representations.consulta.ConsultaRequestRepresentation;
import com.conexa.hospital.representations.consulta.ConsultaResponseRepresentation;
import com.conexa.hospital.representations.paciente.PacienteRepresentation;
import com.conexa.hospital.security.utils.JwtUtil;
import com.conexa.hospital.services.utils.ModelValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public Consulta create(Consulta consulta) {
        ModelValidator.validate(consulta);
        return consultaRepository.save(consulta);
    }

    public List<ConsultaResponseRepresentation> retrieveAgendamentosDoDia(Medico medico) {
        Date dateStart = this.date("yyyy-MM-dd 00:00:00");
        Date dateEnd = this.date("yyyy-MM-dd 23:59:59");

        List<Consulta> consultas = this.consultaRepository.findAllByMedicoAndDataHoraAtendimentoBetween(medico, dateStart, dateEnd);
        return this.consultaRepresentations(consultas);
    }

    public ConsultaResponseRepresentation addConsulta(String token, ConsultaRequestRepresentation requestRepresentation) {
        Medico medico = this.medicoFromToken(token);
        Paciente paciente = this.paciente(requestRepresentation.getPacienteRepresentation());
        Consulta consulta = new Consulta(medico, paciente, requestRepresentation.getDataHoraAtendimento());
        
        consulta = this.consultaRepository.save(consulta);

        return ConsultaResponseRepresentation.fromConsulta(consulta);
    }

    private List<ConsultaResponseRepresentation> consultaRepresentations(List<Consulta> consultas) {
        List<ConsultaResponseRepresentation> representations = new ArrayList<ConsultaResponseRepresentation>();
        for (Consulta consulta : consultas) {
            representations.add(ConsultaResponseRepresentation.fromConsulta(consulta));
        }

        return representations;
    }

    public Medico medicoFromToken(String token) {
        if (token != null) {
            String usuario = jwtUtil.extractUsername(token);
            return this.medicoRepository.findByUsuario(usuario).get();
        }

        return null;
    }

    private Paciente paciente(PacienteRepresentation representation) {
        if (representation != null) {
            return this.pacienteRepository.findById(representation.getId()).get();
        }

        return null;
    }

    private Date date(String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
        Date date = null;

        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
