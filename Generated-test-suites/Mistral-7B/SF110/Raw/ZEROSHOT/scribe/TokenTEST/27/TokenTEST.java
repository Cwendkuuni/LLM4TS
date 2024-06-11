package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

 private String token = "sampleToken";
 private String secret = "sampleSecret";

 @Test
 public void testTokenConstructionWithTokenAndSecret() {
     Token tokenObject = new Token(token, secret);
     assertEquals(token, tokenObject.getToken());
     assertEquals(secret, tokenObject.getSecret());
 }

 @Test(expected = IllegalStateException.class)
 public void testTokenConstructionWithTokenAndSecretWithoutRawResponse() {
     new Token(token, secret);
 }

 @Test
 public void testTokenConstructionWithTokenAndSecretAndRawResponse() {
     String rawResponse = "sampleRawResponse";
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
     String rawResponse = "sampleRawResponse";
     Token tokenObject = new Token(token, secret, rawResponse);
     assertEquals(rawResponse, tokenObject.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRawResponseWithoutRawResponse() {
     Token tokenObject = new Token(token, secret);
     try {
         tokenObject.getRawResponse();
     } catch (IllegalStateException e) {
         fail("Expected IllegalStateException, but got: " + e.getMessage());
     }
 }

 @Test
 public void testToString() {
     Token tokenObject = new Token(token, secret);
     String expected = String.format("Token[%s , %s]", token, secret);
     assertEquals(expected, tokenObject.toString());
 }
}