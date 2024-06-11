package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

 private String token = "abc123";
 private String secret = "def456";
 private String rawResponse = "response from server";

 @Test
 public void testTokenConstructionWithTokenAndSecret() {
     Token tokenObject = new Token(token, secret);
     assertEquals(token, tokenObject.getToken());
     assertEquals(secret, tokenObject.getSecret());
     assertNull(tokenObject.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRawResponseWhenNotConstructedWithRawResponse() {
     Token tokenObject = new Token(token, secret);
     tokenObject.getRawResponse();
 }

 @Test
 public void testTokenConstructionWithTokenSecretAndRawResponse() {
     Token tokenObject = new Token(token, secret, rawResponse);
     assertEquals(token, tokenObject.getToken());
     assertEquals(secret, tokenObject.getSecret());
     assertEquals(rawResponse, tokenObject.getRawResponse());
 }

 @Test
 public void testToString() {
     Token tokenObject = new Token(token, secret, rawResponse);
     String expected = String.format("Token[%s , %s]", token, secret);
     assertEquals(expected, tokenObject.toString());
 }
}