package com.conexa.hospital.representations.paciente;

import java.util.ArrayList;
import java.util.List;

import com.conexa.hospital.models.Paciente;
import com.fasterxml.jackson.annotation.JsonGetter;

public class PacienteRepresentation {
    
    private int id;
    private String nome;
    private String cpf;
    private int idade;
    private String telefone;

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

    @JsonGetter("cpf")
    public String getCpf() {
        return this.cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @JsonGetter("idade")
    public int getIdade() {
        return this.idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    @JsonGetter("telefone")
    public String getTelefone() {
        return this.telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Paciente toPaciente(Paciente paciente) {
        paciente.setNome(nome);
        paciente.setCpf(cpf);
        paciente.setIdade(idade);
        paciente.setTelefone(telefone);
        return paciente;
    }

    public static PacienteRepresentation fromPaciente(Paciente paciente) {
        PacienteRepresentation representation = new PacienteRepresentation();
        representation.setId(paciente.getId());
        representation.setNome(paciente.getNome());
        representation.setCpf(paciente.getCpf());
        representation.setIdade(paciente.getIdade());
        representation.setTelefone(paciente.getTelefone());
        return representation;
    }

    public static List<PacienteRepresentation> fromPaciente(List<Paciente> pacientes) {
        List<PacienteRepresentation> representations = new ArrayList<PacienteRepresentation>();

        for (Paciente paciente : pacientes) {
            PacienteRepresentation representation = new PacienteRepresentation();
            representation.setId(paciente.getId());
            representation.setNome(paciente.getNome());
            representation.setCpf(paciente.getCpf());
            representation.setIdade(paciente.getIdade());
            representation.setTelefone(paciente.getTelefone());
            representations.add(representation);
        }

        return representations;
    }

}
