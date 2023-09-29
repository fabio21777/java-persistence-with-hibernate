package com.hibernate.capitulo3.jpa;

import com.hibernate.capitulo2.model.BaseModel;
import com.hibernate.capitulo2.model.Menssage;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MensagemJpa {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void save(BaseModel entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
    }

    @Transactional(readOnly = true)
    public List<Menssage> findAllMenssage() {
        return entityManager.createQuery("select m from Menssage m", Menssage.class).getResultList();
    }

}
