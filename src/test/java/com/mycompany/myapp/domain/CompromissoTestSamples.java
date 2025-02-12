package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CompromissoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Compromisso getCompromissoSample1() {
        return new Compromisso().id(1L).titulo("titulo1").descricao("descricao1");
    }

    public static Compromisso getCompromissoSample2() {
        return new Compromisso().id(2L).titulo("titulo2").descricao("descricao2");
    }

    public static Compromisso getCompromissoRandomSampleGenerator() {
        return new Compromisso()
            .id(longCount.incrementAndGet())
            .titulo(UUID.randomUUID().toString())
            .descricao(UUID.randomUUID().toString());
    }
}
