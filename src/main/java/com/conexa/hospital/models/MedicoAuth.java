package com.conexa.hospital.models;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MedicoAuth implements UserDetails {

    private Medico medico;

    public MedicoAuth(Medico medico) {
        this.medico = medico;
    }

    public Medico getMedico() {
        return this.medico;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.medico.getSenha();
    }

    @Override
    public String getUsername() {
        return this.medico.getUsuario();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
