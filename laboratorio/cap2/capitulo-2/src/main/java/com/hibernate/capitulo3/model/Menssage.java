package com.hibernate.capitulo3.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Table(name = "menssage")
@Entity
public class Menssage extends BaseModel {
    @Column(name = "texto", length = 255, nullable = false)
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
