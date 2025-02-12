package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CompromissoTestSamples.*;
import static com.mycompany.myapp.domain.EtapaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CompromissoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Compromisso.class);
        Compromisso compromisso1 = getCompromissoSample1();
        Compromisso compromisso2 = new Compromisso();
        assertThat(compromisso1).isNotEqualTo(compromisso2);

        compromisso2.setId(compromisso1.getId());
        assertThat(compromisso1).isEqualTo(compromisso2);

        compromisso2 = getCompromissoSample2();
        assertThat(compromisso1).isNotEqualTo(compromisso2);
    }

    @Test
    void etapaTest() {
        Compromisso compromisso = getCompromissoRandomSampleGenerator();
        Etapa etapaBack = getEtapaRandomSampleGenerator();

        compromisso.addEtapa(etapaBack);
        assertThat(compromisso.getEtapas()).containsOnly(etapaBack);
        assertThat(etapaBack.getCompromisso()).isEqualTo(compromisso);

        compromisso.removeEtapa(etapaBack);
        assertThat(compromisso.getEtapas()).doesNotContain(etapaBack);
        assertThat(etapaBack.getCompromisso()).isNull();

        compromisso.etapas(new HashSet<>(Set.of(etapaBack)));
        assertThat(compromisso.getEtapas()).containsOnly(etapaBack);
        assertThat(etapaBack.getCompromisso()).isEqualTo(compromisso);

        compromisso.setEtapas(new HashSet<>());
        assertThat(compromisso.getEtapas()).doesNotContain(etapaBack);
        assertThat(etapaBack.getCompromisso()).isNull();
    }
}
