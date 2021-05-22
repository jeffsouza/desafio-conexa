package com.conexa.hospital.representations.medico;

import java.util.List;

import com.conexa.hospital.representations.consulta.ConsultaResponseRepresentation;
import com.fasterxml.jackson.annotation.JsonGetter;

public class MedicoAuthResponseRepresentation {
    
    private String token;
    private String nome;
    private String especialidade;
    private List<ConsultaResponseRepresentation> consultas;

    public MedicoAuthResponseRepresentation(String token, String nome,
                                            String especialidade, List<ConsultaResponseRepresentation> consultas) {
        this.token = token;
        this.nome = nome;
        this.especialidade = especialidade;
        this.consultas = consultas;
    }

    @JsonGetter("token")
    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonGetter("medico")
    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    @JsonGetter("especialidade")
    public String getEspecialidade() {
        return this.especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    @JsonGetter("agendamentos_hoje")
    public List<ConsultaResponseRepresentation> getConsultas() {
        return this.consultas;
    }

    public void setConsultas(List<ConsultaResponseRepresentation> consultas) {
        this.consultas = consultas;
    }

}
