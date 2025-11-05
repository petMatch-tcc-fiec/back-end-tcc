package com.PetMatch.PetMatchBackEnd.features.animais.repositories.impl;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalSearch;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.SortOrder;
import com.PetMatch.PetMatchBackEnd.features.animais.repositories.AnimalCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AnimalCustomRepositoryImpl implements AnimalCustomRepository {

    private final EntityManager entityManager;

    public AnimalCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Animais> findAnimais(AnimalSearch animalSearch) {

    // 1. Inicialização da Criteria API
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Animais> cq = cb.createQuery(Animais.class);
    Root<Animais> Animais = cq.from(Animais.class); // Define a entidade raiz (FROM Animais p)

    List<Predicate> predicates = new ArrayList<>();

    // 2. Constrói as Cláusulas WHERE (Predicates) dinamicamente

    // Critério: Nome (Busca parcial, case-insensitive LIKE
        if (animalSearch.getNome() != null && !animalSearch.getNome().trim().isEmpty()) {
        // cb.like(cb.upper(Animais.get("nome")), "%NOME%")
        Predicate nomePredicate = cb.like(
                cb.upper(Animais.get("nome")),
                "%" + animalSearch.getNome().toUpperCase().trim() + "%"
        );
        predicates.add(nomePredicate);
    }

    // Critério: Idade (Busca por idade exata)
        if (animalSearch.getIdade() != null) {
        // cb.equal(Animais.get("idade"), idade)
        Predicate idadePredicate = cb.equal(Animais.get("idade"), animalSearch.getIdade());
        predicates.add(idadePredicate);
    }

    // Critério: porte
        if (animalSearch.getPorte() != null) {
        // cb.equal(Animais.get("porte"), porte)
        Predicate portePredicate = cb.equal(Animais.get("porte"), animalSearch.getPorte());
        predicates.add(portePredicate);
    }

        if (animalSearch.getCor() != null) {
            // cb.equal(Animais.get("cor"), cor)
            Predicate corPredicate = cb.equal(Animais.get("cor"), animalSearch.getCor());
            predicates.add(corPredicate);
        }

        if (animalSearch.getRaca() != null) {
            // cb.equal(Animais.get("raça"), raca)
            Predicate racaPredicate = cb.equal(Animais.get("raca"), animalSearch.getRaca());
            predicates.add(racaPredicate);
        }
        if (animalSearch.getEspecie() != null) {
            // cb.equal(Animais.get("especie"), especie)
            Predicate especiePredicate = cb.equal(Animais.get("especie"), animalSearch.getEspecie());
            predicates.add(especiePredicate);
        }
        if (animalSearch.getSexo() != null) {
            // cb.equal(Animais.get("sexo"), sexo)
            Predicate sexoPredicate = cb.equal(Animais.get("sexo"), animalSearch.getSexo());
            predicates.add(sexoPredicate);
        }

    // Combina todas as condições com AND
        if (!predicates.isEmpty()) {
        cq.where(cb.and(predicates.toArray(new Predicate[0])));
    }

    // 3. Constrói a Cláusula ORDER BY dinamicamente
    String sortBy = animalSearch.getSortBy();
    SortOrder sortOrder = animalSearch.getSortOrder() != null ? animalSearch.getSortOrder() : SortOrder.ASC;

        if (sortBy != null && !sortBy.trim().isEmpty()) {
        // Garantir que a string de ordenação mapeia para o campo correto
        String safeSortBy = getSafeSortField(sortBy);

        Order order;
        if (sortOrder == SortOrder.DESC) {
            // cb.desc(Animais.get(safeSortBy))
            order = cb.desc(Animais.get(safeSortBy));
        } else {
            // cb.asc(Animais.get(safeSortBy))
            order = cb.asc(Animais.get(safeSortBy));
        }

        cq.orderBy(order);
    } else {
        // Ordenação default
        cq.orderBy(cb.asc(Animais.get("id")));
    }

    // 4. Cria e executa a TypedQuery
    TypedQuery<Animais> query = entityManager.createQuery(cq);

        return query.getResultList();
}

// Método auxiliar para mapear/validar o campo de ordenação (segurança)
private String getSafeSortField(String sortBy) {
    // Exemplo: Mapear nomes de campos da AnimaisSearch para a entidade Animais
    switch (sortBy.toLowerCase()) {
        case "nome":
            return "nome";
        case "idade":
            return "idade";
        case "sexo":
            return "sexo";
        case "cor":
            return "cor";
        case "especie":
            return "especie";
        case "raca":
            return "raca";
        case "porte":
            return "porte";
        // Adicione outros campos de ordenação válidos
        case "id":
        default:
            return "id";
    }
}
}
