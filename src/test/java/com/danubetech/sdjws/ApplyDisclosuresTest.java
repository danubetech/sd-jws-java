package com.danubetech.sdjws;

import com.danubetech.sdjws.disclosure.Disclosure;
import com.danubetech.sdjws.disclosure.DisclosureGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

public class ApplyDisclosuresTest {

	public static final String SDJWS1 = """
			{
			    "id": "urn:uuid:9bcc9aaa-3bdc-4414-9450-739c295c752c",
			    "_sd": [
			        "7F463wXr392eQyVSxLLO5BwrerDsbZuaknewKL2CVp4"
			    ],
			    "_sd_alg": "sha-256"
			}
			""";

	public static final Disclosure DISCLOSURE1 = DisclosureGenerator.getInstance().parse("WyJZTFFndXpoUjJkUjZ5NU05dm5BNW1fYkpMYU02OEIxUHQzRHBqQU1sOUIwLXV2aVliYWNTeUN2TlRWVkw4TFZBSThLYllrM3A3NXd2a3g3OFdBLWEtd2diRXVFSHNlZ0Y4clQxOFBIUURDMFBZbU5HY0pJY1VGaG5feUQycUROZW1LLUhKVGhWaHJRZjdfSUZ0T0JhQUFnajk0dGZqMXdDUTV6bzlucDRIWkEiLCIvY3JlZGVudGlhbFN1YmplY3QiLHsiZmFtaWx5TmFtZSI6IkNhcnJvbGwiLCJnaXZlbk5hbWUiOiJMZXdpcyJ9XQ");

	@Test
	public void testApplyDisclosure1() throws Exception {
		SDJWSObject sdjwsObject = SDJWSObject.fromJson(SDJWS1);

		sdjwsObject.applyDisclosure(DISCLOSURE1);

		assertNull(sdjwsObject.getSdJsonArray());
		assertNull(sdjwsObject.getSdAlg());
		assertNull(sdjwsObject.getSdTyp());
	}

	public static final String SDJWS2 = """
			{
			    "id": "urn:uuid:9bcc9aaa-3bdc-4414-9450-739c295c752c",
			    "credentialSubject": {
			        "givenName": "Lewis"
			    },
			    "_sd": [
			        "o7CydxeKIJp_37diqSifclzb1utexIs9cn7Vtu5f4dU"
			    ],
			    "_sd_alg": "sha-256"
			}
			""";

	public static final Disclosure DISCLOSURE2 = DisclosureGenerator.getInstance().parse("WyJPWElzdV9pNUdrdVFSY1htRjE4UUFlckRMM19OWHN6YVhHNWlfRTVqaFFnUGUyemJlZVZSN0p5Y3hfeTlZTzV6OUk1b091eUVHNFVrc0NVVFFOQWUxN2VWUDc0ZS1WRV9oZU1YODE3dXdvMHN0ODhISDhaaUhJNVRFTEYxQmdiSkFXb0kybTBZcFdBVi00WU9ONzExUmlKZmhXaUYxdnZ5OXlZMldsUU9xNFUiLCIvY3JlZGVudGlhbFN1YmplY3QvZmFtaWx5TmFtZSIsIkNhcnJvbGwiXQ");

	@Test
	public void testApplyDisclosure2() throws Exception {
		SDJWSObject sdjwsObject = SDJWSObject.fromJson(SDJWS2);

		sdjwsObject.applyDisclosure(DISCLOSURE2);

		assertNull(sdjwsObject.getSdJsonArray());
		assertNull(sdjwsObject.getSdAlg());
		assertNull(sdjwsObject.getSdTyp());
	}

	public static final String SDJWS3 = """
			{
			    "credentialSubject": {
			        "familyName": "Carroll"
			    },
			    "_sd": [
			        "hRNbbFCaUkRIEVcvbZIJYSetLEcGhBeWeRFWvsaqvUY",
			        "q9X3jolOsUZe0j4sfh4p7Zuk3cfuQseUCAlcV6g_1Nk"
			    ],
			    "_sd_alg": "sha-256"
			}
			""";

	public static final List<Disclosure> DISCLOSURES3 = List.of(
			DisclosureGenerator.getInstance().parse("WyJUSk1tdTVnRi1vLUZpQ3dTNnVja3p2REdMaEdFSl9XVWl1LWx4Q2pFUEpPbW1qSTZjMFl5MnVPMENxbUVLRkphaXlpQ3poMjNCUkdxSWFJSHpHOWNhV0RZcU04VFZaMGlQTXdHOHFmZlo5MmVqQmtuU0t3X3RuUGRCLW0yMTV5VHpReklfOUsybFFvYTlUeUs5azVvSXhWQlB2cnlNcXg2bldHUEtHZEQ1SW8iLCIvaWQiLCJ1cm46dXVpZDo5YmNjOWFhYS0zYmRjLTQ0MTQtOTQ1MC03MzljMjk1Yzc1MmMiXQ"),
			DisclosureGenerator.getInstance().parse("WyJGUk9SakZIVnBYWUFuX0lYOHY5a3pfTUhnbkdNMi1kbkVsR3hhWWRjLWhLejUxZEo4c0llN2VFc1V5dUZYQUs3ekFYS2ZJd25FUnh3aFhQeFg4X2NIZmotX3VOZER4TlNwMjhuaVh2WHBZeExBRm1TNmlWTVlJcXhEVUZkZTlkYmgtdldzUHlySnlPdDhCSDJCS1VRR2Ezc2NsV2pIMjFzNTlPUFFwUXpRbGMiLCIvY3JlZGVudGlhbFN1YmplY3QvZ2l2ZW5OYW1lIiwiTGV3aXMiXQ")
	);

	@Test
	public void testApplyDisclosure3() throws Exception {
		SDJWSObject sdjwsObject = SDJWSObject.fromJson(SDJWS3);

		sdjwsObject.applyDisclosures(DISCLOSURES3);

		assertNull(sdjwsObject.getSdJsonArray());
		assertNull(sdjwsObject.getSdAlg());
		assertNull(sdjwsObject.getSdTyp());
	}
}
