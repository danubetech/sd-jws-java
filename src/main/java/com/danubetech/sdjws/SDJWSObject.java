package com.danubetech.sdjws;

import com.danubetech.sdjws.digest.DefaultDigestGenerator;
import com.danubetech.sdjws.digest.DigestGenerator;
import com.danubetech.sdjws.disclosure.Disclosure;
import com.danubetech.sdjws.disclosure.DisclosureGenerator;
import jakarta.json.*;
import jakarta.json.stream.JsonGenerator;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class SDJWSObject {

    private JsonObject jsonObject;

    private DisclosureGenerator disclosureGenerator = DisclosureGenerator.getInstance();

    private SDJWSObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    /*
     * (De-)serialization
     */

    public static SDJWSObject fromJsonObject(JsonObject jsonObject) {
        return new SDJWSObject(jsonObject);
    }

    public static SDJWSObject fromJson(String json) {
        JsonObject jsonObject = Json.createReader(new StringReader(json)).readObject();
        return new SDJWSObject(jsonObject);
    }

    public static SDJWSObject fromMap(Map<String, Object> map) {
        JsonObject jsonObject = Json.createObjectBuilder(map).build();
        return new SDJWSObject(jsonObject);
    }

    public String toJson(boolean pretty) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = pretty ? Json.createWriterFactory(Map.of(JsonGenerator.PRETTY_PRINTING, "")).createWriter(stringWriter) : Json.createWriter(stringWriter);
        jsonWriter.write(this.getJsonObject());
        jsonWriter.close();
        return stringWriter.toString();
    }

    public String toJson() {
        return this.toJson(false);
    }

    /*
     * Manipulation methods
     */

    public Disclosure generateDisclosure(final JsonPointer jsonPointer, final DigestGenerator digestGenerator) {

        // find the JSON value to extract as a disclosure

        JsonValue jsonValue = jsonPointer.getValue(this.getJsonObject());
        if (jsonValue == null) throw new IllegalArgumentException("Cannot find " + jsonPointer + " in SD-JWS object");

        // create the disclosure from the JSON value

        Disclosure disclosure = this.getDisclosureGenerator().create(jsonPointer, jsonValue);

        // remove the disclosure JSON value

        JsonObject jsonObject = jsonPointer.remove(this.getJsonObject());

        // begin manipulation of the SD-JWS object

        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder(jsonObject);

        // add the disclosure digest to the "_sd" array

        JsonArray previousSdJsonArray = this.getSdJsonArray();
        JsonArrayBuilder sdJsonArrayBuilder = previousSdJsonArray != null ? Json.createArrayBuilder(previousSdJsonArray) : Json.createArrayBuilder();
        sdJsonArrayBuilder.add(disclosure.calculateDisclosureDigest(digestGenerator));
        JsonArray sdJsonArray = sdJsonArrayBuilder.build();

        jsonObjectBuilder.add("_sd", sdJsonArray);

        // set the "_sd_alg" value

        String previousSdAlg = this.getSdAlg();
        if (previousSdAlg != null) {
            if (! previousSdAlg.equals(digestGenerator.getAlgorithm())) {
                throw new IllegalStateException("Digest algorithm in '_sd_alg' " + previousSdAlg + " does not match digest generator algorithm " + digestGenerator.getAlgorithm());
            }
        } else {
            jsonObjectBuilder.add("_sd_alg", digestGenerator.getAlgorithm());
        }

        // finish manipulation of the SD-JWS object

        jsonObject = jsonObjectBuilder.build();
        this.setJsonObject(jsonObject);

        // done

        return disclosure;
    }

    public Disclosure generateDisclosure(final JsonPointer jsonPointer) {
        return this.generateDisclosure(jsonPointer, DefaultDigestGenerator.getInstance());
    }

    public List<Disclosure> generateDisclosures(final List<JsonPointer> jsonPointers, final DigestGenerator digestGenerator) {
        return jsonPointers.stream().map(jsonPointer -> this.generateDisclosure(jsonPointer, digestGenerator)).toList();
    }

    public List<Disclosure> generateDisclosures(final List<JsonPointer> jsonPointers) {
        return jsonPointers.stream().map(jsonPointer -> this.generateDisclosure(jsonPointer, DefaultDigestGenerator.getInstance())).toList();
    }

    public void applyDisclosure(final Disclosure disclosure, final DigestGenerator digestGenerator) {

        // check if digest algorithm matches

        String algorithm = this.getSdAlg();
        if (! digestGenerator.getAlgorithm().equalsIgnoreCase(algorithm)) {
            throw new IllegalStateException("Digest algorithm in '_sd_alg' " + algorithm + " does not match digest generator algorithm " + digestGenerator.getAlgorithm());
        }

        // find the digest in the "_sd" array

        final JsonArray sdJsonArray = this.getSdJsonArray();
        if (sdJsonArray == null) throw new IllegalStateException("No '_sd' found in SD-JWS object");

        final String disclosureDigest = disclosure.calculateDisclosureDigest(digestGenerator);

        int sdJsonArrayIndex = IntStream.range(0, sdJsonArray.size())
                .filter(streamIndex -> disclosureDigest.equals(sdJsonArray.getString(streamIndex)))
                .findFirst()
                .orElse(-1);
        if (sdJsonArrayIndex < 0) throw new IllegalStateException("Disclosure digest " + disclosureDigest + " not found in '_sd' in SD-JWS object");

        JsonPointer sdJsonArrayJsonPointer = Json.createPointer("/_sd/" + sdJsonArrayIndex);

        // remove the disclosure digest from the "_sd" array

        JsonObject jsonObject = sdJsonArrayJsonPointer.remove(this.getJsonObject());

        // remove all "_sd*" entries if no disclosure digests are left

        if (jsonObject.getJsonArray("_sd").isEmpty()) {
            JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder(jsonObject);
            jsonObjectBuilder.remove("_sd");
            jsonObjectBuilder.remove("_sd_alg");
            jsonObjectBuilder.remove("_sd_typ");
            jsonObject = jsonObjectBuilder.build();
        }

        // add the JSON value

        jsonObject = disclosure.getJsonPointer().add(jsonObject, disclosure.getJsonValue());

        // done

        this.setJsonObject(jsonObject);
    }

    public void applyDisclosure(final Disclosure disclosure) {
        this.applyDisclosure(disclosure, DefaultDigestGenerator.getInstance());
    }

    public void applyDisclosures(final List<Disclosure> disclosures, final DigestGenerator digestGenerator) {
        for (Disclosure disclosure : disclosures) this.applyDisclosure(disclosure, digestGenerator);
    }

    public void applyDisclosures(final List<Disclosure> disclosures) {
        for (Disclosure disclosure : disclosures) this.applyDisclosure(disclosure, DefaultDigestGenerator.getInstance());
    }

    public JsonArray getSdJsonArray() {
        return this.getJsonObject().getJsonArray("_sd");
    }

    public String getSdAlg() {
        JsonString jsonString = this.getJsonObject().getJsonString("_sd_alg");
        return jsonString == null ? null : jsonString.getString();
    }

    public String getSdTyp() {
        JsonString jsonString = this.getJsonObject().getJsonString("_sd_typ");
        return jsonString == null ? null : jsonString.getString();
    }

    /*
     * Getters and setters
     */

    public JsonObject getJsonObject() {
        return this.jsonObject;
    }

    private void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public DisclosureGenerator getDisclosureGenerator() {
        return this.disclosureGenerator;
    }

    public void setDisclosureGenerator(DisclosureGenerator disclosureGenerator) {
        this.disclosureGenerator = disclosureGenerator;
    }

    /*
     * Object methods
     */

    @Override
    public String toString() {
        return this.toJson();
    }
}
