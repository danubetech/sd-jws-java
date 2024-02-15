package com.danubetech.sdjws.jws;

import com.danubetech.sdjws.SDJWSObject;
import com.danubetech.sdjws.disclosure.Disclosure;
import com.nimbusds.jose.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JWSGenerator {

    public static final JOSEObjectType JWS_JOSE_OBJECT_TYPE = new JOSEObjectType("JWS");

    private static final JWSGenerator instance = new JWSGenerator();

    public static JWSGenerator getInstance() {
        return instance;
    }

    public JWSGenerator() {
    }

    public JWSHeader generateJWSHeader(JWSAlgorithm jwsAlgorithm) {

        return new JWSHeader.Builder(jwsAlgorithm)
                .type(JWS_JOSE_OBJECT_TYPE)
                .base64URLEncodePayload(false)
                .criticalParams(Set.of("b64"))
                .build();
    }

    public JWSObject generateJWSObject(SDJWSObject sdjwsObject, List<Disclosure> disclosures, JWSAlgorithm jwsAlgorithm) {

        JWSHeader jwsHeader = this.generateJWSHeader(jwsAlgorithm);

        Payload payload = new Payload(Map.of(
                "sd", sdjwsObject.getJsonObject(),
                "disclosures", disclosures
        ));

        return new JWSObject(jwsHeader, payload);
    }

    public JWSObjectJSON generateJWSObjectJSON(SDJWSObject sdjwsObject, List<Disclosure> disclosures) throws ParseException {

        Payload payload = new Payload(Map.of(
                "sd", sdjwsObject.getJsonObject(),
                "disclosures", disclosures
        ));

        return new JWSObjectJSON(payload);
    }
}
