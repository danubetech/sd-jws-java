package com.danubetech.sdjws.disclosure;

import com.danubetech.sdjws.salt.DefaultSaltGenerator;
import com.danubetech.sdjws.salt.SaltGenerator;
import jakarta.json.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DisclosureGenerator {

    private static final DisclosureGenerator instance = new DisclosureGenerator();

    private Base64.Encoder encoder;
    private Base64.Decoder decoder;
    private SaltGenerator saltGenerator;

    public static DisclosureGenerator getInstance() {
        return instance;
    }

    public DisclosureGenerator(Base64.Encoder encoder, Base64.Decoder decoder, SaltGenerator saltGenerator) {
        this.encoder = encoder;
        this.decoder = decoder;
        this.saltGenerator = saltGenerator;
    }

    public DisclosureGenerator() {
        this(Base64.getUrlEncoder().withoutPadding(), Base64.getUrlDecoder(), DefaultSaltGenerator.getInstance());
    }

    public Disclosure create(final String salt, final JsonPointer jsonPointer, final JsonValue jsonValue) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        jsonArrayBuilder.add(salt);
        jsonArrayBuilder.add(jsonPointer.toString());
        jsonArrayBuilder.add(jsonValue);
        JsonArray disclosureJson = jsonArrayBuilder.build();

        StringWriter disclosureStringWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(disclosureStringWriter);
        jsonWriter.write(disclosureJson);
        jsonWriter.close();
        String disclosureString = this.getEncoder().encodeToString(disclosureStringWriter.toString().getBytes(StandardCharsets.UTF_8));

        return new Disclosure(salt, jsonPointer, jsonValue, disclosureJson, disclosureString);
    }

    public Disclosure create(final JsonPointer jsonPointer, final JsonValue jsonValue) {
        String salt = this.getSaltGenerator().generateSalt();
        return this.create(salt, jsonPointer, jsonValue);
    }

    public Disclosure parse(final String disclosureString) {
        JsonReader jsonReader = Json.createReader(new StringReader(new String(this.getDecoder().decode(disclosureString), StandardCharsets.UTF_8)));
        JsonArray disclosureJson = jsonReader.readArray();
        jsonReader.close();

        String salt = disclosureJson.getString(0);
        JsonPointer jsonPointer = Json.createPointer(disclosureJson.getString(1));
        JsonValue jsonValue = disclosureJson.get(2);

        return new Disclosure(salt, jsonPointer, jsonValue, disclosureJson, disclosureString);
    }

    /*
     * Getters and setters
     */

    public Base64.Encoder getEncoder() {
        return this.encoder;
    }

    public void setEncoder(Base64.Encoder encoder) {
        this.encoder = encoder;
    }

    public Base64.Decoder getDecoder() {
        return this.decoder;
    }

    public void setDecoder(Base64.Decoder decoder) {
        this.decoder = decoder;
    }

    public SaltGenerator getSaltGenerator() {
        return this.saltGenerator;
    }

    public void setSaltGenerator(SaltGenerator saltGenerator) {
        this.saltGenerator = saltGenerator;
    }
}
