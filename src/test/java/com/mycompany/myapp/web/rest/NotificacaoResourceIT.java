package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.NotificacaoAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Notificacao;
import com.mycompany.myapp.repository.NotificacaoRepository;
import com.mycompany.myapp.service.dto.NotificacaoDTO;
import com.mycompany.myapp.service.mapper.NotificacaoMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link NotificacaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificacaoResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final Instant DEFAULT_PRAZO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PRAZO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/notificacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private NotificacaoMapper notificacaoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificacaoMockMvc;

    private Notificacao notificacao;

    private Notificacao insertedNotificacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notificacao createEntity() {
        return new Notificacao().titulo(DEFAULT_TITULO).prazo(DEFAULT_PRAZO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notificacao createUpdatedEntity() {
        return new Notificacao().titulo(UPDATED_TITULO).prazo(UPDATED_PRAZO);
    }

    @BeforeEach
    public void initTest() {
        notificacao = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNotificacao != null) {
            notificacaoRepository.delete(insertedNotificacao);
            insertedNotificacao = null;
        }
    }

    @Test
    @Transactional
    void createNotificacao() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);
        var returnedNotificacaoDTO = om.readValue(
            restNotificacaoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificacaoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NotificacaoDTO.class
        );

        // Validate the Notificacao in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNotificacao = notificacaoMapper.toEntity(returnedNotificacaoDTO);
        assertNotificacaoUpdatableFieldsEquals(returnedNotificacao, getPersistedNotificacao(returnedNotificacao));

        insertedNotificacao = returnedNotificacao;
    }

    @Test
    @Transactional
    void createNotificacaoWithExistingId() throws Exception {
        // Create the Notificacao with an existing ID
        notificacao.setId(1L);
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificacaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Notificacao in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTituloIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificacao.setTitulo(null);

        // Create the Notificacao, which fails.
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        restNotificacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrazoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificacao.setPrazo(null);

        // Create the Notificacao, which fails.
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        restNotificacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNotificacaos() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList
        restNotificacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].prazo").value(hasItem(DEFAULT_PRAZO.toString())));
    }

    @Test
    @Transactional
    void getNotificacao() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get the notificacao
        restNotificacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, notificacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificacao.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.prazo").value(DEFAULT_PRAZO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingNotificacao() throws Exception {
        // Get the notificacao
        restNotificacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNotificacao() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificacao
        Notificacao updatedNotificacao = notificacaoRepository.findById(notificacao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNotificacao are not directly saved in db
        em.detach(updatedNotificacao);
        updatedNotificacao.titulo(UPDATED_TITULO).prazo(UPDATED_PRAZO);
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(updatedNotificacao);

        restNotificacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificacaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Notificacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNotificacaoToMatchAllProperties(updatedNotificacao);
    }

    @Test
    @Transactional
    void putNonExistingNotificacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificacao.setId(longCount.incrementAndGet());

        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotificacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificacao.setId(longCount.incrementAndGet());

        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotificacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificacao.setId(longCount.incrementAndGet());

        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notificacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificacaoWithPatch() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificacao using partial update
        Notificacao partialUpdatedNotificacao = new Notificacao();
        partialUpdatedNotificacao.setId(notificacao.getId());

        partialUpdatedNotificacao.titulo(UPDATED_TITULO);

        restNotificacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificacao))
            )
            .andExpect(status().isOk());

        // Validate the Notificacao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificacaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNotificacao, notificacao),
            getPersistedNotificacao(notificacao)
        );
    }

    @Test
    @Transactional
    void fullUpdateNotificacaoWithPatch() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificacao using partial update
        Notificacao partialUpdatedNotificacao = new Notificacao();
        partialUpdatedNotificacao.setId(notificacao.getId());

        partialUpdatedNotificacao.titulo(UPDATED_TITULO).prazo(UPDATED_PRAZO);

        restNotificacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificacao))
            )
            .andExpect(status().isOk());

        // Validate the Notificacao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificacaoUpdatableFieldsEquals(partialUpdatedNotificacao, getPersistedNotificacao(partialUpdatedNotificacao));
    }

    @Test
    @Transactional
    void patchNonExistingNotificacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificacao.setId(longCount.incrementAndGet());

        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificacaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotificacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificacao.setId(longCount.incrementAndGet());

        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotificacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificacao.setId(longCount.incrementAndGet());

        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacaoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(notificacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notificacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotificacao() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the notificacao
        restNotificacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, notificacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return notificacaoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Notificacao getPersistedNotificacao(Notificacao notificacao) {
        return notificacaoRepository.findById(notificacao.getId()).orElseThrow();
    }

    protected void assertPersistedNotificacaoToMatchAllProperties(Notificacao expectedNotificacao) {
        assertNotificacaoAllPropertiesEquals(expectedNotificacao, getPersistedNotificacao(expectedNotificacao));
    }

    protected void assertPersistedNotificacaoToMatchUpdatableProperties(Notificacao expectedNotificacao) {
        assertNotificacaoAllUpdatablePropertiesEquals(expectedNotificacao, getPersistedNotificacao(expectedNotificacao));
    }
}
