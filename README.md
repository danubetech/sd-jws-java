# sd-jws-java

## Information

This is an implementation of EBSI's SD-JWS data model: https://code.europa.eu/ebsi/ecosystem/-/blob/EBIP-SD-JWT/drafts/sd-jws.md

## Maven

Build:

	mvn clean install

Dependency:

	<repositories>
		<repository>
			<id>danubetech-maven-public</id>
			<url>https://repo.danubetech.com/repository/maven-public/</url>
		</repository>
	</repositories>

	<dependency>
		<groupId>com.danubetech</groupId>
		<artifactId>sd-jws-java</artifactId>
		<version>0.1-SNAPSHOT</version>
	</dependency>

## Generating disclosures

Given this JSON data:

```json
{
  "id": "urn:uuid:9bcc9aaa-3bdc-4414-9450-739c295c752c",
  "credentialSubject": {
    "familyName": "Carroll",
    "givenName": "Lewis"
  }
}
```

Running this code:

```java
SDJWSObject sdjwsObject = SDJWSObject.fromJson(JSON);
Disclosure disclosure = sdjwsObject.generateDisclosure(Json.createPointer("/credentialSubject"));
```

Results in this disclosure:

```
WyJqLWd6b01iRDU5RFhZY2RMUnFsZERRQm13Q1pOQWN6SHNUWGFHZG4tWGNmZzFfMDhmNWpUcFhVd2V5bXllcGk5Zk1MeDRHQVpFQ3VBSjFvMFplM0VSUWpDN1dXb1VWdjhUYWZqVy1aTTNiaWV2Q243YnpDZUR4ZWY4X1ZnSVphVWhyQkFTU1N4OWZuQm9wUXVSR3N3cGJlaXRVZkRBVWVoLXVaV0RxTldPNGciLCIvY3JlZGVudGlhbFN1YmplY3QvZmFtaWx5TmFtZSIsIkNhcnJvbGwiXQ
```

And this SD-JWS object:

```json
{
    "id": "urn:uuid:9bcc9aaa-3bdc-4414-9450-739c295c752c",
    "credentialSubject": {
        "givenName": "Lewis"
    },
    "_sd": [
        "nVVTp7q-BDBlW11YSHD_MFnqvNv4A1d-qHGBR544c-k"
    ],
    "_sd_alg": "sha-256",
    "_sd_typ": "application/json"
}
```

## Applying a disclosure

Given the above disclosure and SD-JWS object, running this code:

```java
Disclosure disclosure = DisclosureGenerator.getInstance().parse(DISLOSURE);
SDJWSObject sdjwsObject = SDJWSObject.fromJson(SDJWS);
sdjwsObject.applyDisclosure(disclosure);
```

Results in this JSON object:

```json
{
    "id": "urn:uuid:9bcc9aaa-3bdc-4414-9450-739c295c752c",
    "credentialSubject": {
        "givenName": "Lewis",
        "familyName": "Carroll"
    }
}
```

## Securing the SD-JWS object:

### JWS Compact Serialization:

Running this code:

```java
SDJWSObject sdjwsObject = SDJWSObject.fromJson(SDJWS);
JWSObject jwsObject = JWSGenerator.getInstance().generateJWSObject(sdjwsObject, DISCLOSURES, JWSAlgorithm.ES256);

ECKey ecKey = new ECKeyGenerator(Curve.P_256).generate();
JWSSigner jwsSigner = new ECDSASigner(ecKey);
jwsObject.sign(jwsSigner);
```

Results in this JWS:

