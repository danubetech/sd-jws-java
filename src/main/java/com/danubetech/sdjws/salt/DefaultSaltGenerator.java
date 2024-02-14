package com.danubetech.sdjws.salt;

import java.util.Base64;
import java.util.Random;

public class DefaultSaltGenerator implements SaltGenerator {

    public static final int LENGTH = 128;

    private static final DefaultSaltGenerator instance = new DefaultSaltGenerator();

    private Random random;
    private Base64.Encoder encoder;

    public static DefaultSaltGenerator getInstance() {
        return instance;
    }

    public DefaultSaltGenerator(Random random, Base64.Encoder encoder) {
        this.random = random;
        this.encoder = encoder;
    }

    public DefaultSaltGenerator() {
        this(new Random(), Base64.getUrlEncoder().withoutPadding());
    }

    public String generateSalt() {
        byte[] bytes = new byte[LENGTH];
        this.getRandom().nextBytes(bytes);
        return this.getEncoder().encodeToString(bytes);
    }

    /*
     * Getters and setters
     */

    public Random getRandom() {
        return this.random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public Base64.Encoder getEncoder() {
        return this.encoder;
    }

    public void setEncoder(Base64.Encoder encoder) {
        this.encoder = encoder;
    }
}
