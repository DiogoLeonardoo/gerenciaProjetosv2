package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.EtapaAsserts.*;
import static com.mycompany.myapp.domain.EtapaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EtapaMapperTest {

    private EtapaMapper etapaMapper;

    @BeforeEach
    void setUp() {
        etapaMapper = new EtapaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEtapaSample1();
        var actual = etapaMapper.toEntity(etapaMapper.toDto(expected));
        assertEtapaAllPropertiesEquals(expected, actual);
    }
}
