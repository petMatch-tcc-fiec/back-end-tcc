package com.PetMatch.PetMatchBackEnd.shared.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("/images")
@Tag(name = "Image Controller", description = "Serviço responsável por disponibilizar imagens e miniaturas (thumbnails).")
public class ImageController {

    public static final String UPLOAD_DIR = "uploads";
    public static final String THUMBNAIL_DIR = "thumbnails";
    public static final int THUMBNAIL_SIZE = 150;

    public ImageController() {
        createDirectoryIfNotExists(UPLOAD_DIR);
        createDirectoryIfNotExists(THUMBNAIL_DIR);
    }

    private void createDirectoryIfNotExists(String directoryName) {
        File directory = new File(directoryName);
        if (!directory.exists() && !directory.mkdirs()) {
            log.error("Falha ao criar diretório: {}", directoryName);
        }
    }

    @Operation(
            summary = "Obtém uma imagem",
            description = "Retorna uma imagem do diretório de uploads ou uma versão em miniatura (thumbnail) se especificado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Imagem retornada com sucesso",
                            content = @Content(mediaType = "image/*",
                                    schema = @Schema(implementation = Resource.class))),
                    @ApiResponse(responseCode = "404", description = "Imagem não encontrada"),
                    @ApiResponse(responseCode = "500", description = "Erro interno ao ler a imagem")
            }
    )
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getImage(
            @Parameter(description = "Nome do arquivo da imagem (com extensão)", example = "foto1.png")
            @PathVariable String filename,

            @Parameter(description = "Indica se deve retornar a miniatura da imagem", example = "false")
            @RequestParam(value = "thumbnail", defaultValue = "false") boolean thumbnail) {

        try {
            String directory = thumbnail ? THUMBNAIL_DIR : UPLOAD_DIR;
            String fileName = thumbnail ? "thumb_" + filename : filename;
            Path filePath = Paths.get(directory, fileName);

            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(filePath.toUri());
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (IOException e) {
            log.error("Erro ao ler a imagem: {}", filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