```
eyJiNjQiOmZhbHNlLCJjcml0IjpbImI2NCJdLCJ0eXAiOiJKT1NFIiwiYWxnIjoiRVMyNTYifQ.{"sd":{"@context":[{"value":"https://www.w3.org/ns/credentials/v2"}],"id":{"value":"urn:uuid:9bcc9aaa-3bdc-4414-9450-739c295c752c"},"issuer":{"value":"did:ebsi:zvHWX359A3CvfJnCYaAiAde"},"validFrom":{"value":"2023-01-01T00:00:00Z"},"validUntil":{"value":"2033-01-01T00:00:00Z"},"credentialSubject":{"id":{"value":"did:key:z2dmzD81cgPx8Vki7JbuuMmFYrWPgYoytykUZ3eyqht1j9KbsDbVZXdb3jzCagESyY4EE2x7Yjx3gNwctoEuRCKKDrdNP3HPFtG8RTvBiYStT5ghBHhHizH2Dy6xQtW3Pd2SecizL9b2jzDCMr7Ka5cRAWZFwvqwAtwTT7xet769y9ERh6"},"birthDate":{"value":"1832-01-27"},"student":{"valueType":"TRUE"}},"credentialSchema":{"id":{"value":"https://api-pilot.ebsi.eu/trusted-schemas-registry/v2/schemas/0x23039e6356ea6b703ce672e7cfac0b42765b150f63df78e2bd18ae785787f6a2"},"type":{"value":"JsonSchema"}},"_sd":[{"value":"PWpJuNXN0WZdcuhVtoizB2tHRYNr0AwyeXik2nwPFAc"},{"value":"cCPq3HfqiTNptfkMsMgUROh_YCdldFs8vRm1IK3Mgdc"},{"value":"PQBLWVCO7Gcs_zLAdmdL5BSzu3y2DHjb97oOo9FXsZs"}],"_sd_alg":{"value":"sha-256"},"_sd_typ":{"value":"application/vc+ld+json"}},"disclosures":[{"salt":"2GLC42sKQveCfGfryNRN9w","jsonPointer":{"tokens":["","credentialSubject","familyName"],"jsonPointer":"/credentialSubject/familyName"},"jsonValue":{"value":"Carroll"},"disclosureJson":[{"value":"2GLC42sKQveCfGfryNRN9w"},{"value":"/credentialSubject/familyName"},{"value":"Carroll"}],"disclosureString":"WyIyR0xDNDJzS1F2ZUNmR2ZyeU5STjl3IiwgIi9jcmVkZW50aWFsU3ViamVjdC9mYW1pbHlOYW1lIiwgIkNhcnJvbGwiXQ"},{"salt":"eluV5Og3gSNII8EYnsxA_A","jsonPointer":{"tokens":["","credentialSubject","givenName"],"jsonPointer":"/credentialSubject/givenName"},"jsonValue":{"value":"Lewis"},"disclosureJson":[{"value":"eluV5Og3gSNII8EYnsxA_A"},{"value":"/credentialSubject/givenName"},{"value":"Lewis"}],"disclosureString":"WyJlbHVWNU9nM2dTTklJOEVZbnN4QV9BIiwgIi9jcmVkZW50aWFsU3ViamVjdC9naXZlbk5hbWUiLCAiTGV3aXMiXQ"},{"salt":"6Ij7tM-a5iVPGboS5tmvVA","jsonPointer":{"tokens":["","type"],"jsonPointer":"/type"},"jsonValue":[{"value":"VerifiableCredential"},{"value":"StudentID"}],"disclosureJson":[{"value":"6Ij7tM-a5iVPGboS5tmvVA"},{"value":"/type"},[{"value":"VerifiableCredential"},{"value":"StudentID"}]],"disclosureString":"WyI2SWo3dE0tYTVpVlBHYm9TNXRtdlZBIiwgIi90eXBlIiwgWyJWZXJpZmlhYmxlQ3JlZGVudGlhbCIsICJTdHVkZW50SUQiXV0"}]}.Z7HEDgzszmO4SYif5z-eILyPYsfUpFWRy8E0LAov908jNHNpn59-sApJfhCJSCOqXDCCDYrEE_Zv9BleZWBIcg
```

### Flattened JWS JSON Serialization Syntax:

Running this code:

```java
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
```

Results in this JWS:

