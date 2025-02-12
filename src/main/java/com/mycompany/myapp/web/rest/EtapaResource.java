package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.EtapaRepository;
import com.mycompany.myapp.service.EtapaService;
import com.mycompany.myapp.service.dto.EtapaDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Etapa}.
 */
@RestController
@RequestMapping("/api/etapas")
public class EtapaResource {

    private static final Logger LOG = LoggerFactory.getLogger(EtapaResource.class);

    private static final String ENTITY_NAME = "etapa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EtapaService etapaService;

    private final EtapaRepository etapaRepository;

    public EtapaResource(EtapaService etapaService, EtapaRepository etapaRepository) {
        this.etapaService = etapaService;
        this.etapaRepository = etapaRepository;
    }

    /**
     * {@code POST  /etapas} : Create a new etapa.
     *
     * @param etapaDTO the etapaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new etapaDTO, or with status {@code 400 (Bad Request)} if the etapa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EtapaDTO> createEtapa(@Valid @RequestBody EtapaDTO etapaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Etapa : {}", etapaDTO);
        if (etapaDTO.getId() != null) {
            throw new BadRequestAlertException("A new etapa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        etapaDTO = etapaService.save(etapaDTO);
        return ResponseEntity.created(new URI("/api/etapas/" + etapaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, etapaDTO.getId().toString()))
            .body(etapaDTO);
    }

    /**
     * {@code PUT  /etapas/:id} : Updates an existing etapa.
     *
     * @param id the id of the etapaDTO to save.
     * @param etapaDTO the etapaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etapaDTO,
     * or with status {@code 400 (Bad Request)} if the etapaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the etapaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EtapaDTO> updateEtapa(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EtapaDTO etapaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Etapa : {}, {}", id, etapaDTO);
        if (etapaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etapaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!etapaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        etapaDTO = etapaService.update(etapaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, etapaDTO.getId().toString()))
            .body(etapaDTO);
    }

    /**
     * {@code PATCH  /etapas/:id} : Partial updates given fields of an existing etapa, field will ignore if it is null
     *
     * @param id the id of the etapaDTO to save.
     * @param etapaDTO the etapaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etapaDTO,
     * or with status {@code 400 (Bad Request)} if the etapaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the etapaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the etapaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EtapaDTO> partialUpdateEtapa(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EtapaDTO etapaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Etapa partially : {}, {}", id, etapaDTO);
        if (etapaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etapaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!etapaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EtapaDTO> result = etapaService.partialUpdate(etapaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, etapaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /etapas} : get all the etapas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of etapas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EtapaDTO>> getAllEtapas(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Etapas");
        Page<EtapaDTO> page = etapaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /etapas/:id} : get the "id" etapa.
     *
     * @param id the id of the etapaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the etapaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EtapaDTO> getEtapa(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Etapa : {}", id);
        Optional<EtapaDTO> etapaDTO = etapaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(etapaDTO);
    }

    /**
     * {@code DELETE  /etapas/:id} : delete the "id" etapa.
     *
     * @param id the id of the etapaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtapa(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Etapa : {}", id);
        etapaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
