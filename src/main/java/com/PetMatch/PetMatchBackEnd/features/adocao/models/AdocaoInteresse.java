package com.PetMatch.PetMatchBackEnd.features.adocao.models;

import com.PetMatch.PetMatchBackEnd.features.adocao.enums.AdocaoStatus;
import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "adocao_interesse")
public class AdocaoInteresse {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animais animal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "data_de_criacao", nullable = false)
    private LocalDateTime dataDeCriacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdocaoStatus status;

    @PrePersist
    protected void onCreate() {
        dataDeCriacao = LocalDateTime.now();
        status = AdocaoStatus.PENDENTE;
    }
}
