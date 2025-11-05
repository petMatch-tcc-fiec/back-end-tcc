package com.PetMatch.PetMatchBackEnd.features.animais.services.impl;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;

import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalSearch;
import com.PetMatch.PetMatchBackEnd.features.animais.repositories.AnimaisRepository;
import com.PetMatch.PetMatchBackEnd.features.animais.services.AnimaisService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnimaisServiceImpl implements AnimaisService{

    private final AnimaisRepository animaisRepository;

    public AnimaisServiceImpl(AnimaisRepository animaisRepository) {
        this.animaisRepository = animaisRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    public Animais create(Animais registerAnimais) {
        Animais animal = new Animais();
        animal.setNome(registerAnimais.getNome());
        animal.setIdade(registerAnimais.getIdade());
        animal.setPorte(registerAnimais.getPorte());
        animal.setSexo(registerAnimais.getSexo());
        animal.setEspecie(registerAnimais.getEspecie());
        animal.setRaca(registerAnimais.getRaca());
        animal.setCor(registerAnimais.getCor());
        animal.setObservacoesAnimal(registerAnimais.getObservacoesAnimal());
        animal.setFichaMedicaAnimal(registerAnimais.getFichaMedicaAnimal());
        animal.setFotosAnimais(registerAnimais.getFotosAnimais());

        return animaisRepository.save(animal);
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
