package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Compromisso;
import com.mycompany.myapp.domain.Notificacao;
import com.mycompany.myapp.service.dto.CompromissoDTO;
import com.mycompany.myapp.service.dto.NotificacaoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notificacao} and its DTO {@link NotificacaoDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificacaoMapper extends EntityMapper<NotificacaoDTO, Notificacao> {
    @Mapping(target = "compromisso", source = "compromisso", qualifiedByName = "compromissoId")
    NotificacaoDTO toDto(Notificacao s);

    @Named("compromissoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompromissoDTO toDtoCompromissoId(Compromisso compromisso);
}
