package com.PetMatch.PetMatchBackEnd.features.animais.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "fichaMedicaAnimal")
public class FichaMedicaAnimal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idFichaMedica;

    @Column(name = "vacinas")
    private String vacinas;

    @Column(name = "historico_saude")
    private String historicoSaude;

    @OneToOne
    @JoinColumn(name = "fk_animais_id_animal", nullable = false)
    private Animais animal;
}
