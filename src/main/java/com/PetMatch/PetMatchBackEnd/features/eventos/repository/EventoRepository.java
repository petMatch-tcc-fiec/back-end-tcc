package com.PetMatch.PetMatchBackEnd.features.eventos.repository;

import com.PetMatch.PetMatchBackEnd.features.eventos.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface EventoRepository extends JpaRepository<Evento, UUID> {
}
