package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.CompromissoAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Compromisso;
import com.mycompany.myapp.domain.enumeration.CompromissoClassificacao;
import com.mycompany.myapp.domain.enumeration.StatusCompromisso;
import com.mycompany.myapp.repository.CompromissoRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.dto.CompromissoDTO;
import com.mycompany.myapp.service.mapper.CompromissoMapper;
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
 * Integration tests for the {@link CompromissoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompromissoResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATA_HORARIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_HORARIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final CompromissoClassificacao DEFAULT_CLASSIFICACAO = CompromissoClassificacao.TRABALHO;
    private static final CompromissoClassificacao UPDATED_CLASSIFICACAO = CompromissoClassificacao.ESTUDO;

    private static final StatusCompromisso DEFAULT_STATUS = StatusCompromisso.PENDENTE;
    private static final StatusCompromisso UPDATED_STATUS = StatusCompromisso.EM_ANDAMENTO;

    private static final String ENTITY_API_URL = "/api/compromissos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompromissoRepository compromissoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompromissoMapper compromissoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompromissoMockMvc;

    private Compromisso compromisso;

    private Compromisso insertedCompromisso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compromisso createEntity() {
        return new Compromisso()
            .titulo(DEFAULT_TITULO)
            .descricao(DEFAULT_DESCRICAO)
            .dataHorario(DEFAULT_DATA_HORARIO)
            .classificacao(DEFAULT_CLASSIFICACAO)
            .status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compromisso createUpdatedEntity() {
        return new Compromisso()
            .titulo(UPDATED_TITULO)
            .descricao(UPDATED_DESCRICAO)
            .dataHorario(UPDATED_DATA_HORARIO)
            .classificacao(UPDATED_CLASSIFICACAO)
            .status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        compromisso = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCompromisso != null) {
            compromissoRepository.delete(insertedCompromisso);
            insertedCompromisso = null;
        }
    }

    @Test
    @Transactional
    void createCompromisso() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Compromisso
        CompromissoDTO compromissoDTO = compromissoMapper.toDto(compromisso);
        var returnedCompromissoDTO = om.readValue(
            restCompromissoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compromissoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CompromissoDTO.class
        );

        // Validate the Compromisso in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCompromisso = compromissoMapper.toEntity(returnedCompromissoDTO);
        assertCompromissoUpdatableFieldsEquals(returnedCompromisso, getPersistedCompromisso(returnedCompromisso));

        insertedCompromisso = returnedCompromisso;
    }

    @Test
    @Transactional
    void createCompromissoWithExistingId() throws Exception {
        // Create the Compromisso with an existing ID
        compromisso.setId(1L);
        CompromissoDTO compromissoDTO = compromissoMapper.toDto(compromisso);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompromissoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compromissoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Compromisso in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTituloIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compromisso.setTitulo(null);

        // Create the Compromisso, which fails.
        CompromissoDTO compromissoDTO = compromissoMapper.toDto(compromisso);

        restCompromissoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compromissoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescricaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compromisso.setDescricao(null);

        // Create the Compromisso, which fails.
        CompromissoDTO compromissoDTO = compromissoMapper.toDto(compromisso);

        restCompromissoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compromissoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataHorarioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compromisso.setDataHorario(null);

        // Create the Compromisso, which fails.
        CompromissoDTO compromissoDTO = compromissoMapper.toDto(compromisso);

        restCompromissoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compromissoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClassificacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compromisso.setClassificacao(null);

        // Create the Compromisso, which fails.
        CompromissoDTO compromissoDTO = compromissoMapper.toDto(compromisso);

        restCompromissoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compromissoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compromisso.setStatus(null);

        // Create the Compromisso, which fails.
        CompromissoDTO compromissoDTO = compromissoMapper.toDto(compromisso);

        restCompromissoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compromissoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompromissos() throws Exception {
        // Initialize the database
        insertedCompromisso = compromissoRepository.saveAndFlush(compromisso);

        // Get all the compromissoList
        restCompromissoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compromisso.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].dataHorario").value(hasItem(DEFAULT_DATA_HORARIO.toString())))
            .andExpect(jsonPath("$.[*].classificacao").value(hasItem(DEFAULT_CLASSIFICACAO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getCompromisso() throws Exception {
        // Initialize the database
        insertedCompromisso = compromissoRepository.saveAndFlush(compromisso);

        // Get the compromisso
        restCompromissoMockMvc
            .perform(get(ENTITY_API_URL_ID, compromisso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compromisso.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.dataHorario").value(DEFAULT_DATA_HORARIO.toString()))
            .andExpect(jsonPath("$.classificacao").value(DEFAULT_CLASSIFICACAO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCompromisso() throws Exception {
        // Get the compromisso
        restCompromissoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompromisso() throws Exception {
        // Initialize the database
        insertedCompromisso = compromissoRepository.saveAndFlush(compromisso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compromisso
        Compromisso updatedCompromisso = compromissoRepository.findById(compromisso.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCompromisso are not directly saved in db
        em.detach(updatedCompromisso);
        updatedCompromisso
            .titulo(UPDATED_TITULO)
            .descricao(UPDATED_DESCRICAO)
            .dataHorario(UPDATED_DATA_HORARIO)
            .classificacao(UPDATED_CLASSIFICACAO)
            .status(UPDATED_STATUS);
        CompromissoDTO compromissoDTO = compromissoMapper.toDto(updatedCompromisso);

        restCompromissoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compromissoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(compromissoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Compromisso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompromissoToMatchAllProperties(updatedCompromisso);
    }

    @Test
    @Transactional
    void putNonExistingCompromisso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compromisso.setId(longCount.incrementAndGet());

        // Create the Compromisso
        CompromissoDTO compromissoDTO = compromissoMapper.toDto(compromisso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompromissoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compromissoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(compromissoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compromisso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompromisso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compromisso.setId(longCount.incrementAndGet());

        // Create the Compromisso
        CompromissoDTO compromissoDTO = compromissoMapper.toDto(compromisso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompromissoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(compromissoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compromisso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompromisso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compromisso.setId(longCount.incrementAndGet());

        // Create the Compromisso
        CompromissoDTO compromissoDTO = compromissoMapper.toDto(compromisso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompromissoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compromissoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compromisso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompromissoWithPatch() throws Exception {
        // Initialize the database
        insertedCompromisso = compromissoRepository.saveAndFlush(compromisso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compromisso using partial update
        Compromisso partialUpdatedCompromisso = new Compromisso();
        partialUpdatedCompromisso.setId(compromisso.getId());

        partialUpdatedCompromisso.titulo(UPDATED_TITULO).descricao(UPDATED_DESCRICAO).classificacao(UPDATED_CLASSIFICACAO);

        restCompromissoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompromisso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompromisso))
            )
            .andExpect(status().isOk());

        // Validate the Compromisso in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompromissoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCompromisso, compromisso),
            getPersistedCompromisso(compromisso)
        );
    }

    @Test
    @Transactional
    void fullUpdateCompromissoWithPatch() throws Exception {
        // Initialize the database
        insertedCompromisso = compromissoRepository.saveAndFlush(compromisso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compromisso using partial update
        Compromisso partialUpdatedCompromisso = new Compromisso();
        partialUpdatedCompromisso.setId(compromisso.getId());

        partialUpdatedCompromisso
            .titulo(UPDATED_TITULO)
            .descricao(UPDATED_DESCRICAO)
            .dataHorario(UPDATED_DATA_HORARIO)
            .classificacao(UPDATED_CLASSIFICACAO)
            .status(UPDATED_STATUS);

        restCompromissoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompromisso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompromisso))
            )
            .andExpect(status().isOk());

        // Validate the Compromisso in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompromissoUpdatableFieldsEquals(partialUpdatedCompromisso, getPersistedCompromisso(partialUpdatedCompromisso));
    }

    @Test
    @Transactional
    void patchNonExistingCompromisso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compromisso.setId(longCount.incrementAndGet());

        // Create the Compromisso
        CompromissoDTO compromissoDTO = compromissoMapper.toDto(compromisso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompromissoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compromissoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(compromissoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compromisso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompromisso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compromisso.setId(longCount.incrementAndGet());

        // Create the Compromisso
        CompromissoDTO compromissoDTO = compromissoMapper.toDto(compromisso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompromissoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(compromissoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compromisso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompromisso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compromisso.setId(longCount.incrementAndGet());

        // Create the Compromisso
        CompromissoDTO compromissoDTO = compromissoMapper.toDto(compromisso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompromissoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(compromissoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compromisso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompromisso() throws Exception {
        // Initialize the database
        insertedCompromisso = compromissoRepository.saveAndFlush(compromisso);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the compromisso
        restCompromissoMockMvc
            .perform(delete(ENTITY_API_URL_ID, compromisso.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return compromissoRepository.count();
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

    protected Compromisso getPersistedCompromisso(Compromisso compromisso) {
        return compromissoRepository.findById(compromisso.getId()).orElseThrow();
    }

    protected void assertPersistedCompromissoToMatchAllProperties(Compromisso expectedCompromisso) {
        assertCompromissoAllPropertiesEquals(expectedCompromisso, getPersistedCompromisso(expectedCompromisso));
    }

    protected void assertPersistedCompromissoToMatchUpdatableProperties(Compromisso expectedCompromisso) {
        assertCompromissoAllUpdatablePropertiesEquals(expectedCompromisso, getPersistedCompromisso(expectedCompromisso));
    }
}
