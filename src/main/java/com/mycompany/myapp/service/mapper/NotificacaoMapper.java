package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Compromisso;
import com.mycompany.myapp.domain.Notificacao;
import com.mycompany.myapp.domain.Notificacao;
import com.mycompany.myapp.service.dto.CompromissoDTO;
import com.mycompany.myapp.service.dto.NotificacaoDTO;
import com.mycompany.myapp.service.dto.NotificacaoDTO;
import org.mapstruct.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Notificacao} and its DTO {@link NotificacaoDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificacaoMapper extends EntityMapper<NotificacaoDTO, Notificacao> {
    @Mapping(target = "titulo", source = "titulo")
    @Mapping(target = "prazo", source = "prazo") // Certifique-se de que 'prazo' é o mapeamento correto
    @Mapping(target = "convidados", source = "convidados") // Se você precisa mapear os convidados
    @Mapping(target = "compromisso", source = "compromisso") // Mapeando o ID do compromisso
    NotificacaoDTO toDto(Notificacao entity);

    @Mapping(target = "titulo", source = "titulo")
    @Mapping(target = "prazo", source = "prazo") // Idem para a conversão inversa
    @Mapping(target = "convidados", source = "convidados")
    @Mapping(target = "compromisso", source = "compromisso") // Mapeando o compromisso do DTO para a entidade
    Notificacao toEntity(NotificacaoDTO dto);

    default Compromisso map(Long value) {
        if (value == null) {
            return null;
        }
        Compromisso compromisso = new Compromisso();
        compromisso.setId(value);
        return compromisso;
    }

    // Mapeamento de Compromisso -> Long
    default Long map(Compromisso compromisso) {
        if (compromisso == null) {
            return null;
        }
        return compromisso.getId();
    }
}
