package com.PetMatch.PetMatchBackEnd.features.animais.services.impl;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import com.PetMatch.PetMatchBackEnd.features.animais.models.FotosAnimais;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.*;
import com.PetMatch.PetMatchBackEnd.features.animais.repositories.AnimaisRepository;
import com.PetMatch.PetMatchBackEnd.features.animais.repositories.FichaMedicaRepository;
import com.PetMatch.PetMatchBackEnd.features.animais.repositories.FotosAnimaisRepository;
import com.PetMatch.PetMatchBackEnd.features.animais.services.AnimaisService;
import com.PetMatch.PetMatchBackEnd.features.user.models.OngUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.OngUsuariosRepository;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.UsuarioRepository;
// import com.PetMatch.PetMatchBackEnd.shared.service.S3Service; // Não usado mais
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnimaisServiceImpl implements AnimaisService{

    private final AnimaisRepository animaisRepository;
    private final OngUsuariosRepository ongUsuariosRepository;
    private final UsuarioRepository usuarioRepository;
    private final FichaMedicaRepository fichaMedicaRepository;
    private final FotosAnimaisRepository fotosAnimaisRepository;
    // private final S3Service s3Service; // Removido uso do S3

    // --- CREATE COM URL (Lógica Nova) ---
    @Override
    @Transactional
    public AnimalResponseDto create(AnimalRegisterDto dto, UUID idUsuarioLogado, String perfilUsuario) {

        // 1. Validação de Permissão
        if (!"ONG".equalsIgnoreCase(perfilUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado: Somente ONGs podem cadastrar animais.");
        }

        // 2. Buscar o Objeto 'Usuario'
        Usuario usuario = usuarioRepository.findById(idUsuarioLogado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário de sistema não encontrado."));

        // 3. Buscar o perfil da ONG
        OngUsuarios ong = ongUsuariosRepository.findByUsuario(usuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não foi possível encontrar uma ONG associada ao usuário."));

        // 4. Criação da Entidade
        Animais animal = Animais.builder()
                .nome(dto.getNome())
                .idade(dto.getIdade())
                .porte(dto.getPorte())
                .sexo(dto.getSexo())
                .especie(dto.getEspecie())
                .raca(dto.getRaca())
                .cor(dto.getCor())
                .observacoesAnimal(dto.getObservacoesAnimal())
                .ong(ong) // Associa a ONG correta
                .build();

        // 5. ✨ MUDANÇA: Salva a URL direto (se existir)
        if (dto.getImagemUrl() != null && !dto.getImagemUrl().isBlank()) {
            FotosAnimais foto = FotosAnimais.builder()
                    .arquivoAnimal(dto.getImagemUrl()) // Salva o Link
                    .animal(animal)
                    .build();
            animal.setFotosAnimais(List.of(foto));
        }

        // 6. Persistência
        Animais salvo = animaisRepository.save(animal);

        // 7. Retorno
        return mapToDto(salvo);
    }

    // --- TODOS OS MÉTODOS ABAIXO FORAM MANTIDOS (Com lógica de segurança) ---

    @Override
    public List<AnimalResponseDto> findAllAnimaisDto(Usuario usuarioAutenticado) {
        String perfil = usuarioAutenticado.getAccessLevel().name();
        List<Animais> animais;

        // Se for Adotante ou Admin, vê tudo
        if ("ADOTANTE".equalsIgnoreCase(perfil) || "ADMIN".equalsIgnoreCase(perfil)) {
            animais = animaisRepository.findAll();
        }
        // Se for ONG, vê apenas os seus
        else if ("ONG".equalsIgnoreCase(perfil)) {
            OngUsuarios ong = ongUsuariosRepository.findByUsuario(usuarioAutenticado)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Perfil de ONG não encontrado."));
            animais = animaisRepository.findByOng(ong);
        }
        else {
            animais = List.of();
        }

        return animais.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AnimalResponseDto> findAnimalDtoById(UUID id, Usuario usuarioAutenticado) {
        Animais animal = animaisRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal não encontrado."));

        String perfil = usuarioAutenticado.getAccessLevel().name();

        if ("ADOTANTE".equalsIgnoreCase(perfil) || "ADMIN".equalsIgnoreCase(perfil)) {
            return Optional.of(mapToDto(animal));
        }

        if ("ONG".equalsIgnoreCase(perfil)) {
            OngUsuarios ongLogada = ongUsuariosRepository.findByUsuario(usuarioAutenticado)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Perfil de ONG não encontrado."));

            if (animal.getOng() != null && animal.getOng().getId().equals(ongLogada.getId())) {
                return Optional.of(mapToDto(animal));
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para visualizar este animal.");
            }
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Perfil de usuário inválido para esta ação.");
    }

    @Override
    public AnimalResponseDto update(UUID id, AnimalRegisterDto updateDto, UUID idUsuarioLogado, String perfilUsuario) {
        if (!"ONG".equalsIgnoreCase(perfilUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado: Somente ONGs podem atualizar animais.");
        }

        Usuario usuario = usuarioRepository.findById(idUsuarioLogado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado."));

        OngUsuarios ongLogada = ongUsuariosRepository.findByUsuario(usuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Perfil de ONG não encontrado."));

        Animais animal = animaisRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal não encontrado com o ID: " + id));

        if (animal.getOng() == null || !animal.getOng().getId().equals(ongLogada.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado: Você não tem permissão para atualizar este animal.");
        }

        animal.setNome(updateDto.getNome());
        animal.setIdade(updateDto.getIdade());
        animal.setPorte(updateDto.getPorte());
        animal.setSexo(updateDto.getSexo());
        animal.setEspecie(updateDto.getEspecie());
        animal.setRaca(updateDto.getRaca());
        animal.setCor(updateDto.getCor());
        animal.setObservacoesAnimal(updateDto.getObservacoesAnimal());

        Animais animalSalvo = animaisRepository.save(animal);
        return mapToDto(animalSalvo);
    }

    @Override
    public void deleteById(UUID id, UUID idUsuarioLogado, String perfilUsuario) {
        if (!"ONG".equalsIgnoreCase(perfilUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado: Somente ONGs podem deletar animais.");
        }

        Usuario usuario = usuarioRepository.findById(idUsuarioLogado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado."));

        OngUsuarios ongLogada = ongUsuariosRepository.findByUsuario(usuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Perfil de ONG não encontrado."));

        Animais animal = animaisRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal não encontrado com o ID: " + id));

        if (animal.getOng() == null || !animal.getOng().getId().equals(ongLogada.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado: Você não tem permissão para deletar este animal.");
        }

        animaisRepository.delete(animal);
    }


    // --- MÉTODOS HELPER (Mapeamento para DTO) ---

    private AnimalResponseDto mapToDto(Animais salvo) {
        OngUsuarios ong = salvo.getOng();
        OngSimplificadaDTO ongDto = (ong != null) ?
                OngSimplificadaDTO.builder().nomeFantasiaOng(ong.getNomeFantasiaOng()).build() :
                null;

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
                .ong(ongDto)
                .fotosAnimais(mapToFotosDto(salvo.getFotosAnimais()))
                .build();
    }

    private List<FotoAnimalDTO> mapToFotosDto(List<FotosAnimais> fotos) {
        if (fotos == null || fotos.isEmpty()) {
            return List.of();
        }
        return fotos.stream()
                .map(foto -> FotoAnimalDTO.builder()
                        .idFotoAnimal(foto.getIdFotoAnimal())
                        .url(foto.getArquivoAnimal())
                        .build())
                .collect(Collectors.toList());
    }

    // --- Métodos antigos da interface (se necessário manter para compilar) ---
    @Override public Optional<Animais> findById(UUID id) { return animaisRepository.findById(id); }
    @Override public List<Animais> findAll() { return animaisRepository.findAll(); }
    @Override public List<Animais> findAllWithQueries(AnimalSearch animalSearch) { return List.of(); }
    @Override public Optional<Animais> update(UUID id, Animais updateAnimais) { return Optional.empty(); }
    @Override public void deleteById(UUID id) { animaisRepository.deleteById(id); }

    // Este overload existe para compatibilidade com a Interface caso você não a tenha mudado
    // Mas chamamos a versão nova internamente
    public AnimalResponseDto create(AnimalRegisterDto dto, org.springframework.web.multipart.MultipartFile file, UUID idUser, String perfil) {
        return create(dto, idUser, perfil);
    }
}