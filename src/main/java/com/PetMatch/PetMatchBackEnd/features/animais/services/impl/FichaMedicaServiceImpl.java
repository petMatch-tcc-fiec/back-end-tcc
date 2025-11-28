package com.PetMatch.PetMatchBackEnd.features.animais.services.impl;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import com.PetMatch.PetMatchBackEnd.features.animais.models.FichaMedicaAnimal;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.FichaMedicaDTO;
import com.PetMatch.PetMatchBackEnd.features.animais.repositories.AnimaisRepository;
import com.PetMatch.PetMatchBackEnd.features.animais.repositories.FichaMedicaRepository;
import com.PetMatch.PetMatchBackEnd.features.animais.services.FichaMedicaService;
import com.PetMatch.PetMatchBackEnd.features.user.models.OngUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.OngUsuariosRepository;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class FichaMedicaServiceImpl implements FichaMedicaService {

    @Autowired
    private FichaMedicaRepository fichaMedicaRepository;
    @Autowired
    private AnimaisRepository animaisRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private OngUsuariosRepository ongUsuariosRepository;

    @Override
    @Transactional
    public FichaMedicaDTO salvarOuAtualizar(UUID idAnimal, FichaMedicaDTO dto, UUID idUsuarioLogado) {
        // 1. Validar Segurança: Usuário existe? É ONG? É dono do animal?
        Usuario usuario = usuarioRepository.findById(idUsuarioLogado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        OngUsuarios ong = ongUsuariosRepository.findByUsuario(usuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas ONGs podem editar fichas médicas."));

        Animais animal = animaisRepository.findById(idAnimal)
                .orElseThrow(() -> new EntityNotFoundException("Animal não encontrado"));

        if (!animal.getOng().getId().equals(ong.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Este animal não pertence à sua ONG.");
        }

        // 2. Buscar ficha existente ou criar nova
        FichaMedicaAnimal ficha = fichaMedicaRepository.findByAnimalId(idAnimal)
                .orElse(new FichaMedicaAnimal());

        // 3. Atualizar dados
        ficha.setAnimal(animal); // Vincula ao animal se for novo
        ficha.setVacinas(dto.getVacinas());
        ficha.setHistoricoSaude(dto.getHistoricoSaude());

        // 4. Salvar
        ficha = fichaMedicaRepository.save(ficha);

        // 5. Retornar DTO
        return FichaMedicaDTO.builder()
                .idFicha(ficha.getIdFichaMedica()) // Verifique se na sua entidade chama idFichaMedica ou id
                .idAnimal(animal.getId())
                .vacinas(ficha.getVacinas())
                .historicoSaude(ficha.getHistoricoSaude())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public FichaMedicaDTO buscarPorAnimal(UUID idAnimal) {
        // Verifica se animal existe
        if (!animaisRepository.existsById(idAnimal)) {
            throw new EntityNotFoundException("Animal não encontrado");
        }

        // Busca ficha. Se não tiver, retorna DTO vazio (para o front não quebrar) ou null.
        return fichaMedicaRepository.findByAnimalId(idAnimal)
                .map(ficha -> FichaMedicaDTO.builder()
                        .idFicha(ficha.getIdFichaMedica())
                        .idAnimal(idAnimal)
                        .vacinas(ficha.getVacinas())
                        .historicoSaude(ficha.getHistoricoSaude())
                        .build())
                .orElse(null);
    }
}
