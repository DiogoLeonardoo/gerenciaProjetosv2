package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Compromisso;
import com.mycompany.myapp.domain.Etapa;
import com.mycompany.myapp.service.dto.CompromissoDTO;
import com.mycompany.myapp.service.dto.EtapaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Etapa} and its DTO {@link EtapaDTO}.
 */
@Mapper(componentModel = "spring")
public interface EtapaMapper extends EntityMapper<EtapaDTO, Etapa> {
    @Mapping(target = "compromisso", source = "compromisso", qualifiedByName = "compromissoId")
    EtapaDTO toDto(Etapa s);

    @Named("compromissoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompromissoDTO toDtoCompromissoId(Compromisso compromisso);
}
