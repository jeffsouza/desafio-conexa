package com.conexa.hospital.representations.medico;

import com.conexa.hospital.models.Medico;
import com.fasterxml.jackson.annotation.JsonGetter;

public class MedicoRepresentation {
    
    private int id;
    private String nome;
    private String especialidade;

    @JsonGetter("id")
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonGetter("nome")
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

    public Medico toMedico(Medico medico) {
        medico.setNome(nome);
        medico.setEspecialidade(especialidade);
        return medico;
    }

    public static MedicoRepresentation fromMedico(Medico medico) {
        MedicoRepresentation representation = new MedicoRepresentation();
        representation.setId(medico.getId());
        representation.setNome(medico.getNome());
        representation.setEspecialidade(medico.getEspecialidade());
        return representation;
    }

}
