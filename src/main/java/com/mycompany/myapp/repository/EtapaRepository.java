package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Etapa;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Etapa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EtapaRepository extends JpaRepository<Etapa, Long> {}
