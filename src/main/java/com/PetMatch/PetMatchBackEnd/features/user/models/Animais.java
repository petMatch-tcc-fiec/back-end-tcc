package com.PetMatch.PetMatchBackEnd.features.user.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "animais")
public class Animais {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_animal;

    @Column(name = "nome_animal")
    private String nome;

    @Column(name = "idade_animal")
    private Integer idade;

    @Column(name = "porte_animal")
    private String porte;

    @Column(name = "sexo_animal", length = 1)
    private String sexo;

    @Column(name = "especie_animal")
    private String tipo;

    @Column(name = "raca_animal")
    private String raca;

    @Column(name = "cor_animal")
    private String cor;

    @Column(name = "observacoes_animal")
    private String observacoesAnimal;

    @Column(name = "ficha_medica_animal")
    private FichaMedicaAnimal fichaMedicaAnimal;

    @Column(name = "fotos_animais")
    private FotosAnimais fotosAnimais;


}
