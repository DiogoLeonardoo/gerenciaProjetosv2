package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EtapaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Etapa getEtapaSample1() {
        return new Etapa().id(1L).descricao("descricao1").ordem(1);
    }

    public static Etapa getEtapaSample2() {
        return new Etapa().id(2L).descricao("descricao2").ordem(2);
    }

    public static Etapa getEtapaRandomSampleGenerator() {
        return new Etapa().id(longCount.incrementAndGet()).descricao(UUID.randomUUID().toString()).ordem(intCount.incrementAndGet());
    }
}
