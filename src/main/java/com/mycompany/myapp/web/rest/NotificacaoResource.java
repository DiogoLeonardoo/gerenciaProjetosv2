package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Notificacao; // Importação da classe Notificacao
import com.mycompany.myapp.repository.NotificacaoRepository;
import com.mycompany.myapp.service.NotificacaoService;
import com.mycompany.myapp.service.dto.NotificacaoDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Notificacao}.
 */
@RestController
@RequestMapping("/api/notificacaos")
public class NotificacaoResource {

    private static final Logger LOG = LoggerFactory.getLogger(NotificacaoResource.class);

    private static final String ENTITY_NAME = "notificacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificacaoService notificacaoService;

    private final NotificacaoRepository notificacaoRepository;

    public NotificacaoResource(NotificacaoService notificacaoService, NotificacaoRepository notificacaoRepository) {
        this.notificacaoService = notificacaoService;
        this.notificacaoRepository = notificacaoRepository;
    }

    /**
     * {@code POST  /notificacaos} : Create a new notificacao.
     *
     * @param notificacaoDTO the notificacaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificacaoDTO, or with status {@code 400 (Bad Request)} if the notificacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Notificacao> createNotificacao(@RequestBody Notificacao notificacao) {
        // Validação da notificação recebida (verificando se o título, prazo, etc., não são nulos)
        if (notificacao.getTitulo() == null || notificacao.getPrazo() == null) {
            return ResponseEntity.badRequest().build(); // Retorna erro 400 se algum campo obrigatório estiver faltando
        }

        // Persistindo a notificação no banco
        Notificacao savedNotificacao = notificacaoRepository.save(notificacao);

        // Retornando a notificação criada com status 201 Created
        return ResponseEntity.created(URI.create("/api/notificacoes/" + savedNotificacao.getId())).body(savedNotificacao);
    }

    /**
     * {@code PUT  /notificacaos/:id} : Updates an existing notificacao.
     *
     * @param id the id of the notificacaoDTO to save.
     * @param notificacaoDTO the notificacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificacaoDTO,
     * or with status {@code 400 (Bad Request)} if the notificacaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Notificacao> updateNotificacao(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Notificacao notificacao
    ) throws URISyntaxException {
        LOG.debug("REST request to update Notificacao : {}, {}", id, notificacao);

        // Validação da notificação recebida (verificando se o título, prazo, etc., não são nulos)
        if (notificacao.getTitulo() == null || notificacao.getPrazo() == null) {
            return ResponseEntity.badRequest().build(); // Retorna erro 400 se algum campo obrigatório estiver faltando
        }

        // Verificando se a notificação com o ID fornecido existe
        if (!notificacaoRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // Retorna 404 se a notificação não for encontrada
        }

        // Atribuindo o ID da notificação recebida ao ID da notificação a ser atualizada
        notificacao.setId(id);

        // Persistindo a notificação atualizada no banco
        Notificacao updatedNotificacao = notificacaoRepository.save(notificacao);

        // Retornando a notificação atualizada com status 200 OK
        return ResponseEntity.ok().body(updatedNotificacao);
    }

    /**
     * {@code PATCH  /notificacaos/:id} : Partial updates given fields of an existing notificacao, field will ignore if it is null
     *
     * @param id the id of the notificacaoDTO to save.
     * @param notificacaoDTO the notificacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificacaoDTO,
     * or with status {@code 400 (Bad Request)} if the notificacaoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the notificacaoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the notificacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NotificacaoDTO> partialUpdateNotificacao(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NotificacaoDTO notificacaoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Notificacao partially : {}, {}", id, notificacaoDTO);
        if (notificacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NotificacaoDTO> result = notificacaoService.partialUpdate(notificacaoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, notificacaoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /notificacaos} : get all the notificacaos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificacaos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NotificacaoDTO>> getAllNotificacaos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Notificacaos");
        Page<NotificacaoDTO> page = notificacaoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /notificacaos/:id} : get the "id" notificacao.
     *
     * @param id the id of the notificacaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificacaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Notificacao> getNotificacaoComConvidados(@PathVariable Long id) {
        Optional<Notificacao> notificacao = notificacaoRepository.findByIdWithConvidados(id);
        return notificacao.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * {@code DELETE  /notificacaos/:id} : delete the "id" notificacao.
     *
     * @param id the id of the notificacaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotificacao(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Notificacao : {}", id);
        notificacaoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
