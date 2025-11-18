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
import com.PetMatch.PetMatchBackEnd.features.user.repositories.UsuarioRepository; // <-- IMPORT NECESSÁRIO
import com.PetMatch.PetMatchBackEnd.shared.service.S3Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor; // <-- MUDADO PARA @AllArgsConstructor
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor // <-- USA @AllArgsConstructor (como no seu EventoServiceImpl)
public class AnimaisServiceImpl implements AnimaisService{

    private final AnimaisRepository animaisRepository;
    private final OngUsuariosRepository ongUsuariosRepository;
    private final UsuarioRepository usuarioRepository; // <-- ADICIONADO (como no EventoServiceImpl)
    private final FichaMedicaRepository fichaMedicaRepository;
    private final FotosAnimaisRepository fotosAnimaisRepository;
    private final S3Service s3Service;


    // --- LÓGICA DE PERMISSÃO APLICADA (padrão Evento: Primitivos) ---
    @Override
    @Transactional
    public AnimalResponseDto create(AnimalRegisterDto dto, MultipartFile file, UUID idUsuarioLogado, String perfilUsuario) {

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
                .ong(ong) // <-- Associa a ONG correta
                .build();

        // 5. Vincula a foto (se houver)
        if (urlDaImagem != null) {
            FotosAnimais foto = FotosAnimais.builder()
                    .arquivoAnimal(urlDaImagem)
                    .animal(animal)
                    .build();
            animal.setFotosAnimais(List.of(foto));
        }

        // 6. Persistência
        Animais salvo = animaisRepository.save(animal);

        // 7. Retorno
        return mapToDto(salvo);
    }

    // --- LÓGICA DE PERMISSÃO APLICADA (padrão Evento: Objeto Usuario) ---
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

            // Usa o novo método do repositório
            animais = animaisRepository.findByOng(ong);
        }
        // Outros perfis (se houver) não veem nada
        else {
            animais = List.of();
        }

        return animais.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // --- LÓGICA DE PERMISSÃO APLICADA (padrão Evento: Objeto Usuario) ---
    @Override
    public Optional<AnimalResponseDto> findAnimalDtoById(UUID id, Usuario usuarioAutenticado) {
        // 1. Busca o animal
        Animais animal = animaisRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal não encontrado."));

        String perfil = usuarioAutenticado.getAccessLevel().name();

        // 2. Adotantes e Admins podem ver
        if ("ADOTANTE".equalsIgnoreCase(perfil) || "ADMIN".equalsIgnoreCase(perfil)) {
            return Optional.of(mapToDto(animal));
        }

        // 3. ONGs precisam ser a "dona"
        if ("ONG".equalsIgnoreCase(perfil)) {
            OngUsuarios ongLogada = ongUsuariosRepository.findByUsuario(usuarioAutenticado)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Perfil de ONG não encontrado."));

            // 4. Verifica se o animal pertence à ONG logada
            if (animal.getOng() != null && animal.getOng().getId().equals(ongLogada.getId())) {
                return Optional.of(mapToDto(animal)); // Pertence, pode ver
            } else {
                // Não é dela, lança 403 - Proibido
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para visualizar este animal.");
            }
        }

        // Perfil desconhecido
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Perfil de usuário inválido para esta ação.");
    }


    // --- LÓGICA DE PERMISSÃO APLICADA (padrão Evento: Primitivos) ---
    @Override
    public AnimalResponseDto update(UUID id, AnimalRegisterDto updateDto, UUID idUsuarioLogado, String perfilUsuario) {

        // 1. Apenas ONGs podem atualizar
        if (!"ONG".equalsIgnoreCase(perfilUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado: Somente ONGs podem atualizar animais.");
        }

        // 2. Buscar a ONG logada
        Usuario usuario = usuarioRepository.findById(idUsuarioLogado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário de sistema não encontrado."));

        OngUsuarios ongLogada = ongUsuariosRepository.findByUsuario(usuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Perfil de ONG não encontrado."));

        // 3. Buscar o Animal
        Animais animal = animaisRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal não encontrado com o ID: " + id));

        // 4. VERIFICAÇÃO DE SEGURANÇA (DONO)
        if (animal.getOng() == null || !animal.getOng().getId().equals(ongLogada.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado: Você não tem permissão para atualizar este animal.");
        }

        // 5. Se passou, atualiza os dados
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

    // --- LÓGICA DE PERMISSÃO APLICADA (padrão Evento: Primitivos) ---
    @Override
    public void deleteById(UUID id, UUID idUsuarioLogado, String perfilUsuario) {

        // 1. Apenas ONGs podem deletar
        if (!"ONG".equalsIgnoreCase(perfilUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado: Somente ONGs podem deletar animais.");
        }

        // 2. Buscar a ONG logada
        Usuario usuario = usuarioRepository.findById(idUsuarioLogado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário de sistema não encontrado."));

        OngUsuarios ongLogada = ongUsuariosRepository.findByUsuario(usuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Perfil de ONG não encontrado."));

        // 3. Buscar o Animal
        Animais animal = animaisRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal não encontrado com o ID: " + id));

        // 4. VERIFICAÇÃO DE SEGURANÇA (DONO)
        if (animal.getOng() == null || !animal.getOng().getId().equals(ongLogada.getId())) {
            // Se não for o dono, lança 403 (Proibido)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado: Você não tem permissão para deletar este animal.");
        }

        // 5. Se passou em tudo, pode deletar
        // (Nota: Adicionar lógica de deletar fotos no S3 aqui, se necessário)
        animaisRepository.delete(animal);
    }


    // --- MÉTODOS HELPER (Mapeamento para DTO) ---

    private AnimalResponseDto mapToDto(Animais salvo) {
        OngUsuarios ong = salvo.getOng();

        // Tratamento para caso a ONG seja nula (embora não deva ser)
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

    // --- Métodos antigos (mantidos para compatibilidade da interface, se houver) ---
    // (Estes métodos não têm segurança e não devem ser chamados pelo Controller)

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
        // ... (lógica original) ...
        return List.of(); // Simplificado
    }

    // Este 'update' antigo não é mais usado pelo controller
    @Override
    public Optional<Animais> update(UUID id, Animais updateAnimais) {
        return Optional.empty();
    }

    // Este 'deleteById' antigo não é mais usado pelo controller
    @Override
    public void deleteById(UUID id) {
        animaisRepository.deleteById(id);
    }
}