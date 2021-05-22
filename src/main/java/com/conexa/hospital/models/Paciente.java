package com.conexa.hospital.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Paciente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String nome;

    @NotNull
    private String cpf;

    @Min(1)
    private Integer idade;

    @NotNull
    private String telefone;

    public Paciente(int id, String nome, String cpf, int idade, String telefone) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.idade = idade;
        this.telefone = telefone;
    }

    public Paciente() {}

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return this.cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public int getIdade() {
        return this.idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof Paciente)) {
            return false;
        }

        Paciente paciente = (Paciente) object;

        return this.id == paciente.id &&
               this.nome.equals(paciente.getNome()) &&
               this.cpf.equals(paciente.getCpf()) &&
               this.idade == paciente.idade &&
               this.telefone.equals(paciente.getTelefone());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + String.valueOf(id).hashCode();
        result = 31 * result + nome.hashCode();
        result = 31 * result + cpf.hashCode();
        result = 31 * result + String.valueOf(idade).hashCode();
        result = 31 * result + telefone.hashCode();

        return result;
    }

}
