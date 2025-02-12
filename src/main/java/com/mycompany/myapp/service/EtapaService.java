package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.EtapaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Etapa}.
 */
public interface EtapaService {
    /**
     * Save a etapa.
     *
     * @param etapaDTO the entity to save.
     * @return the persisted entity.
     */
    EtapaDTO save(EtapaDTO etapaDTO);

    /**
     * Updates a etapa.
     *
     * @param etapaDTO the entity to update.
     * @return the persisted entity.
     */
    EtapaDTO update(EtapaDTO etapaDTO);

    /**
     * Partially updates a etapa.
     *
     * @param etapaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EtapaDTO> partialUpdate(EtapaDTO etapaDTO);

    /**
     * Get all the etapas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EtapaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" etapa.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EtapaDTO> findOne(Long id);

    /**
     * Delete the "id" etapa.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
