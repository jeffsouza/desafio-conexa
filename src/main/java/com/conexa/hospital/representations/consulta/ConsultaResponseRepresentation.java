package com.conexa.hospital.representations.consulta;

import java.util.Date;

import com.conexa.hospital.models.Consulta;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;

public class ConsultaResponseRepresentation {
    
    private int idPaciente;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dataHoraAtendimento;

    public ConsultaResponseRepresentation() {}

    public ConsultaResponseRepresentation(int idPaciente, Date dataHoraAtendimento) {
        this.idPaciente = idPaciente;
        this.dataHoraAtendimento = dataHoraAtendimento;
    }

    @JsonGetter("id_paciente")
    public int getIdPaciente() {
        return this.idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    @JsonGetter("data_hora_atendimento")
    public Date getDataHoraAtendimento() {
        return this.dataHoraAtendimento;
    }

    public void setDataHoraAtendimento(Date dataHoraAtendimento) {
        this.dataHoraAtendimento = dataHoraAtendimento;
    }

    public static ConsultaResponseRepresentation fromConsulta(Consulta consulta) {
        ConsultaResponseRepresentation representation = new ConsultaResponseRepresentation();
        representation.setIdPaciente(consulta.getPaciente().getId());
        representation.setDataHoraAtendimento(consulta.getDataHoraAtendimento());
        return representation;
    }
}
