package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

 private String token = "testToken123";
 private String secret = "testSecret456";
 private String rawResponse = "testRawResponse789";

 @Test
 public void testTokenConstructionWithTokenAndSecret() {
     Token tokenObj = new Token(token, secret);
     assertEquals(token, tokenObj.getToken());
     assertEquals(secret, tokenObj.getSecret());
     assertNull(tokenObj.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testTokenConstructionWithTokenAndSecretAndRawResponseThrowsException() {
     Token tokenObj = new Token(token, secret, rawResponse);
     fail("Expected IllegalStateException, but no exception was thrown");
 }

 @Test
 public void testTokenToStringMethod() {
     Token tokenObj = new Token(token, secret, rawResponse);
     String expected = String.format("Token[%s , %s]", token, secret);
     assertEquals(expected, tokenObj.toString());
 }

 @Test
 public void testGetTokenMethod() {
     Token tokenObj = new Token(token, secret, rawResponse);
     assertEquals(token, tokenObj.getToken());
 }

 @Test
 public void testGetSecretMethod() {
     Token tokenObj = new Token(token, secret, rawResponse);
     assertEquals(secret, tokenObj.getSecret());
 }

 @Test
 public void testGetRawResponseMethodWithNullRawResponse() {
     Token tokenObj = new Token(token, secret);
     assertNull(tokenObj.getRawResponse());
 }

 @Test
 public void testGetRawResponseMethodWithValidRawResponse() {
     Token tokenObj = new Token(token, secret, rawResponse);
     assertEquals(rawResponse, tokenObj.getRawResponse());
 }
}