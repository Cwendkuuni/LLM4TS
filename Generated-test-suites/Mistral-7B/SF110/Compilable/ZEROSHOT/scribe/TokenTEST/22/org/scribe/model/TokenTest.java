package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

 private String token = "testToken";
 private String secret = "testSecret";
 private String rawResponse = "testRawResponse";

 @Test
 public void testTokenConstructionWithTokenAndSecret() {
     Token tokenObject = new Token(token, secret);
     assertEquals("testToken", tokenObject.getToken());
     assertEquals("testSecret", tokenObject.getSecret());
     assertNull(tokenObject.getRawResponse());
 }

 @Test
 public void testTokenConstructionWithTokenSecretAndRawResponse() {
     Token tokenObject = new Token(token, secret, rawResponse);
     assertEquals("testToken", tokenObject.getToken());
     assertEquals("testSecret", tokenObject.getSecret());
     assertEquals("testRawResponse", tokenObject.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRawResponseWhenNotConstructedWithRawResponse() {
     Token tokenObject = new Token(token, secret);
     tokenObject.getRawResponse();
 }

 @Test
 public void testToStringMethod() {
     Token tokenObject = new Token(token, secret);
     String expectedString = "Token[testToken , testSecret]";
     assertEquals(expectedString, tokenObject.toString());
 }
}