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
     String rawResponse = "testRawResponse";
     Token tokenObject = new Token(token, secret, rawResponse);
     fail("Expected IllegalStateException to be thrown");
 }

 @Test
 public void testTokenToStringMethod() {
     Token tokenObject = new Token(token, secret);
     assertEquals("Token[testToken , testSecret]", tokenObject.toString());
 }

 @Test
 public void testGetTokenMethod() {
     Token tokenObject = new Token(token, secret);
     assertEquals(token, tokenObject.getToken());
 }

 @Test
 public void testGetSecretMethod() {
     Token tokenObject = new Token(token, secret);
     assertEquals(secret, tokenObject.getSecret());
 }

 @Test
 public void testGetRawResponseMethodWithNullRawResponse() {
     Token tokenObject = new Token(token, secret);
     try {
         tokenObject.getRawResponse();
         fail("Expected IllegalStateException to be thrown");
     } catch (IllegalStateException e) {
         // expected
     }
 }

 @Test
 public void testGetRawResponseMethodWithRawResponse() {
     Token tokenObject = new Token(token, secret, rawResponse);
     assertEquals(rawResponse, tokenObject.getRawResponse());
 }
}