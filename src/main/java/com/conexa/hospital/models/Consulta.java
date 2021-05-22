package com.conexa.hospital.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class Consulta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @NotNull
    private Medico medico;

    @ManyToOne
    @NotNull
    private Paciente paciente;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date dataHoraAtendimento;

    public Consulta() {}

    public Consulta(Medico medico, Paciente paciente, Date dataHoraAtendimento) {
        this.medico = medico;
        this.paciente = paciente;
        this.dataHoraAtendimento = dataHoraAtendimento;
    }

    public Medico getMedico() {
        return this.medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Date getDataHoraAtendimento() {
        return this.dataHoraAtendimento;
    }

    public void setDataHoraAtendimento(Date dataHoraAtendimento) {
        this.dataHoraAtendimento = dataHoraAtendimento;
    }

}
