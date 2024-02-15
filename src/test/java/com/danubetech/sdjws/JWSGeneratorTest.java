package com.danubetech.sdjws;

import com.danubetech.sdjws.disclosure.Disclosure;
import com.danubetech.sdjws.disclosure.DisclosureGenerator;
import com.danubetech.sdjws.jws.JWSGenerator;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

public class JWSGeneratorTest {

	public static final String SDJWS = """
			{
			  "@context": ["https://www.w3.org/ns/credentials/v2"],
			  "id": "urn:uuid:9bcc9aaa-3bdc-4414-9450-739c295c752c",
			  "issuer": "did:ebsi:zvHWX359A3CvfJnCYaAiAde",
			  "validFrom": "2023-01-01T00:00:00Z",
			  "validUntil": "2033-01-01T00:00:00Z",
			  "credentialSubject": {
			    "id": "did:key:z2dmzD81cgPx8Vki7JbuuMmFYrWPgYoytykUZ3eyqht1j9KbsDbVZXdb3jzCagESyY4EE2x7Yjx3gNwctoEuRCKKDrdNP3HPFtG8RTvBiYStT5ghBHhHizH2Dy6xQtW3Pd2SecizL9b2jzDCMr7Ka5cRAWZFwvqwAtwTT7xet769y9ERh6",
			    "birthDate": "1832-01-27",
			    "student": true
			  },
			  "credentialSchema": {
			    "id": "https://api-pilot.ebsi.eu/trusted-schemas-registry/v2/schemas/0x23039e6356ea6b703ce672e7cfac0b42765b150f63df78e2bd18ae785787f6a2",
			    "type": "JsonSchema"
			  },
			  "_sd": [
			    "PWpJuNXN0WZdcuhVtoizB2tHRYNr0AwyeXik2nwPFAc",
			    "cCPq3HfqiTNptfkMsMgUROh_YCdldFs8vRm1IK3Mgdc",
			    "PQBLWVCO7Gcs_zLAdmdL5BSzu3y2DHjb97oOo9FXsZs"
			  ],
			  "_sd_alg": "sha-256",
			  "_sd_typ": "application/vc+ld+json"
			}
			""";

	public static final List<Disclosure> DISCLOSURES = List.of(
			DisclosureGenerator.getInstance().parse("WyIyR0xDNDJzS1F2ZUNmR2ZyeU5STjl3IiwgIi9jcmVkZW50aWFsU3ViamVjdC9mYW1pbHlOYW1lIiwgIkNhcnJvbGwiXQ"),
			DisclosureGenerator.getInstance().parse("WyJlbHVWNU9nM2dTTklJOEVZbnN4QV9BIiwgIi9jcmVkZW50aWFsU3ViamVjdC9naXZlbk5hbWUiLCAiTGV3aXMiXQ"),
			DisclosureGenerator.getInstance().parse("WyI2SWo3dE0tYTVpVlBHYm9TNXRtdlZBIiwgIi90eXBlIiwgWyJWZXJpZmlhYmxlQ3JlZGVudGlhbCIsICJTdHVkZW50SUQiXV0")
	);

	@Test
	public void testJWSGenerator() throws Exception {
		SDJWSObject sdjwsObject = SDJWSObject.fromJson(SDJWS);
		JWSObject jwsObject = JWSGenerator.getInstance().generateJWSObject(sdjwsObject, DISCLOSURES, JWSAlgorithm.ES256);

		ECKey ecKey = new ECKeyGenerator(Curve.P_256).generate();
		JWSSigner jwsSigner = new ECDSASigner(ecKey);
		jwsObject.sign(jwsSigner);
	}

	@Test
	public void testJWSGeneratorJSON() throws Exception {
		SDJWSObject sdjwsObject = SDJWSObject.fromJson(SDJWS);
		JWSObjectJSON jwsObjectJson = JWSGenerator.getInstance().generateJWSObjectJSON(sdjwsObject, DISCLOSURES);

		JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.ES256)
				.type(JOSEObjectType.JOSE)
				.base64URLEncodePayload(false)
				.criticalParams(Set.of("b64"))
				.build();

		ECKey ecKey = new ECKeyGenerator(Curve.P_256).generate();
		JWSSigner jwsSigner = new ECDSASigner(ecKey);
		jwsObjectJson.sign(jwsHeader, jwsSigner);
		System.out.println(jwsObjectJson.serializeFlattened());
	}
}
