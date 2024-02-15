package com.danubetech.sdjws.disclosure;

import com.danubetech.sdjws.digest.DefaultDigestGenerator;
import com.danubetech.sdjws.digest.DigestGenerator;
import jakarta.json.JsonArray;
import jakarta.json.JsonPointer;
import jakarta.json.JsonValue;

public class Disclosure {

    private final String salt;
    private final JsonPointer jsonPointer;
    private final JsonValue jsonValue;

    private final JsonArray disclosureJson;
    private final String disclosureString;

    Disclosure(String salt, JsonPointer jsonPointer, JsonValue jsonValue, JsonArray disclosureJson, String disclosureString) {
        this.salt = salt;
        this.jsonPointer = jsonPointer;
        this.jsonValue = jsonValue;
        this.disclosureJson = disclosureJson;
        this.disclosureString = disclosureString;
    }

    public String calculateDisclosureDigest(DigestGenerator digestGenerator) {
        return digestGenerator.generateDigest(this.getDisclosureString());
    }

    public String calculateDisclosureDigest() {
        return this.calculateDisclosureDigest(DefaultDigestGenerator.getInstance());
    }

    /*
     * Getters
     */

    public String getSalt() {
        return this.salt;
    }

    public JsonPointer getJsonPointer() {
        return this.jsonPointer;
    }

    public JsonValue getJsonValue() {
        return this.jsonValue;
    }

    public JsonArray getDisclosureJson() {
        return this.disclosureJson;
    }

    public String getDisclosureString() {
        return this.disclosureString;
    }

    /*
     * Object methods
     */

    @Override
    public String toString() {
        return this.getDisclosureString();
    }
}
