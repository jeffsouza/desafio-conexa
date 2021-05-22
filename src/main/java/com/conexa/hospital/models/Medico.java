package com.conexa.hospital.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Medico {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String usuario;

    @NotNull
    private String senha;

    private String token;

    @NotNull
    private String nome;

    @NotNull
    private String especialidade;

    public Medico() {}

    public Medico(int id, String usuario, String senha, String nome, String especialidade) {
        this.id = id;
        this.usuario = usuario;
        this.senha = senha;
        this.nome = nome;
        this.especialidade = especialidade;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return this.usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return this.senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecialidade() {
        return this.especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof Medico)) {
            return false;
        }

        Medico medico = (Medico) object;

        return this.id == medico.id &&
               this.usuario.equals(medico.getUsuario()) &&
               this.senha.equals(medico.getSenha()) &&
               this.nome.equals(medico.getNome()) &&
               this.especialidade.equals(medico.getEspecialidade());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + String.valueOf(id).hashCode();
        result = 31 * result + usuario.hashCode();
        result = 31 * result + senha.hashCode();
        result = 31 * result + nome.hashCode();
        result = 31 * result + especialidade.hashCode();

        return result;
    }

}
