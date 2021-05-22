package com.conexa.hospital;

import java.util.Arrays;

import com.conexa.hospital.models.Medico;
import com.conexa.hospital.properties.MedicoProperties;
import com.conexa.hospital.repositories.MedicoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@ComponentScan(
	basePackages = {
		"com.conexa.hospital.controllers",
		"com.conexa.hospital.services",
		"com.conexa.hospital.security",
		"com.conexa.hospital.properties"
	}
)
public class HospitalApplication {

	@Autowired
	private MedicoProperties properties;

	@Bean
	public ApplicationRunner initializer(MedicoRepository repository) {
		return args -> repository.saveAll(Arrays.asList(
			this.buildMedico(properties.id1(), properties.usuario1(), properties.senha1(),
							properties.nome1(), properties.especialidade1()),
			this.buildMedico(properties.id2(), properties.usuario2(), properties.senha2(),
							properties.nome2(), properties.especialidade2()),
			this.buildMedico(properties.id3(), properties.usuario3(), properties.senha3(),
							properties.nome3(), properties.especialidade3())
		));
	}

	public static void main(String[] args) {
		SpringApplication.run(HospitalApplication.class, args);
	}

	private Medico buildMedico(int id, String usuario, String senha, String nome, String especialidade) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedSenha = passwordEncoder.encode(senha);
		return new Medico(id, usuario, encodedSenha, nome, especialidade);
	}

}
