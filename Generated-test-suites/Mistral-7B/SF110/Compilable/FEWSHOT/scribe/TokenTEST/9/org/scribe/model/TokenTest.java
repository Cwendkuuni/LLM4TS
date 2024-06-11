package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenTest {

 @Test
 public void testTokenConstructionWithTokenAndSecret() {
 String token = "token123";
 String secret = "secret123";
 Token tokenObject = new Token(token, secret);
 assertEquals(token, tokenObject.getToken());
 assertEquals(secret, tokenObject.getSecret());
 assertNull(tokenObject.getRawResponse());
 }

 @Test
 public void testTokenConstructionWithTokenSecretAndRawResponse() {
 String token = "token123";
 String secret = "secret123";
 String rawResponse = "raw response data";
 Token tokenObject = new Token(token, secret, rawResponse);
 assertEquals(token, tokenObject.getToken());
 assertEquals(secret, tokenObject.getSecret());
 assertEquals(rawResponse, tokenObject.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalStateExceptionWhenGettingRawResponseWithoutRawResponse() {
 String token = "token123";
 String secret = "secret123";
 Token tokenObject = new Token(token, secret);
 tokenObject.getRawResponse();
 }

 @Test
 public void testToStringMethod() {
 String token = "token123";
 String secret = "secret123";
 Token tokenObject = new Token(token, secret);
 assertEquals("Token[token123 , secret123]", tokenObject.toString());
 }
}