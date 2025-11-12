package com.PetMatch.PetMatchBackEnd.features.animais.services.impl;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import com.PetMatch.PetMatchBackEnd.features.animais.models.FichaMedicaAnimal;
import com.PetMatch.PetMatchBackEnd.features.animais.models.FotosAnimais;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalRegisterDto;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalResponseDto;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalSearch;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.OngSimplificadaDTO;
import com.PetMatch.PetMatchBackEnd.features.animais.repositories.AnimaisRepository;
import com.PetMatch.PetMatchBackEnd.features.animais.repositories.FichaMedicaRepository;
import com.PetMatch.PetMatchBackEnd.features.animais.repositories.FotosAnimaisRepository;
import com.PetMatch.PetMatchBackEnd.features.animais.services.AnimaisService;
import com.PetMatch.PetMatchBackEnd.features.user.models.OngUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.OngUsuariosRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnimaisServiceImpl implements AnimaisService{

    private final AnimaisRepository animaisRepository;
    private final OngUsuariosRepository ongUsuariosRepository;
    private final FichaMedicaRepository fichaMedicaRepository;
    private final FotosAnimaisRepository fotosAnimaisRepository;

    public AnimaisServiceImpl(AnimaisRepository animaisRepository, OngUsuariosRepository ongUsuariosRepository, FichaMedicaRepository fichaMedicaRepository, FotosAnimaisRepository fotosAnimaisRepository) {
        this.animaisRepository = animaisRepository;
        this.ongUsuariosRepository = ongUsuariosRepository;
        this.fichaMedicaRepository = fichaMedicaRepository;
        this.fotosAnimaisRepository = fotosAnimaisRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public AnimalResponseDto create(AnimalRegisterDto dto, Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        // Busca a ONG desse usuário
        OngUsuarios ong = ongUsuariosRepository.findByUsuario(usuario)
                .orElseThrow(() -> new AccessDeniedException("Usuário não é uma ONG"));

        // Cria o animal vinculado à ONG do usuário logado
        Animais animal = Animais.builder()
                .nome(dto.getNome())
                .idade(dto.getIdade())
                .porte(dto.getPorte())
                .sexo(dto.getSexo())
                .especie(dto.getEspecie())
                .raca(dto.getRaca())
                .cor(dto.getCor())
                .observacoesAnimal(dto.getObservacoesAnimal())
                .ong(ong)
                .build();

        Animais salvo = animaisRepository.save(animal);

        // ✅ Cria o DTO manualmente aqui mesmo
        return AnimalResponseDto.builder()
                .id(salvo.getId())
                .nome(salvo.getNome())
                .idade(salvo.getIdade())
                .porte(salvo.getPorte())
                .sexo(salvo.getSexo())
                .especie(salvo.getEspecie())
                .raca(salvo.getRaca())
                .cor(salvo.getCor())
                .observacoesAnimal(salvo.getObservacoesAnimal())
                .ong(OngSimplificadaDTO.builder()
                        .nomeFantasiaOng(ong.getNomeFantasiaOng())
                        .build())
                .build();
    }

    @Override
    public Optional<Animais> findById(UUID id) {
        return animaisRepository.findById(id);
    }

    @Override
    public List<Animais> findAll() {
        return animaisRepository.findAll();
    }

    @Override
    public List<Animais> findAllWithQueries(AnimalSearch animalSearch) {
        List<Animais> meusAnimais = animaisRepository.findAnimais(animalSearch);
        if(CollectionUtils.isEmpty(meusAnimais)) {
            return List.of();
        } else {
            return meusAnimais.stream().map(
                    animal -> Animais.builder()
                            .id(animal.getId())
                            .nome(animal.getNome())
                            .idade(animal.getIdade())
                            .porte(animal.getPorte())
                            .sexo(animal.getSexo())
                            .especie(animal.getEspecie())
                            .raca(animal.getRaca())
                            .cor(animal.getCor())
                            .build()
            ).toList();
        }
    }

    @Override
    public Optional<Animais> update(UUID id, Animais updateAnimais) {
        return animaisRepository.findById(id).map(animais -> {
            animais.setNome(updateAnimais.getNome());
            animais.setIdade(updateAnimais.getIdade());
            animais.setPorte(updateAnimais.getPorte());
            animais.setSexo(updateAnimais.getSexo());
            animais.setEspecie(updateAnimais.getEspecie());
            animais.setRaca(updateAnimais.getRaca());
            animais.setCor(updateAnimais.getCor());
            animais.setObservacoesAnimal(updateAnimais.getObservacoesAnimal());
            animais.setFichaMedicaAnimal(updateAnimais.getFichaMedicaAnimal());
            animais.setFotosAnimais(updateAnimais.getFotosAnimais());

            return animaisRepository.save(animais);
        });
    }
    
    @Override
    public void deleteById(UUID id) {
        animaisRepository.deleteById(id);
    }
}
