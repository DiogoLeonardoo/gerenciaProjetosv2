package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CompromissoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Compromisso}.
 */
public interface CompromissoService {
    /**
     * Save a compromisso.
     *
     * @param compromissoDTO the entity to save.
     * @return the persisted entity.
     */
    CompromissoDTO save(CompromissoDTO compromissoDTO);

    /**
     * Updates a compromisso.
     *
     * @param compromissoDTO the entity to update.
     * @return the persisted entity.
     */
    CompromissoDTO update(CompromissoDTO compromissoDTO);

    /**
     * Partially updates a compromisso.
     *
     * @param compromissoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompromissoDTO> partialUpdate(CompromissoDTO compromissoDTO);

    /**
     * Get all the compromissos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompromissoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" compromisso.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompromissoDTO> findOne(Long id);

    /**
     * Delete the "id" compromisso.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
