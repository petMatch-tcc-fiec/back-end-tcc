package com.PetMatch.PetMatchBackEnd.features.animais.services;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalRegisterDto;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalResponseDto;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalSearch;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnimaisService {

    // --- MÉTODOS COM LÓGICA DE PERMISSÃO ---

    /**
     * Cria um novo animal. Apenas ONGs podem criar.
     * Recebe ID e Perfil do usuário logado.
     * ✨ ATUALIZADO: Removido MultipartFile, agora a imagem vem via URL no DTO.
     */
    AnimalResponseDto create(AnimalRegisterDto dto, UUID idUsuarioLogado, String perfilUsuario);

    /**
     * Lista animais. Adotantes/Admin veem tudo. ONGs veem apenas os seus.
     * Recebe o objeto Usuario.
     */
    List<AnimalResponseDto> findAllAnimaisDto(Usuario usuarioAutenticado);

    /**
     * Busca um animal por ID. Adotantes/Admin veem qualquer um. ONGs veem apenas os seus.
     * Recebe o objeto Usuario.
     */
    Optional<AnimalResponseDto> findAnimalDtoById(UUID id, Usuario usuarioAutenticado);

    /**
     * Atualiza um animal. Apenas a ONG dona pode atualizar.
     * Recebe ID e Perfil do usuário logado.
     */
    AnimalResponseDto update(UUID id, AnimalRegisterDto updateDto, UUID idUsuarioLogado, String perfilUsuario);

    /**
     * Deleta um animal. Apenas a ONG dona pode deletar.
     * Recebe ID e Perfil do usuário logado.
     */
    void deleteById(UUID id, UUID idUsuarioLogado, String perfilUsuario);


    // --- MÉTODOS ANTIGOS (Mantidos para compatibilidade interna) ---

    Optional<Animais> findById(UUID id);

    List<Animais> findAll();

    List<Animais> findAllWithQueries(AnimalSearch animalSearch);

    Optional<Animais> update(UUID id, Animais updateAnimais);

    void deleteById(UUID id);
}