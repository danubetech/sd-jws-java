package com.danubetech.sdjws.digest;

public interface DigestGenerator {

    String generateDigest(String content);
    String getAlgorithm();
}