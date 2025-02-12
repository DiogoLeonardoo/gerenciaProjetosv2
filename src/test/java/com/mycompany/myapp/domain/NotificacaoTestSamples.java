package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NotificacaoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Notificacao getNotificacaoSample1() {
        return new Notificacao().id(1L).titulo("titulo1");
    }

    public static Notificacao getNotificacaoSample2() {
        return new Notificacao().id(2L).titulo("titulo2");
    }

    public static Notificacao getNotificacaoRandomSampleGenerator() {
        return new Notificacao().id(longCount.incrementAndGet()).titulo(UUID.randomUUID().toString());
    }
}
