package com.hibernate.capitulo3.services;

import com.hibernate.capitulo2.jpa.MensagemJpa;
import com.hibernate.capitulo2.model.Menssage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenssageService {
    @Autowired
    private MensagemJpa mensagemJpa;

    public void save(Menssage menssage) {
        if (menssage == null)
            throw new IllegalArgumentException("Mensagem não pode ser nula");

        mensagemJpa.save(menssage);
    }

    public List<Menssage> findAllMenssage() {
        return mensagemJpa.findAllMenssage();
    }
}
