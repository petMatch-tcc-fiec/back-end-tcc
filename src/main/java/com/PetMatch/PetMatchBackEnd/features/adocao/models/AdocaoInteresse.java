package com.PetMatch.PetMatchBackEnd.features.adocao.models;

import com.PetMatch.PetMatchBackEnd.features.adocao.enums.AdocaoStatus;
import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "adocao_interesse")
@Schema(
        name = "AdocaoInteresse",
        description = "Entidade que representa o interesse de um usuário em adotar um animal."
)
public class AdocaoInteresse {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false, name = "id_interesse")
    @Schema(
            description = "Identificador único do registro de interesse de adoção.",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_animal", nullable = false)
    @Schema(
            description = "Animal para o qual o interesse foi registrado.",
            implementation = Animais.class
    )
    private Animais animal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @Schema(
            description = "Usuário que demonstrou interesse na adoção.",
            implementation = Usuario.class
    )
    private Usuario usuario;

    @Column(name = "data_de_criacao", nullable = false)
    @Schema(
            description = "Data e hora em que o interesse foi criado.",
            example = "2025-11-07T18:45:00"
    )
    private LocalDateTime dataDeCriacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(
            description = "Status atual do processo de adoção.",
            example = "PENDENTE"
    )
    private AdocaoStatus status;

    @PrePersist
    protected void onCreate() {
        dataDeCriacao = LocalDateTime.now();
        status = AdocaoStatus.PENDENTE;
    }
}
