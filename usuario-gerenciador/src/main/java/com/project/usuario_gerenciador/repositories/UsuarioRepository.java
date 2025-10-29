package com.project.usuario_gerenciador.repositories;

import com.project.usuario_gerenciador.models.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);

    @Query(value = "SELECT * FROM usuarios u WHERE " +
          "(:status IS NULL OR u.status = :status) AND " +
          "(:search IS NULL OR LOWER(u.nome) LIKE LOWER('%' || :search || '%'))",
          countQuery = "SELECT COUNT(*) FROM usuarios u WHERE " +
          "(:status IS NULL OR u.status = :status) AND " +
          "(:search IS NULL OR LOWER(u.nome) LIKE LOWER('%' || :search || '%'))",
          nativeQuery = true)
    Page<Usuario> findAllWithFilters(
            @Param("status") String status,
            @Param("search") String search,
            Pageable pageable
    );
}

