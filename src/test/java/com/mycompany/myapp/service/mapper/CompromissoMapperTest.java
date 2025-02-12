package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.CompromissoAsserts.*;
import static com.mycompany.myapp.domain.CompromissoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompromissoMapperTest {

    private CompromissoMapper compromissoMapper;

    @BeforeEach
    void setUp() {
        compromissoMapper = new CompromissoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCompromissoSample1();
        var actual = compromissoMapper.toEntity(compromissoMapper.toDto(expected));
        assertCompromissoAllPropertiesEquals(expected, actual);
    }
}
