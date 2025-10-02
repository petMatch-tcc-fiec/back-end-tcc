package com.PetMatch.PetMatchBackEnd.features.animais.repositories;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnimaisRepository extends JpaRepository<Animais, UUID> {

    List<Animais> findBySexo(String sexo);
    List<Animais> findByRaca(String raca);
    List<Animais> findByCor(String cor);
    List<Animais> findByPorte(String porte);
    List<Animais> findByEspecie(String especie);
}
