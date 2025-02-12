package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Notificacao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Notificacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {}
