package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

 private String token = "token123";
 private String secret = "secret456";
 private String rawResponse = "rawResponse789";

 @Test
 public void testTokenConstructorWithTokenAndSecret() {
     Token tokenObject = new Token(token, secret);
     assertEquals(token, tokenObject.getToken());
     assertEquals(secret, tokenObject.getSecret());
     assertNull(tokenObject.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testTokenConstructorWithTokenAndSecretAndRawResponseThrowsException() {
     String rawResponse = "rawResponse111";
     Token tokenObject = new Token(token, secret, rawResponse);
 }

 @Test
 public void testTokenConstructorWithTokenAndSecretAndRawResponse() {
     Token tokenObject = new Token(token, secret, rawResponse);
     assertEquals(token, tokenObject.getToken());
     assertEquals(secret, tokenObject.getSecret());
     assertEquals(rawResponse, tokenObject.getRawResponse());
 }

 @Test
 public void testGetToken() {
     Token tokenObject = new Token(token, secret);
     Token tokenObject2 = new Token(token, secret, rawResponse);
     assertEquals(token, tokenObject.getToken());
     assertEquals(token, tokenObject2.getToken());
 }

 @Test
 public void testGetSecret() {
     Token tokenObject = new Token(token, secret);
     Token tokenObject2 = new Token(token, secret, rawResponse);
     assertEquals(secret, tokenObject.getSecret());
     assertEquals(secret, tokenObject2.getSecret());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRawResponseWhenTokenObjectWasNotConstructedWithRawResponseThrowsException() {
     Token tokenObject = new Token(token, secret);
     tokenObject.getRawResponse();
 }

 @Test
 public void testGetRawResponse() {
     Token tokenObject = new Token(token, secret, rawResponse);
     assertEquals(rawResponse, tokenObject.getRawResponse());
 }

 @Test
 public void testToString() {
     Token tokenObject = new Token(token, secret);
     Token tokenObject2 = new Token(token, secret, rawResponse);
     String expectedTokenToString = String.format("Token[%s , %s]", token, secret);
     String tokenObjectToString = tokenObject.toString();
     String tokenObject2ToString = tokenObject2.toString();
     assertEquals(expectedTokenToString, tokenObjectToString);
     assertEquals(expectedTokenToString, tokenObject2ToString);
 }
}