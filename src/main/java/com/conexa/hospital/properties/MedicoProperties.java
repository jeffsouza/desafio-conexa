package com.conexa.hospital.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:shared.properties")
public class MedicoProperties {
    
    @Value("${user.id1}")
    private int id1;

    @Value("${user.id2}")
    private int id2;

    @Value("${user.id3}")
    private int id3;

    @Value("${user.usuario1}")
    private String usuario1;

    @Value("${user.usuario2}")
    private String usuario2;

    @Value("${user.usuario3}")
    private String usuario3;

    @Value("${user.senha1}")
    private String senha1;

    @Value("${user.senha2}")
    private String senha2;

    @Value("${user.senha3}")
    private String senha3;

    @Value("${user.nome1}")
    private String nome1;

    @Value("${user.nome2}")
    private String nome2;

    @Value("${user.nome3}")
    private String nome3;

    @Value("${user.especialidade1}")
    private String especialidade1;

    @Value("${user.especialidade2}")
    private String especialidade2;

    @Value("${user.especialidade3}")
    private String especialidade3;

    public int id1() {
        return id1;
    }

    public int id2() {
        return id2;
    }

    public int id3() {
        return id3;
    }

    public String usuario1() {
        return usuario1;
    }

    public String usuario2() {
        return usuario2;
    }

    public String usuario3() {
        return usuario3;
    }

    public String senha1() {
        return senha1;
    }

    public String senha2() {
        return senha2;
    }

    public String senha3() {
        return senha3;
    }

    public String nome1() {
        return nome1;
    }

    public String nome2() {
        return nome2;
    }

    public String nome3() {
        return nome3;
    }

    public String especialidade1() {
        return especialidade1;
    }

    public String especialidade2() {
        return especialidade2;
    }

    public String especialidade3() {
        return especialidade3;
    }

}