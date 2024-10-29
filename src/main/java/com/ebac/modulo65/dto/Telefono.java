package com.ebac.modulo65.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Telefono {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTelefono;
    private String tipoTelefono;
    private int lada;
    private String numero;
    @ManyToOne
    @JoinColumn(name = "idUsuario")
    @JsonBackReference  // Parte no administrada de la relación
    private Usuario usuario;
}
