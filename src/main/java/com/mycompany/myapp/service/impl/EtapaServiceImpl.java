package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Etapa;
import com.mycompany.myapp.repository.EtapaRepository;
import com.mycompany.myapp.service.EtapaService;
import com.mycompany.myapp.service.dto.EtapaDTO;
import com.mycompany.myapp.service.mapper.EtapaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Etapa}.
 */
@Service
@Transactional
public class EtapaServiceImpl implements EtapaService {

    private static final Logger LOG = LoggerFactory.getLogger(EtapaServiceImpl.class);

    private final EtapaRepository etapaRepository;

    private final EtapaMapper etapaMapper;

    public EtapaServiceImpl(EtapaRepository etapaRepository, EtapaMapper etapaMapper) {
        this.etapaRepository = etapaRepository;
        this.etapaMapper = etapaMapper;
    }

    @Override
    public EtapaDTO save(EtapaDTO etapaDTO) {
        LOG.debug("Request to save Etapa : {}", etapaDTO);
        Etapa etapa = etapaMapper.toEntity(etapaDTO);
        etapa = etapaRepository.save(etapa);
        return etapaMapper.toDto(etapa);
    }

    @Override
    public EtapaDTO update(EtapaDTO etapaDTO) {
        LOG.debug("Request to update Etapa : {}", etapaDTO);
        Etapa etapa = etapaMapper.toEntity(etapaDTO);
        etapa = etapaRepository.save(etapa);
        return etapaMapper.toDto(etapa);
    }

    @Override
    public Optional<EtapaDTO> partialUpdate(EtapaDTO etapaDTO) {
        LOG.debug("Request to partially update Etapa : {}", etapaDTO);

        return etapaRepository
            .findById(etapaDTO.getId())
            .map(existingEtapa -> {
                etapaMapper.partialUpdate(existingEtapa, etapaDTO);

                return existingEtapa;
            })
            .map(etapaRepository::save)
            .map(etapaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EtapaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Etapas");
        return etapaRepository.findAll(pageable).map(etapaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EtapaDTO> findOne(Long id) {
        LOG.debug("Request to get Etapa : {}", id);
        return etapaRepository.findById(id).map(etapaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Etapa : {}", id);
        etapaRepository.deleteById(id);
    }
}
