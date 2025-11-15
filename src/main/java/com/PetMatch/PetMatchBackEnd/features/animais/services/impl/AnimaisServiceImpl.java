package com.PetMatch.PetMatchBackEnd.features.animais.services.impl;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import com.PetMatch.PetMatchBackEnd.features.animais.models.FichaMedicaAnimal;
import com.PetMatch.PetMatchBackEnd.features.animais.models.FotosAnimais;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.*;
import com.PetMatch.PetMatchBackEnd.features.animais.repositories.AnimaisRepository;
import com.PetMatch.PetMatchBackEnd.features.animais.repositories.FichaMedicaRepository;
import com.PetMatch.PetMatchBackEnd.features.animais.repositories.FotosAnimaisRepository;
import com.PetMatch.PetMatchBackEnd.features.animais.services.AnimaisService;
import com.PetMatch.PetMatchBackEnd.features.user.models.OngUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.OngUsuariosRepository;
import com.PetMatch.PetMatchBackEnd.shared.service.S3Service;
import jakarta.persistence.EntityNotFoundException;
import com.PetMatch.PetMatchBackEnd.shared.service.S3Service;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnimaisServiceImpl implements AnimaisService{

    private final AnimaisRepository animaisRepository;
    private final OngUsuariosRepository ongUsuariosRepository;
    private final FichaMedicaRepository fichaMedicaRepository;
    private final FotosAnimaisRepository fotosAnimaisRepository;
    private final S3Service s3Service;


    public AnimaisServiceImpl(AnimaisRepository animaisRepository, OngUsuariosRepository ongUsuariosRepository, FichaMedicaRepository fichaMedicaRepository, FotosAnimaisRepository fotosAnimaisRepository, S3Service s3Service) {
        this.animaisRepository = animaisRepository;
        this.ongUsuariosRepository = ongUsuariosRepository;
        this.fichaMedicaRepository = fichaMedicaRepository;
        this.fotosAnimaisRepository = fotosAnimaisRepository;
        this.s3Service = s3Service;
        //this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public AnimalResponseDto create(AnimalRegisterDto dto, MultipartFile file, Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        OngUsuarios ong = ongUsuariosRepository.findByUsuario(usuario)
                .orElseThrow(() -> new AccessDeniedException("Usuário não é uma ONG"));

        // === LÓGICA DE UPLOAD ===
        String urlDaImagem = null;
        if (file != null && !file.isEmpty()) {
            try {
                urlDaImagem = s3Service.uploadFile(file); // <-- CHAMA O S3
            } catch (IOException e) {
                // Lide com o erro de upload aqui, talvez logando
                System.err.println("Erro ao fazer upload do arquivo: " + e.getMessage());
                // Você pode escolher lançar uma exceção ou continuar sem imagem
            }
        }
        // === FIM DO UPLOAD ===

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

        // === VINCULA A FOTO AO ANIMAL (ANTES DE SALVAR) ===
        if (urlDaImagem != null) {
            FotosAnimais foto = FotosAnimais.builder()
                    .arquivoAnimal(urlDaImagem) // Salva a URL retornada pelo S3
                    .animal(animal)
                    .build();
            // Adiciona a foto à lista de fotos do animal
            animal.setFotosAnimais(List.of(foto));
        }

        Animais salvo = animaisRepository.save(animal);

        // Retorna o DTO completo usando o helper
        return mapToDto(salvo);
    }

    // 4. ATUALIZAR OS HELPERS 'mapToDto' e 'mapToFotosDto'

    @Override
    public List<AnimalResponseDto> findAllAnimaisDto() {
        return animaisRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Helper PRIVADO para converter Entidade para DTO.
     */
    private AnimalResponseDto mapToDto(Animais salvo) {
        OngUsuarios ong = salvo.getOng();

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
                        .nomeFantasiaOng(ong != null ? ong.getNomeFantasiaOng() : null)
                        .build())
                // MUDANÇA: Mapeia as fotos
                .fotosAnimais(mapToFotosDto(salvo.getFotosAnimais()))
                .build();
    }

    /**
     * Helper PRIVADO para converter Lista de Entidades de Foto para Lista de DTOs de Foto
     */
    private List<FotoAnimalDTO> mapToFotosDto(List<FotosAnimais> fotos) {
        if (fotos == null || fotos.isEmpty()) {
            return List.of(); // Retorna lista vazia
        }

        // Mapeia os campos da Entidade para o DTO
        return fotos.stream()
                .map(foto -> FotoAnimalDTO.builder()
                        .idFotoAnimal(foto.getIdFotoAnimal())// Entidade -> DTO
                        .url(foto.getArquivoAnimal())         // Entidade -> DTO
                        .build())
                .collect(Collectors.toList());
    }

    // O resto dos seus métodos (findById, findAll, findAllWithQueries, etc.) continua aqui...

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
