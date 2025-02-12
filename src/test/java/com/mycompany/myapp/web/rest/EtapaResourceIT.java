package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.EtapaAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Etapa;
import com.mycompany.myapp.domain.enumeration.StatusEtapa;
import com.mycompany.myapp.repository.EtapaRepository;
import com.mycompany.myapp.service.dto.EtapaDTO;
import com.mycompany.myapp.service.mapper.EtapaMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link EtapaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EtapaResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final StatusEtapa DEFAULT_STATUS = StatusEtapa.PENDENTE;
    private static final StatusEtapa UPDATED_STATUS = StatusEtapa.CONCLUIDA;

    private static final Integer DEFAULT_ORDEM = 1;
    private static final Integer UPDATED_ORDEM = 2;

    private static final String ENTITY_API_URL = "/api/etapas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EtapaRepository etapaRepository;

    @Autowired
    private EtapaMapper etapaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEtapaMockMvc;

    private Etapa etapa;

    private Etapa insertedEtapa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etapa createEntity() {
        return new Etapa().descricao(DEFAULT_DESCRICAO).status(DEFAULT_STATUS).ordem(DEFAULT_ORDEM);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etapa createUpdatedEntity() {
        return new Etapa().descricao(UPDATED_DESCRICAO).status(UPDATED_STATUS).ordem(UPDATED_ORDEM);
    }

    @BeforeEach
    public void initTest() {
        etapa = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEtapa != null) {
            etapaRepository.delete(insertedEtapa);
            insertedEtapa = null;
        }
    }

    @Test
    @Transactional
    void createEtapa() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Etapa
        EtapaDTO etapaDTO = etapaMapper.toDto(etapa);
        var returnedEtapaDTO = om.readValue(
            restEtapaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etapaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EtapaDTO.class
        );

        // Validate the Etapa in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEtapa = etapaMapper.toEntity(returnedEtapaDTO);
        assertEtapaUpdatableFieldsEquals(returnedEtapa, getPersistedEtapa(returnedEtapa));

        insertedEtapa = returnedEtapa;
    }

    @Test
    @Transactional
    void createEtapaWithExistingId() throws Exception {
        // Create the Etapa with an existing ID
        etapa.setId(1L);
        EtapaDTO etapaDTO = etapaMapper.toDto(etapa);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtapaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etapaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Etapa in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescricaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        etapa.setDescricao(null);

        // Create the Etapa, which fails.
        EtapaDTO etapaDTO = etapaMapper.toDto(etapa);

        restEtapaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etapaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        etapa.setStatus(null);

        // Create the Etapa, which fails.
        EtapaDTO etapaDTO = etapaMapper.toDto(etapa);

        restEtapaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etapaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrdemIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        etapa.setOrdem(null);

        // Create the Etapa, which fails.
        EtapaDTO etapaDTO = etapaMapper.toDto(etapa);

        restEtapaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etapaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEtapas() throws Exception {
        // Initialize the database
        insertedEtapa = etapaRepository.saveAndFlush(etapa);

        // Get all the etapaList
        restEtapaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etapa.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].ordem").value(hasItem(DEFAULT_ORDEM)));
    }

    @Test
    @Transactional
    void getEtapa() throws Exception {
        // Initialize the database
        insertedEtapa = etapaRepository.saveAndFlush(etapa);

        // Get the etapa
        restEtapaMockMvc
            .perform(get(ENTITY_API_URL_ID, etapa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(etapa.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.ordem").value(DEFAULT_ORDEM));
    }

    @Test
    @Transactional
    void getNonExistingEtapa() throws Exception {
        // Get the etapa
        restEtapaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEtapa() throws Exception {
        // Initialize the database
        insertedEtapa = etapaRepository.saveAndFlush(etapa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the etapa
        Etapa updatedEtapa = etapaRepository.findById(etapa.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEtapa are not directly saved in db
        em.detach(updatedEtapa);
        updatedEtapa.descricao(UPDATED_DESCRICAO).status(UPDATED_STATUS).ordem(UPDATED_ORDEM);
        EtapaDTO etapaDTO = etapaMapper.toDto(updatedEtapa);

        restEtapaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, etapaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etapaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Etapa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEtapaToMatchAllProperties(updatedEtapa);
    }

    @Test
    @Transactional
    void putNonExistingEtapa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etapa.setId(longCount.incrementAndGet());

        // Create the Etapa
        EtapaDTO etapaDTO = etapaMapper.toDto(etapa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtapaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, etapaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etapaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etapa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEtapa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etapa.setId(longCount.incrementAndGet());

        // Create the Etapa
        EtapaDTO etapaDTO = etapaMapper.toDto(etapa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtapaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(etapaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etapa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEtapa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etapa.setId(longCount.incrementAndGet());

        // Create the Etapa
        EtapaDTO etapaDTO = etapaMapper.toDto(etapa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtapaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etapaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etapa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEtapaWithPatch() throws Exception {
        // Initialize the database
        insertedEtapa = etapaRepository.saveAndFlush(etapa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the etapa using partial update
        Etapa partialUpdatedEtapa = new Etapa();
        partialUpdatedEtapa.setId(etapa.getId());

        partialUpdatedEtapa.ordem(UPDATED_ORDEM);

        restEtapaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtapa.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEtapa))
            )
            .andExpect(status().isOk());

        // Validate the Etapa in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEtapaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEtapa, etapa), getPersistedEtapa(etapa));
    }

    @Test
    @Transactional
    void fullUpdateEtapaWithPatch() throws Exception {
        // Initialize the database
        insertedEtapa = etapaRepository.saveAndFlush(etapa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the etapa using partial update
        Etapa partialUpdatedEtapa = new Etapa();
        partialUpdatedEtapa.setId(etapa.getId());

        partialUpdatedEtapa.descricao(UPDATED_DESCRICAO).status(UPDATED_STATUS).ordem(UPDATED_ORDEM);

        restEtapaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtapa.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEtapa))
            )
            .andExpect(status().isOk());

        // Validate the Etapa in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEtapaUpdatableFieldsEquals(partialUpdatedEtapa, getPersistedEtapa(partialUpdatedEtapa));
    }

    @Test
    @Transactional
    void patchNonExistingEtapa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etapa.setId(longCount.incrementAndGet());

        // Create the Etapa
        EtapaDTO etapaDTO = etapaMapper.toDto(etapa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtapaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, etapaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(etapaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etapa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEtapa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etapa.setId(longCount.incrementAndGet());

        // Create the Etapa
        EtapaDTO etapaDTO = etapaMapper.toDto(etapa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtapaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(etapaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etapa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEtapa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etapa.setId(longCount.incrementAndGet());

        // Create the Etapa
        EtapaDTO etapaDTO = etapaMapper.toDto(etapa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtapaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(etapaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etapa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEtapa() throws Exception {
        // Initialize the database
        insertedEtapa = etapaRepository.saveAndFlush(etapa);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the etapa
        restEtapaMockMvc
            .perform(delete(ENTITY_API_URL_ID, etapa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return etapaRepository.count();
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

    protected Etapa getPersistedEtapa(Etapa etapa) {
        return etapaRepository.findById(etapa.getId()).orElseThrow();
    }

    protected void assertPersistedEtapaToMatchAllProperties(Etapa expectedEtapa) {
        assertEtapaAllPropertiesEquals(expectedEtapa, getPersistedEtapa(expectedEtapa));
    }

    protected void assertPersistedEtapaToMatchUpdatableProperties(Etapa expectedEtapa) {
        assertEtapaAllUpdatablePropertiesEquals(expectedEtapa, getPersistedEtapa(expectedEtapa));
    }
}
