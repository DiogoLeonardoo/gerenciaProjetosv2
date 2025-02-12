package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Notificacao;
import com.mycompany.myapp.repository.NotificacaoRepository;
import com.mycompany.myapp.service.NotificacaoService;
import com.mycompany.myapp.service.dto.NotificacaoDTO;
import com.mycompany.myapp.service.mapper.NotificacaoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Notificacao}.
 */
@Service
@Transactional
public class NotificacaoServiceImpl implements NotificacaoService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificacaoServiceImpl.class);

    private final NotificacaoRepository notificacaoRepository;

    private final NotificacaoMapper notificacaoMapper;

    public NotificacaoServiceImpl(NotificacaoRepository notificacaoRepository, NotificacaoMapper notificacaoMapper) {
        this.notificacaoRepository = notificacaoRepository;
        this.notificacaoMapper = notificacaoMapper;
    }

    @Override
    public NotificacaoDTO save(NotificacaoDTO notificacaoDTO) {
        LOG.debug("Request to save Notificacao : {}", notificacaoDTO);
        Notificacao notificacao = notificacaoMapper.toEntity(notificacaoDTO);
        notificacao = notificacaoRepository.save(notificacao);
        return notificacaoMapper.toDto(notificacao);
    }

    @Override
    public NotificacaoDTO update(NotificacaoDTO notificacaoDTO) {
        LOG.debug("Request to update Notificacao : {}", notificacaoDTO);
        Notificacao notificacao = notificacaoMapper.toEntity(notificacaoDTO);
        notificacao = notificacaoRepository.save(notificacao);
        return notificacaoMapper.toDto(notificacao);
    }

    @Override
    public Optional<NotificacaoDTO> partialUpdate(NotificacaoDTO notificacaoDTO) {
        LOG.debug("Request to partially update Notificacao : {}", notificacaoDTO);

        return notificacaoRepository
            .findById(notificacaoDTO.getId())
            .map(existingNotificacao -> {
                notificacaoMapper.partialUpdate(existingNotificacao, notificacaoDTO);

                return existingNotificacao;
            })
            .map(notificacaoRepository::save)
            .map(notificacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificacaoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Notificacaos");
        return notificacaoRepository.findAll(pageable).map(notificacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificacaoDTO> findOne(Long id) {
        LOG.debug("Request to get Notificacao : {}", id);
        return notificacaoRepository.findById(id).map(notificacaoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Notificacao : {}", id);
        notificacaoRepository.deleteById(id);
    }
}
