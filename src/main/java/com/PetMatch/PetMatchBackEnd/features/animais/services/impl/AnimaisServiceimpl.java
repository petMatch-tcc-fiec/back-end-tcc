package com.PetMatch.PetMatchBackEnd.features.animais.services.impl;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;

import com.PetMatch.PetMatchBackEnd.features.user.models.OngUsuarios;
import com.PetMatch.PetMatchBackEnd.features.animais.repositories.AnimaisRepository;
import com.PetMatch.PetMatchBackEnd.features.animais.services.AnimaisService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnimaisServiceimpl implements AnimaisService{

    private final AnimaisRepository animaisRepository;

    public AnimaisServiceimpl(AnimaisRepository animaisRepository) {
        this.animaisRepository = animaisRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    public Animais create(Animais registerAnimais) {
        Animais animal = new Animais();
        animal.setNome(registerAnimais.getNome());
        animal.setIdade(registerAnimais.getIdade());
        animal.setPorte(registerAnimais.getPorte());
        animal.setSexo(registerAnimais.getSexo());
        animal.setTipo(registerAnimais.getTipo());
        animal.setRaca(registerAnimais.getRaca());
        animal.setCor(registerAnimais.getCor());
        animal.setObservacoesAnimal(registerAnimais.getObservacoesAnimal());
        animal.setFichaMedicaAnimal(registerAnimais.getFichaMedicaAnimal());
        animal.setFotosAnimais(registerAnimais.getFotosAnimais());


        return animaisRepository.save(animal);
    }

    @Override
    public OngUsuarios save(Animais animais) {
        return null;
    }

    @Override
    public Optional<Animais> findById(UUID id) {
        return animaisRepository.findById(id);
    }

    @Override
    public Optional<Animais> findBySexo(String sexo) {
        return animaisRepository.findBySexo(sexo);
    }

    @Override
    public Optional<Animais> findByRaca(String raca) {
        return Optional.empty();
    }

    @Override
    public List<Animais> findAll() {
        return animaisRepository.findAll();
    }

    @Override
    public Optional<Animais> update(UUID id, Animais updateAnimais) {
        return animaisRepository.findById(id).map(animais -> {
            animais.setNome(updateAnimais.getNome());
            animais.setIdade(updateAnimais.getIdade());
            animais.setPorte(updateAnimais.getPorte());
            animais.setSexo(updateAnimais.getSexo());
            animais.setTipo(updateAnimais.getTipo());
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

    }
}
