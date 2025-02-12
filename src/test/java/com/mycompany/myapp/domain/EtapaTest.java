package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CompromissoTestSamples.*;
import static com.mycompany.myapp.domain.EtapaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EtapaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Etapa.class);
        Etapa etapa1 = getEtapaSample1();
        Etapa etapa2 = new Etapa();
        assertThat(etapa1).isNotEqualTo(etapa2);

        etapa2.setId(etapa1.getId());
        assertThat(etapa1).isEqualTo(etapa2);

        etapa2 = getEtapaSample2();
        assertThat(etapa1).isNotEqualTo(etapa2);
    }

    @Test
    void compromissoTest() {
        Etapa etapa = getEtapaRandomSampleGenerator();
        Compromisso compromissoBack = getCompromissoRandomSampleGenerator();

        etapa.setCompromisso(compromissoBack);
        assertThat(etapa.getCompromisso()).isEqualTo(compromissoBack);

        etapa.compromisso(null);
        assertThat(etapa.getCompromisso()).isNull();
    }
}
