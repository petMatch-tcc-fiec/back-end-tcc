package com.PetMatch.PetMatchBackEnd.features.animais.models;

import com.PetMatch.PetMatchBackEnd.features.user.models.OngUsuarios;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "animais")
public class Animais {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false, name = "id_animal")
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_ongusuarios_id_ong", nullable = false)
    private OngUsuarios ong;

    @OneToOne(mappedBy = "animal", cascade = CascadeType.ALL)
    private FichaMedicaAnimal fichaMedicaAnimal;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FotosAnimais> fotosAnimais = new ArrayList<>();
}
