package com.PetMatch.PetMatchBackEnd.features.animais.models;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "fotosAnimais")
public class FotosAnimais {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idFotoAnimal;

    @Column(name = "arquivo_animal", nullable = false)
    private String arquivoAnimal;

    @ManyToOne
    @JoinColumn(name = "fk_animais_id_animal", nullable = false)
    private Animais animal;
}

