package com.PetMatch.PetMatchBackEnd.features.eventos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_eventos")
    private UUID id;

    @Column(name = "nome_eventos", nullable = false, length = 255)
    private String nome;

    @Column(name = "data_hora_eventos", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "endereco_eventos", nullable = false, length = 300)
    private String endereco;

    @Column(name = "fl_ongUsuarios_id_ong", nullable = false)
    private UUID idOng;
}
