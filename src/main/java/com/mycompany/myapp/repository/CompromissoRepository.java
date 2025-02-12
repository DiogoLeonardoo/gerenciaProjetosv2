package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Compromisso;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Compromisso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompromissoRepository extends JpaRepository<Compromisso, Long> {
    @Query("select compromisso from Compromisso compromisso where compromisso.usuario.login = ?#{authentication.name}")
    List<Compromisso> findByUsuarioIsCurrentUser();

    @Query("SELECT c FROM Compromisso c WHERE c.status = 'CONCLUIDO' AND c.dataHorario BETWEEN :inicio AND :fim")
    List<Compromisso> findCompromissosConcluidosPorPeriodo(@Param("inicio") Instant inicio, @Param("fim") Instant fim);
}
