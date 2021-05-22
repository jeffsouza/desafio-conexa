package com.conexa.hospital.repositories;

import java.util.Date;
import java.util.List;

import com.conexa.hospital.models.Consulta;
import com.conexa.hospital.models.Medico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Integer> {
    
    public List<Consulta> findAllByMedicoAndDataHoraAtendimentoBetween(Medico medico, Date dataHoraAtendimentoStart, Date dataHoraAtendimentoEnd);

}
