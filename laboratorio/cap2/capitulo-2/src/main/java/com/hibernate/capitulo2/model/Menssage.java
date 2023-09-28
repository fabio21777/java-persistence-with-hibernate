package com.hibernate.capitulo2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Table(name = "menssage")
@Entity
public class Menssage extends BaseModel {



    private String texto;

    public Menssage() {
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
