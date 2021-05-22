package com.conexa.hospital.representations.consulta;

import java.util.Date;

import com.conexa.hospital.representations.paciente.PacienteRepresentation;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;

public class ConsultaRequestRepresentation {
    
    private PacienteRepresentation pacienteRepresentation;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dataHoraAtendimento;

    @JsonGetter("paciente")
    public PacienteRepresentation getPacienteRepresentation() {
        return this.pacienteRepresentation;
    }

    public void setPacienteRepresentation(PacienteRepresentation pacienteRepresentation) {
        this.pacienteRepresentation = pacienteRepresentation;
    }

    @JsonGetter("data_hora_atendimento")
    public Date getDataHoraAtendimento() {
        return this.dataHoraAtendimento;
    }

    public void setDataHoraAtendimento(Date dataHoraAtendimento) {
        this.dataHoraAtendimento = dataHoraAtendimento;
    }
}
