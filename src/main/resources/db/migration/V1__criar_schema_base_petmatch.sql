-- Flyway Migration V1
-- Descrição: Criação do esquema base petmatchdb, corrigindo sintaxe de FKs e implementando a tabela Usuarios.

-- NOTA: Os campos 'accessLevel' e 'state' foram renomeados para 'access_level' e 'register_state'
-- para seguir o padrão snake_case do MySQL/JPA e evitar o erro 'Unknown column' (500) anterior.
-- Certifique-se de atualizar sua entidade Usuario.java com: @Column(name="access_level")

--
-- Table structure for table `usuarios` (TABELA BASE)
--
CREATE TABLE `usuarios` (
    `id` BINARY(16) NOT NULL, -- UUIDs
    `name` VARCHAR(255),
    `email` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `picture` VARCHAR(255),
    `fcm_token` VARCHAR(255),
    `register_state` VARCHAR(50) NOT NULL,   -- Renomeado para seguir o snake_case
    `access_level` enum('ADMIN', 'ADOTANTE', 'ONG') NOT NULL,     -- Renomeado para seguir o snake_case
    PRIMARY KEY (`id`),
    UNIQUE KEY `email_usuario` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------------

--
-- Table structure for table `adminusuarios`
--
CREATE TABLE `adminusuarios` (
    `id` BINARY(16) NOT NULL,
    `usuario_id` BINARY(16) NOT NULL, -- Coluna FK obrigatória
    `nome_admin` VARCHAR(255) NOT NULL,
    `email_admin` VARCHAR(100) NOT NULL,
    `senha_admin` VARCHAR(100) NOT NULL,
    `cpf_cnpj_admin` VARCHAR(20) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email_admin` (`email_admin`),
    UNIQUE KEY `cpf_cnpj_admin` (`cpf_cnpj_admin`),
    UNIQUE KEY `unq_admin_usuario` (`usuario_id`), -- Garante o relacionamento 1:1 com Usuarios
    CONSTRAINT `fk_admin_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `adotanteusuarios`
--
CREATE TABLE `adotanteusuarios` (
    `id` BINARY(16) NOT NULL,
    `usuario_id` BINARY(16) NOT NULL, -- Coluna FK obrigatória
    `nome_adotante` VARCHAR(100) NOT NULL,
    `cpf_adotante` VARCHAR(14) NOT NULL,
    `endereco_adotante` VARCHAR(255) NOT NULL,
    `celular_adotante` VARCHAR(20) DEFAULT NULL,
    `email_adotante` VARCHAR(100) DEFAULT NULL,
    `senha_adotante` VARCHAR(100) NOT NULL,
    `descricao_outros_animais` VARCHAR(255) DEFAULT NULL,
    `preferencia` VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `cpf_adotante` (`cpf_adotante`),
    UNIQUE KEY `email_adotante` (`email_adotante`),
    UNIQUE KEY `unq_adotante_usuario` (`usuario_id`), -- Garante o relacionamento 1:1 com Usuarios
    CONSTRAINT `fk_adotante_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `ongusuarios`
--
CREATE TABLE `ongusuarios` (
    `id` BINARY(16) NOT NULL,
    `usuario_id` BINARY(16) NOT NULL, -- Coluna FK obrigatória
    `nome_fantasia_ong` VARCHAR(255) NOT NULL,
    `email_ong` VARCHAR(100) NOT NULL,
    `senha_ong` VARCHAR(100) NOT NULL,
    `endereco_ong` VARCHAR(255) NOT NULL,
    `telefone_ong` VARCHAR(20) DEFAULT NULL,
    `celular_ong` VARCHAR(20) DEFAULT NULL,
    `cnpj_ong` VARCHAR(20) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email_ong` (`email_ong`),
    UNIQUE KEY `cnpj_ong` (`cnpj_ong`),
    UNIQUE KEY `unq_ong_usuario` (`usuario_id`), -- Garante o relacionamento 1:1 com Usuarios
    CONSTRAINT `fk_ong_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `animais`
--
CREATE TABLE `animais` (
    `id_animal` binary(16) NOT NULL,
    `nome_animal` varchar(100) NOT NULL,
    `idade_animal` int DEFAULT NULL,
    `porte_animal` varchar(10) NOT NULL,
    `sexo_animal` char(1) NOT NULL,
    `especie_animal` varchar(50) NOT NULL,
    `raca_animal` varchar(50) DEFAULT NULL,
    `cor_animal` varchar(50) DEFAULT NULL,
    `observacoes_animal` varchar(255) DEFAULT NULL,
    `fk_ongusuarios_id_ong` binary(16) NOT NULL,
    PRIMARY KEY (`id_animal`),
    KEY `fk_animal_ong` (`fk_ongusuarios_id_ong`),
    CONSTRAINT `fk_animal_ong` FOREIGN KEY (`fk_ongusuarios_id_ong`) REFERENCES `ongusuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `adocao`
--
CREATE TABLE `adocao` (
    `fk_animais_id_animal` binary(16) NOT NULL,
    `fk_adotanteusuarios_id_adotante` binary(16) NOT NULL,
    `data_adocao` date NOT NULL,
    `adocao_status` char(1) NOT NULL,
    PRIMARY KEY (`fk_animais_id_animal`,`fk_adotanteusuarios_id_adotante`),
    KEY `fk_adotam_adotante` (`fk_adotanteusuarios_id_adotante`),
    CONSTRAINT `fk_adotam_adotante` FOREIGN KEY (`fk_adotanteusuarios_id_adotante`) REFERENCES `adotanteusuarios` (`id`),
    CONSTRAINT `fk_adotam_animal` FOREIGN KEY (`fk_animais_id_animal`) REFERENCES `animais` (`id_animal`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `adocao_interesse`
--
CREATE TABLE `adocao_interesse` (
    `id_interesse` binary(16) NOT NULL,
    `data_de_criacao` datetime NOT NULL,
    `fk_animais_id_animal` binary(16) NOT NULL,
    `fk_adotanteusuarios_id_adotante` binary(16) NOT NULL,
    PRIMARY KEY (`id_interesse`),
    UNIQUE KEY `unq_adocao_interesse` (`fk_animais_id_animal`,`fk_adotanteusuarios_id_adotante`),
    KEY `fk_usuario_interesse` (`fk_adotanteusuarios_id_adotante`),
    CONSTRAINT `fk_animal_interesse` FOREIGN KEY (`fk_animais_id_animal`) REFERENCES `animais` (`id_animal`),
    CONSTRAINT `fk_usuario_interesse` FOREIGN KEY (`fk_adotanteusuarios_id_adotante`) REFERENCES `adotanteusuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `eventos`
--
CREATE TABLE `eventos` (
    `id_evento` binary(16) NOT NULL,
    `nome_evento` varchar(255) NOT NULL,
    `data_hora_evento` datetime NOT NULL,
    `endereco_evento` varchar(255) NOT NULL,
    `fk_ongusuarios_id_ong` binary(16) NOT NULL,
    PRIMARY KEY (`id_evento`),
    KEY `fk_evento_ong` (`fk_ongusuarios_id_ong`),
    CONSTRAINT `fk_evento_ong` FOREIGN KEY (`fk_ongusuarios_id_ong`) REFERENCES `ongusuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `fichamedicaanimal`
--
CREATE TABLE `fichamedicaanimal` (
    `id_ficha_medica` binary(16) NOT NULL,
    `vacinas` varchar(255) DEFAULT NULL,
    `historico_saude` varchar(255) DEFAULT NULL,
    `fk_animais_id_animal` binary(16) NOT NULL,
    PRIMARY KEY (`id_ficha_medica`),
    UNIQUE KEY `fk_animais_id_animal` (`fk_animais_id_animal`),
    CONSTRAINT `fk_ficha_animal` FOREIGN KEY (`fk_animais_id_animal`) REFERENCES `animais` (`id_animal`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `fotosadotante`
--
CREATE TABLE `fotosadotante` (
    `id_foto` binary(16) NOT NULL,
    `arquivo` varchar(255) NOT NULL,
    `fk_adotanteusuarios_id_adotante` binary(16) NOT NULL,
    PRIMARY KEY (`id_foto`),
    KEY `fk_foto_adotante` (`fk_adotanteusuarios_id_adotante`),
    CONSTRAINT `fk_foto_adotante` FOREIGN KEY (`fk_adotanteusuarios_id_adotante`) REFERENCES `adotanteusuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `fotosanimais`
--
CREATE TABLE `fotosanimais` (
    `id_foto_animal` binary(16) NOT NULL,
    `arquivo_animal` varchar(255) NOT NULL,
    `fk_animais_id_animal` binary(16) NOT NULL,
    PRIMARY KEY (`id_foto_animal`),
    KEY `fk_foto_animal` (`fk_animais_id_animal`),
    CONSTRAINT `fk_foto_animal` FOREIGN KEY (`fk_animais_id_animal`) REFERENCES `animais` (`id_animal`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;