```json
{
    "protected": "eyJiNjQiOmZhbHNlLCJjcml0IjpbImI2NCJdLCJ0eXAiOiJKT1NFIiwiYWxnIjoiRVMyNTYifQ",
    "payload": "eyJzZCI6eyJAY29udGV4dCI6W3sidmFsdWUiOiJodHRwczovL3d3dy53My5vcmcvbnMvY3JlZGVudGlhbHMvdjIifV0sImlkIjp7InZhbHVlIjoidXJuOnV1aWQ6OWJjYzlhYWEtM2JkYy00NDE0LTk0NTAtNzM5YzI5NWM3NTJjIn0sImlzc3VlciI6eyJ2YWx1ZSI6ImRpZDplYnNpOnp2SFdYMzU5QTNDdmZKbkNZYUFpQWRlIn0sInZhbGlkRnJvbSI6eyJ2YWx1ZSI6IjIwMjMtMDEtMDFUMDA6MDA6MDBaIn0sInZhbGlkVW50aWwiOnsidmFsdWUiOiIyMDMzLTAxLTAxVDAwOjAwOjAwWiJ9LCJjcmVkZW50aWFsU3ViamVjdCI6eyJpZCI6eyJ2YWx1ZSI6ImRpZDprZXk6ejJkbXpEODFjZ1B4OFZraTdKYnV1TW1GWXJXUGdZb3l0eWtVWjNleXFodDFqOUtic0RiVlpYZGIzanpDYWdFU3lZNEVFMng3WWp4M2dOd2N0b0V1UkNLS0RyZE5QM0hQRnRHOFJUdkJpWVN0VDVnaEJIaEhpekgyRHk2eFF0VzNQZDJTZWNpekw5YjJqekRDTXI3S2E1Y1JBV1pGd3Zxd0F0d1RUN3hldDc2OXk5RVJoNiJ9LCJiaXJ0aERhdGUiOnsidmFsdWUiOiIxODMyLTAxLTI3In0sInN0dWRlbnQiOnsidmFsdWVUeXBlIjoiVFJVRSJ9fSwiY3JlZGVudGlhbFNjaGVtYSI6eyJpZCI6eyJ2YWx1ZSI6Imh0dHBzOi8vYXBpLXBpbG90LmVic2kuZXUvdHJ1c3RlZC1zY2hlbWFzLXJlZ2lzdHJ5L3YyL3NjaGVtYXMvMHgyMzAzOWU2MzU2ZWE2YjcwM2NlNjcyZTdjZmFjMGI0Mjc2NWIxNTBmNjNkZjc4ZTJiZDE4YWU3ODU3ODdmNmEyIn0sInR5cGUiOnsidmFsdWUiOiJKc29uU2NoZW1hIn19LCJfc2QiOlt7InZhbHVlIjoiUFdwSnVOWE4wV1pkY3VoVnRvaXpCMnRIUllOcjBBd3llWGlrMm53UEZBYyJ9LHsidmFsdWUiOiJjQ1BxM0hmcWlUTnB0ZmtNc01nVVJPaF9ZQ2RsZEZzOHZSbTFJSzNNZ2RjIn0seyJ2YWx1ZSI6IlBRQkxXVkNPN0djc196TEFkbWRMNUJTenUzeTJESGpiOTdvT285RlhzWnMifV0sIl9zZF9hbGciOnsidmFsdWUiOiJzaGEtMjU2In0sIl9zZF90eXAiOnsidmFsdWUiOiJhcHBsaWNhdGlvbi92YytsZCtqc29uIn19LCJkaXNjbG9zdXJlcyI6W3sic2FsdCI6IjJHTEM0MnNLUXZlQ2ZHZnJ5TlJOOXciLCJqc29uUG9pbnRlciI6eyJ0b2tlbnMiOlsiIiwiY3JlZGVudGlhbFN1YmplY3QiLCJmYW1pbHlOYW1lIl0sImpzb25Qb2ludGVyIjoiL2NyZWRlbnRpYWxTdWJqZWN0L2ZhbWlseU5hbWUifSwianNvblZhbHVlIjp7InZhbHVlIjoiQ2Fycm9sbCJ9LCJkaXNjbG9zdXJlSnNvbiI6W3sidmFsdWUiOiIyR0xDNDJzS1F2ZUNmR2ZyeU5STjl3In0seyJ2YWx1ZSI6Ii9jcmVkZW50aWFsU3ViamVjdC9mYW1pbHlOYW1lIn0seyJ2YWx1ZSI6IkNhcnJvbGwifV0sImRpc2Nsb3N1cmVTdHJpbmciOiJXeUl5UjB4RE5ESnpTMUYyWlVObVIyWnllVTVTVGpsM0lpd2dJaTlqY21Wa1pXNTBhV0ZzVTNWaWFtVmpkQzltWVcxcGJIbE9ZVzFsSWl3Z0lrTmhjbkp2Ykd3aVhRIn0seyJzYWx0IjoiZWx1VjVPZzNnU05JSThFWW5zeEFfQSIsImpzb25Qb2ludGVyIjp7InRva2VucyI6WyIiLCJjcmVkZW50aWFsU3ViamVjdCIsImdpdmVuTmFtZSJdLCJqc29uUG9pbnRlciI6Ii9jcmVkZW50aWFsU3ViamVjdC9naXZlbk5hbWUifSwianNvblZhbHVlIjp7InZhbHVlIjoiTGV3aXMifSwiZGlzY2xvc3VyZUpzb24iOlt7InZhbHVlIjoiZWx1VjVPZzNnU05JSThFWW5zeEFfQSJ9LHsidmFsdWUiOiIvY3JlZGVudGlhbFN1YmplY3QvZ2l2ZW5OYW1lIn0seyJ2YWx1ZSI6Ikxld2lzIn1dLCJkaXNjbG9zdXJlU3RyaW5nIjoiV3lKbGJIVldOVTluTTJkVFRrbEpPRVZaYm5ONFFWOUJJaXdnSWk5amNtVmtaVzUwYVdGc1UzVmlhbVZqZEM5bmFYWmxiazVoYldVaUxDQWlUR1YzYVhNaVhRIn0seyJzYWx0IjoiNklqN3RNLWE1aVZQR2JvUzV0bXZWQSIsImpzb25Qb2ludGVyIjp7InRva2VucyI6WyIiLCJ0eXBlIl0sImpzb25Qb2ludGVyIjoiL3R5cGUifSwianNvblZhbHVlIjpbeyJ2YWx1ZSI6IlZlcmlmaWFibGVDcmVkZW50aWFsIn0seyJ2YWx1ZSI6IlN0dWRlbnRJRCJ9XSwiZGlzY2xvc3VyZUpzb24iOlt7InZhbHVlIjoiNklqN3RNLWE1aVZQR2JvUzV0bXZWQSJ9LHsidmFsdWUiOiIvdHlwZSJ9LFt7InZhbHVlIjoiVmVyaWZpYWJsZUNyZWRlbnRpYWwifSx7InZhbHVlIjoiU3R1ZGVudElEIn1dXSwiZGlzY2xvc3VyZVN0cmluZyI6Ild5STJTV28zZEUwdFlUVnBWbEJIWW05VE5YUnRkbFpCSWl3Z0lpOTBlWEJsSWl3Z1d5SldaWEpwWm1saFlteGxRM0psWkdWdWRHbGhiQ0lzSUNKVGRIVmtaVzUwU1VRaVhWMCJ9XX0",
    "signature": "ngLVDK0g1Mf-UPiscpHARkNZh_YetrXE2Tdc05kstKKl_J82VbiSV74_wgCKCo6VlGy2A5pvi9NFLnTbUNTHHg"
}
```

## About

Danube Tech - https://danubetech.com/

<br clear="left" />
