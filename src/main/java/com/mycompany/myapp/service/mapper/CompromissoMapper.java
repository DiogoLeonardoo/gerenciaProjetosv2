package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Compromisso;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.CompromissoDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Compromisso} and its DTO {@link CompromissoDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompromissoMapper extends EntityMapper<CompromissoDTO, Compromisso> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userId")
    CompromissoDTO toDto(Compromisso s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
