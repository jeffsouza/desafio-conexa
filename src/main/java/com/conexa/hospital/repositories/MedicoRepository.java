package com.conexa.hospital.repositories;

import java.util.Optional;

import com.conexa.hospital.models.Medico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {

    public Optional<Medico> findByUsuario(String usuario);
    
}
