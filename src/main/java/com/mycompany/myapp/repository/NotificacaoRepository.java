package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Notificacao;
import java.util.Optional; // Para Optional
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param; // Para @Param
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Notificacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    @Query("SELECT n FROM Notificacao n LEFT JOIN FETCH n.convidados WHERE n.id = :id")
    Optional<Notificacao> findByIdWithConvidados(@Param("id") Long id);
}
