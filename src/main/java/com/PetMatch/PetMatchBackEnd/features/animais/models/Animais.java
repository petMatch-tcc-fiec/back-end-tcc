package com.PetMatch.PetMatchBackEnd.features.animais.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "animais")
public class Animais {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome_animal")
    private String nome;

    @Column(name = "idade_animal")
    private Integer idade;

    @Column(name = "porte_animal")
    private String porte;

    @Column(name = "sexo_animal", length = 1)
    private String sexo;

    @Column(name = "especie_animal")
    private String especie;

    @Column(name = "raca_animal")
    private String raca;

    @Column(name = "cor_animal")
    private String cor;

    @Column(name = "observacoes_animal")
    private String observacoesAnimal;

    // Relacionamento OneToOne com FichaMedicaAnimal
    @OneToOne(mappedBy = "animal", cascade = CascadeType.ALL)
    private FichaMedicaAnimal fichaMedicaAnimal;

    // Relacionamento OneToMany com FotosAnimais
    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FotosAnimais> fotosAnimais = new ArrayList<>();
}
