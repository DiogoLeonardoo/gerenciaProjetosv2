package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CompromissoTestSamples.*;
import static com.mycompany.myapp.domain.NotificacaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notificacao.class);
        Notificacao notificacao1 = getNotificacaoSample1();
        Notificacao notificacao2 = new Notificacao();
        assertThat(notificacao1).isNotEqualTo(notificacao2);

        notificacao2.setId(notificacao1.getId());
        assertThat(notificacao1).isEqualTo(notificacao2);

        notificacao2 = getNotificacaoSample2();
        assertThat(notificacao1).isNotEqualTo(notificacao2);
    }

    @Test
    void compromissoTest() {
        Notificacao notificacao = getNotificacaoRandomSampleGenerator();
        Compromisso compromissoBack = getCompromissoRandomSampleGenerator();

        notificacao.setCompromisso(compromissoBack);
        assertThat(notificacao.getCompromisso()).isEqualTo(compromissoBack);

        notificacao.compromisso(null);
        assertThat(notificacao.getCompromisso()).isNull();
    }
}
