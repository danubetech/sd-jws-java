package com.danubetech.sdjws.disclosure;

import jakarta.json.JsonArray;
import jakarta.json.JsonPointer;
import jakarta.json.JsonValue;

public class Disclosure {

    private final String salt;
    private final JsonPointer jsonPointer;
    private final JsonValue jsonValue;

    private final JsonArray disclosureJson;
    private final String disclosureString;
    private final String disclosureDigest;

    Disclosure(String salt, JsonPointer jsonPointer, JsonValue jsonValue, JsonArray disclosureJson, String disclosureString, String disclosureDigest) {
        this.salt = salt;
        this.jsonPointer = jsonPointer;
        this.jsonValue = jsonValue;
        this.disclosureJson = disclosureJson;
        this.disclosureString = disclosureString;
        this.disclosureDigest = disclosureDigest;
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

    public String getDisclosureDigest() {
        return this.disclosureDigest;
    }

    /*
     * Object methods
     */

    @Override
    public String toString() {
        return "Disclosure{" +
                "salt='" + salt + '\'' +
                ", jsonPointer=" + jsonPointer +
                ", jsonValue=" + jsonValue +
                ", disclosureJson=" + disclosureJson +
                ", disclosureString='" + disclosureString + '\'' +
                ", disclosureDigest='" + disclosureDigest + '\'' +
                '}';
    }
}
