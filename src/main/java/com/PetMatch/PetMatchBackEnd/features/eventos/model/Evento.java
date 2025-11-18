package com.PetMatch.PetMatchBackEnd.features.eventos.model;

import com.PetMatch.PetMatchBackEnd.features.user.models.OngUsuarios;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_evento")
    private UUID id;

    @Column(name = "nome_evento", nullable = false, length = 255)
    private String nome;

    @Column(name = "data_hora_evento", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "endereco_evento", nullable = false, length = 255)
    private String endereco;

    // --- NOVO CAMPO ADICIONADO ---
    @Column(name = "descricao_evento", nullable = true, length = 2000) // Coloquei um tamanho maior
    private String descricao;
    // --- FIM DO NOVO CAMPO ---

    @Column(name = "fk_ongusuarios_id_ong", nullable = false)
    private UUID idOng;
}
