package com.conexa.hospital.representations.medico;

import com.fasterxml.jackson.annotation.JsonGetter;

public class MedicoAuthRequestRepresentation {
    
    private String token;
    private String usuario;
    private String senha;

    public MedicoAuthRequestRepresentation() {}

    public MedicoAuthRequestRepresentation(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    @JsonGetter("token")
    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonGetter("usuario")
    public String getUsuario() {
        return this.usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @JsonGetter("senha")
    public String getSenha() {
        return this.senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
