package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.NotificacaoAsserts.*;
import static com.mycompany.myapp.domain.NotificacaoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificacaoMapperTest {

    private NotificacaoMapper notificacaoMapper;

    @BeforeEach
    void setUp() {
        notificacaoMapper = new NotificacaoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNotificacaoSample1();
        var actual = notificacaoMapper.toEntity(notificacaoMapper.toDto(expected));
        assertNotificacaoAllPropertiesEquals(expected, actual);
    }
}
