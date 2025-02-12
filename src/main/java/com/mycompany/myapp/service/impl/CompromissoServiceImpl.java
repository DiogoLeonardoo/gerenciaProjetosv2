package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Compromisso;
import com.mycompany.myapp.repository.CompromissoRepository;
import com.mycompany.myapp.service.CompromissoService;
import com.mycompany.myapp.service.dto.CompromissoDTO;
import com.mycompany.myapp.service.mapper.CompromissoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Compromisso}.
 */
@Service
@Transactional
public class CompromissoServiceImpl implements CompromissoService {

    private static final Logger LOG = LoggerFactory.getLogger(CompromissoServiceImpl.class);

    private final CompromissoRepository compromissoRepository;

    private final CompromissoMapper compromissoMapper;

    public CompromissoServiceImpl(CompromissoRepository compromissoRepository, CompromissoMapper compromissoMapper) {
        this.compromissoRepository = compromissoRepository;
        this.compromissoMapper = compromissoMapper;
    }

    @Override
    public CompromissoDTO save(CompromissoDTO compromissoDTO) {
        LOG.debug("Request to save Compromisso : {}", compromissoDTO);
        Compromisso compromisso = compromissoMapper.toEntity(compromissoDTO);
        compromisso = compromissoRepository.save(compromisso);
        return compromissoMapper.toDto(compromisso);
    }

    @Override
    public CompromissoDTO update(CompromissoDTO compromissoDTO) {
        LOG.debug("Request to update Compromisso : {}", compromissoDTO);
        Compromisso compromisso = compromissoMapper.toEntity(compromissoDTO);
        compromisso = compromissoRepository.save(compromisso);
        return compromissoMapper.toDto(compromisso);
    }

    @Override
    public Optional<CompromissoDTO> partialUpdate(CompromissoDTO compromissoDTO) {
        LOG.debug("Request to partially update Compromisso : {}", compromissoDTO);

        return compromissoRepository
            .findById(compromissoDTO.getId())
            .map(existingCompromisso -> {
                compromissoMapper.partialUpdate(existingCompromisso, compromissoDTO);

                return existingCompromisso;
            })
            .map(compromissoRepository::save)
            .map(compromissoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompromissoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Compromissos");
        return compromissoRepository.findAll(pageable).map(compromissoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompromissoDTO> findOne(Long id) {
        LOG.debug("Request to get Compromisso : {}", id);
        return compromissoRepository.findById(id).map(compromissoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Compromisso : {}", id);
        compromissoRepository.deleteById(id);
    }
}
