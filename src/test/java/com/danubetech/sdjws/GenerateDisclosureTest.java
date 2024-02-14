package com.danubetech.sdjws;

import com.danubetech.sdjws.disclosure.Disclosure;
import com.danubetech.sdjws.salt.DefaultSaltGenerator;
import jakarta.json.Json;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenerateDisclosureTest {

	private static final Random RANDOM1 = new Random(1);
	private static final Random RANDOM2 = new Random(2);

	private static final String JSON = """
            {
              "id": "urn:uuid:9bcc9aaa-3bdc-4414-9450-739c295c752c",
              "credentialSubject": {
                "familyName": "Carroll",
                "givenName": "Lewis"
              }
            }
            """;

	@Test
	public void testGenerateDisclosure1() throws Exception {
		SDJWSObject sdjwsObject = SDJWSObject.fromJson(JSON);
		((DefaultSaltGenerator) sdjwsObject.getDisclosureGenerator().getSaltGenerator()).setRandom(RANDOM1);

		Disclosure disclosure1 = sdjwsObject.generateDisclosure(Json.createPointer("/credentialSubject"));
		assertEquals(disclosure1.getSalt(), "c9Uau9icuBlvDvtokvlNaPzMLDXwuEYJ5fEsVd2Fq6jV2b73aAjztXLlkAESuBknulu19n4b2ii0BJvw5K7XjbFde_L8DDTpqZ3k7zvCsXyBN61lmHj56T3x9lg2esooZFJHS57zdl4k6aiBc3JN3fsEsB3M6wyK6tZBxY2tVpU");
		assertEquals(disclosure1.getJsonPointer().toString(), "/credentialSubject");
		assertEquals(disclosure1.getJsonValue().toString(), "{\"familyName\":\"Carroll\",\"givenName\":\"Lewis\"}");
		assertEquals(disclosure1.getDisclosureJson().toString(), "[\"c9Uau9icuBlvDvtokvlNaPzMLDXwuEYJ5fEsVd2Fq6jV2b73aAjztXLlkAESuBknulu19n4b2ii0BJvw5K7XjbFde_L8DDTpqZ3k7zvCsXyBN61lmHj56T3x9lg2esooZFJHS57zdl4k6aiBc3JN3fsEsB3M6wyK6tZBxY2tVpU\",\"/credentialSubject\",{\"familyName\":\"Carroll\",\"givenName\":\"Lewis\"}]");
		assertEquals(disclosure1.getDisclosureString(), "WyJjOVVhdTlpY3VCbHZEdnRva3ZsTmFQek1MRFh3dUVZSjVmRXNWZDJGcTZqVjJiNzNhQWp6dFhMbGtBRVN1QmtudWx1MTluNGIyaWkwQkp2dzVLN1hqYkZkZV9MOEREVHBxWjNrN3p2Q3NYeUJONjFsbUhqNTZUM3g5bGcyZXNvb1pGSkhTNTd6ZGw0azZhaUJjM0pOM2ZzRXNCM002d3lLNnRaQnhZMnRWcFUiLCIvY3JlZGVudGlhbFN1YmplY3QiLHsiZmFtaWx5TmFtZSI6IkNhcnJvbGwiLCJnaXZlbk5hbWUiOiJMZXdpcyJ9XQ");
		assertEquals(disclosure1.calculateDisclosureDigest(), "K_gXSSjPJbk7jJ--NTqkGz5cmZRA_Fn2gMnW0RVuMTo");
	}

	@Test
	public void testGenerateDisclosure2() throws Exception {
		SDJWSObject sdjwsObject = SDJWSObject.fromJson(JSON);
		((DefaultSaltGenerator) sdjwsObject.getDisclosureGenerator().getSaltGenerator()).setRandom(RANDOM2);

		Disclosure disclosure2 = sdjwsObject.generateDisclosure(Json.createPointer("/credentialSubject/familyName"));
		System.out.println(disclosure2);
		assertEquals(disclosure2.getSalt(), "OXIsu_i5GkuQRcXmF18QAerDL3_NXszaXG5i_E5jhQgPe2zbeeVR7Jycx_y9YO5z9I5oOuyEG4UksCUTQNAe17eVP74e-VE_heMX817uwo0st88HH8ZiHI5TELF1BgbJAWoI2m0YpWAV-4YON711RiJfhWiF1vvy9yY2WlQOq4U");
		assertEquals(disclosure2.getJsonPointer().toString(), "/credentialSubject/familyName");
		assertEquals(disclosure2.getJsonValue().toString(), "\"Carroll\"");
		assertEquals(disclosure2.getDisclosureJson().toString(), "[\"OXIsu_i5GkuQRcXmF18QAerDL3_NXszaXG5i_E5jhQgPe2zbeeVR7Jycx_y9YO5z9I5oOuyEG4UksCUTQNAe17eVP74e-VE_heMX817uwo0st88HH8ZiHI5TELF1BgbJAWoI2m0YpWAV-4YON711RiJfhWiF1vvy9yY2WlQOq4U\",\"/credentialSubject/familyName\",\"Carroll\"]");
		assertEquals(disclosure2.getDisclosureString(), "WyJPWElzdV9pNUdrdVFSY1htRjE4UUFlckRMM19OWHN6YVhHNWlfRTVqaFFnUGUyemJlZVZSN0p5Y3hfeTlZTzV6OUk1b091eUVHNFVrc0NVVFFOQWUxN2VWUDc0ZS1WRV9oZU1YODE3dXdvMHN0ODhISDhaaUhJNVRFTEYxQmdiSkFXb0kybTBZcFdBVi00WU9ONzExUmlKZmhXaUYxdnZ5OXlZMldsUU9xNFUiLCIvY3JlZGVudGlhbFN1YmplY3QvZmFtaWx5TmFtZSIsIkNhcnJvbGwiXQ");
		assertEquals(disclosure2.calculateDisclosureDigest(), "o7CydxeKIJp_37diqSifclzb1utexIs9cn7Vtu5f4dU");
	}
}
