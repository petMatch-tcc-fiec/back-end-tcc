package com.PetMatch.PetMatchBackEnd.features.animais.services.impl;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalRegisterDto;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalSearch;
import com.PetMatch.PetMatchBackEnd.features.animais.repositories.AnimaisRepository;
import com.PetMatch.PetMatchBackEnd.features.animais.services.AnimaisService;
import com.PetMatch.PetMatchBackEnd.features.user.models.OngUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.OngUsuariosRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnimaisServiceImpl implements AnimaisService{

    private final AnimaisRepository animaisRepository;
    private final OngUsuariosRepository ongUsuariosRepository;

    public AnimaisServiceImpl(AnimaisRepository animaisRepository, OngUsuariosRepository ongUsuariosRepository) {
        this.animaisRepository = animaisRepository;
        this.ongUsuariosRepository = ongUsuariosRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Animais create(Animais animais) {
        Animais animal = Animais.builder()
                .nome(animais.getNome())
                .idade(animais.getIdade())
                .porte(animais.getPorte())
                .sexo(animais.getSexo())
                .especie(animais.getEspecie())
                .raca(animais.getRaca())
                .cor(animais.getCor())
                .observacoesAnimal(animais.getObservacoesAnimal())
                .ong(ong)
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
