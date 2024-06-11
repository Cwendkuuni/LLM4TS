package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

 private String token = "testToken";
 private String secret = "testSecret";
 private String rawResponse = "testRawResponse";

 @Test
 public void testTokenConstructorWithTokenAndSecret() {
     Token tokenObject = new Token(token, secret);
     assertEquals(token, tokenObject.getToken());
     assertEquals(secret, tokenObject.getSecret());
     assertNull(tokenObject.getRawResponse());
 }

 @Test
 public void testTokenConstructorWithTokenSecretAndRawResponse() {
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
     String expectedString = String.format("Token[%s , %s]", token, secret);
     assertEquals(expectedString, tokenObject.toString());
 }
}