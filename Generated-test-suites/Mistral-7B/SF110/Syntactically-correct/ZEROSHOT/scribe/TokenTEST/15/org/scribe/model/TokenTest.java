package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

 private String token = "token123";
 private String secret = "secret456";
 private String rawResponse = "rawResponse789";

 @Test
 public void testTokenConstructionWithTokenAndSecret() {
     Token tokenObject = new Token(token, secret);
     assertEquals(token, tokenObject.getToken());
     assertEquals(secret, tokenObject.getSecret());
     assertNull(tokenObject.getRawResponse());
 }

 @Test
 public void testTokenConstructionWithTokenSecretAndRawResponse() {
     Token tokenObject = new Token(token, secret, rawResponse);
     assertEquals(token, tokenObject.getToken());
     assertEquals(secret, tokenObject.getSecret());
     assertEquals(rawResponse, tokenObject.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRawResponseWhenNotConstructedWithRawResponse() {
     Token tokenObject = new Token(token, secret);
     tokenObject.getRawResponse();
 }

 @Test
 public void testToStringMethod() {
     Token tokenObject = new Token(token, secret, rawResponse);
     String expectedOutput = String.format("Token[%s , %s]", token, secret);
     assertEquals(expectedOutput, tokenObject.toString());
 }
}