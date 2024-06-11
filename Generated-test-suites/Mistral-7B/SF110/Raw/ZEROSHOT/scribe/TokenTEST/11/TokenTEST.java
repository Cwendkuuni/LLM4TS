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
     assertEquals(token, tokenObject.getToken());
     assertEquals(secret, tokenObject.getSecret());
     assertNull(tokenObject.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testTokenConstructionWithTokenAndSecretAndRawResponseThrowsException() {
     Token tokenObject = new Token(token, secret, rawResponse);
     fail("Should have thrown IllegalStateException");
 }

 @Test
 public void testTokenConstructionWithTokenAndSecretAndRawResponse() {
     Token tokenObject = new Token(token, secret, rawResponse);
     assertEquals(token, tokenObject.getToken());
     assertEquals(secret, tokenObject.getSecret());
     assertEquals(rawResponse, tokenObject.getRawResponse());
 }

 @Test
 public void testGetToken() {
     Token tokenObject = new Token(token, secret);
     assertEquals(token, tokenObject.getToken());
 }

 @Test
 public void testGetSecret() {
     Token tokenObject = new Token(token, secret);
     assertEquals(secret, tokenObject.getSecret());
 }

 @Test
 public void testGetRawResponse() {
     Token tokenObject = new Token(token, secret, rawResponse);
     assertEquals(rawResponse, tokenObject.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRawResponseWhenTokenObjectHasNoRawResponse() {
     Token tokenObject = new Token(token, secret);
     tokenObject.getRawResponse();
     fail("Should have thrown IllegalStateException");
 }

 @Test
 public void testToString() {
     Token tokenObject = new Token(token, secret, rawResponse);
     String expected = String.format("Token[%s , %s]", token, secret);
     assertEquals(expected, tokenObject.toString());
 }
}