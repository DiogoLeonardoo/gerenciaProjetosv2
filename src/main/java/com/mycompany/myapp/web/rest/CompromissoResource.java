package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Compromisso;
import com.mycompany.myapp.repository.CompromissoRepository;
import com.mycompany.myapp.service.CompromissoService;
import com.mycompany.myapp.service.dto.CompromissoDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Compromisso}.
 */
@RestController
@RequestMapping("/api/compromissos")
public class CompromissoResource {

    private static final Logger LOG = LoggerFactory.getLogger(CompromissoResource.class);

    private static final String ENTITY_NAME = "compromisso";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompromissoService compromissoService;

    private final CompromissoRepository compromissoRepository;

    public CompromissoResource(CompromissoService compromissoService, CompromissoRepository compromissoRepository) {
        this.compromissoService = compromissoService;
        this.compromissoRepository = compromissoRepository;
    }

    /**
     * {@code POST  /compromissos} : Create a new compromisso.
     *
     * @param compromissoDTO the compromissoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new compromissoDTO, or with status {@code 400 (Bad Request)}
     *         if the compromisso has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CompromissoDTO> createCompromisso(@Valid @RequestBody CompromissoDTO compromissoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Compromisso : {}", compromissoDTO);
        if (compromissoDTO.getId() != null) {
            throw new BadRequestAlertException("A new compromisso cannot already have an ID", ENTITY_NAME, "idexists");
        }
        compromissoDTO = compromissoService.save(compromissoDTO);
        return ResponseEntity.created(new URI("/api/compromissos/" + compromissoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, compromissoDTO.getId().toString()))
            .body(compromissoDTO);
    }

    /**
     * {@code PUT  /compromissos/:id} : Updates an existing compromisso.
     *
     * @param id             the id of the compromissoDTO to save.
     * @param compromissoDTO the compromissoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated compromissoDTO,
     *         or with status {@code 400 (Bad Request)} if the compromissoDTO is not
     *         valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         compromissoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompromissoDTO> updateCompromisso(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CompromissoDTO compromissoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Compromisso : {}, {}", id, compromissoDTO);
        if (compromissoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compromissoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compromissoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        compromissoDTO = compromissoService.update(compromissoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, compromissoDTO.getId().toString()))
            .body(compromissoDTO);
    }

    /**
     * {@code PATCH  /compromissos/:id} : Partial updates given fields of an
     * existing compromisso, field will ignore if it is null
     *
     * @param id             the id of the compromissoDTO to save.
     * @param compromissoDTO the compromissoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated compromissoDTO,
     *         or with status {@code 400 (Bad Request)} if the compromissoDTO is not
     *         valid,
     *         or with status {@code 404 (Not Found)} if the compromissoDTO is not
     *         found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         compromissoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompromissoDTO> partialUpdateCompromisso(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CompromissoDTO compromissoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Compromisso partially : {}, {}", id, compromissoDTO);
        if (compromissoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compromissoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compromissoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompromissoDTO> result = compromissoService.partialUpdate(compromissoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, compromissoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /compromissos} : get all the compromissos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of compromissos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CompromissoDTO>> getAllCompromissos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Compromissos");
        Page<CompromissoDTO> page = compromissoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /compromissos/:id} : get the "id" compromisso.
     *
     * @param id the id of the compromissoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the compromissoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompromissoDTO> getCompromisso(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Compromisso : {}", id);
        Optional<CompromissoDTO> compromissoDTO = compromissoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(compromissoDTO);
    }

    /**
     * {@code DELETE  /compromissos/:id} : delete the "id" compromisso.
     *
     * @param id the id of the compromissoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompromisso(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Compromisso : {}", id);
        compromissoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/concluidos")
    public ResponseEntity<List<Compromisso>> getCompromissosConcluidos(@RequestParam String inicio, @RequestParam String fim) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");

        LocalDate dataInicio = LocalDate.parse(inicio, formatter);
        Instant inicioInstant = dataInicio.atStartOfDay(zoneId).toInstant();

        LocalDate dataFim = LocalDate.parse(fim, formatter);
        Instant fimInstant = dataFim.atTime(LocalTime.MAX).atZone(zoneId).toInstant();

        List<Compromisso> compromissos = compromissoRepository.findCompromissosConcluidosPorPeriodo(inicioInstant, fimInstant);
        return ResponseEntity.ok(compromissos);
    }
}
