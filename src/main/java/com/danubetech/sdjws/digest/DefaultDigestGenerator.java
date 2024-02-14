package com.danubetech.sdjws.digest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class DefaultDigestGenerator implements DigestGenerator {

    private static final DefaultDigestGenerator instance = new DefaultDigestGenerator();

    private String algorithm;
    private Base64.Encoder encoder;

    public static DefaultDigestGenerator getInstance() {
        return instance;
    }

    public DefaultDigestGenerator(String algorithm, Base64.Encoder encoder) {
        this.algorithm = algorithm;
        this.encoder = encoder;
    }

    public DefaultDigestGenerator() {
        this("sha-256", Base64.getUrlEncoder().withoutPadding());
    }

    @Override
    public String generateDigest(String content) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(this.getAlgorithm());
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        md.update(content.getBytes(StandardCharsets.UTF_8));
        byte[] digestBytes = md.digest();
        return this.getEncoder().encodeToString(digestBytes);
    }

    /*
     * Getters and setters
     */

    @Override
    public String getAlgorithm() {
        return this.algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public Base64.Encoder getEncoder() {
        return this.encoder;
    }

    public void setEncoder(Base64.Encoder encoder) {
        this.encoder = encoder;
    }
}
