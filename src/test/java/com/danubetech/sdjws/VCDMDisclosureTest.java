package com.danubetech.sdjws;

import com.danubetech.sdjws.disclosure.Disclosure;
import com.danubetech.sdjws.disclosure.DisclosureGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

public class VCDMDisclosureTest {

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
	public void testVCDMDisclosure() throws Exception {
		SDJWSObject sdjwsObject = SDJWSObject.fromJson(SDJWS);

		for (Disclosure disclosure : DISCLOSURES) sdjwsObject.applyDisclosure(disclosure);
		System.out.println(sdjwsObject.toJson(true));

		assertNull(sdjwsObject.getSdJsonArray());
		assertNull(sdjwsObject.getSdAlg());
		assertNull(sdjwsObject.getSdTyp());
	}
}
