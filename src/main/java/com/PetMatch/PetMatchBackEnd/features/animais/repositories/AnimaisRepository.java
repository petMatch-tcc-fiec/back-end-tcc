package com.PetMatch.PetMatchBackEnd.features.animais.repositories;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import com.PetMatch.PetMatchBackEnd.features.user.models.OngUsuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnimaisRepository extends JpaRepository<Animais, UUID>, AnimalCustomRepository {
    /**
     * NOVO MÉTODO: Encontra todos os animais que pertencem a uma ONG específica.
     * @param ong A entidade OngUsuarios
     * @return Lista de animais
     */
    List<Animais> findByOng(OngUsuarios ong);
}
