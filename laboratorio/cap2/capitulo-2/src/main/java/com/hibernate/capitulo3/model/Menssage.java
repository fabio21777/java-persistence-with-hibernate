package com.hibernate.capitulo3.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Table(name = "menssage")
// @Entity(name = "menssage")
public class Menssage extends BaseModel {
    @Column(name = "texto", length = 255, nullable = false)
    // @Basic(optional = false)
    private String texto;

    // esse cara não vai ser mapeado
    @Transient
    private String trasiente;

    public Menssage() {
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTrasiente() {
        return trasiente;
    }

    public void setTrasiente(String trasiente) {
        this.trasiente = trasiente;
    }

}
