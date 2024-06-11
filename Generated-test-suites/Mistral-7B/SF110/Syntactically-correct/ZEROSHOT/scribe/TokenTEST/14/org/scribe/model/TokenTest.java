package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

 private String token = "abcdefg1234567890";
 private String secret = "hijklmnopqrstuvwxyz1234567890";

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
     String rawResponse = "{\"access_token\":\"" + token + "\",\"access_token_secret\":\"" + secret + "\"}";
     Token tokenObject = new Token(token, secret, rawResponse);
     assertEquals(token, tokenObject.getToken());
     assertEquals(secret, tokenObject.getSecret());
     assertEquals(rawResponse, tokenObject.getRawResponse());
 }

 @Test
 public void testGetToken() {
     Token tokenObject = new Token(token, secret, null);
     assertEquals(token, tokenObject.getToken());
 }

 @Test
 public void testGetSecret() {
     Token tokenObject = new Token(token, secret, null);
     assertEquals(secret, tokenObject.getSecret());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRawResponseWithoutRawResponse() {
     Token tokenObject = new Token(token, secret, null);
     tokenObject.getRawResponse();
 }

 @Test
 public void testGetRawResponseWithRawResponse() {
     String rawResponse = "{\"access_token\":\"" + token + "\",\"access_token_secret\":\"" + secret + "\"}";
     Token tokenObject = new Token(token, secret, rawResponse);
     assertEquals(rawResponse, tokenObject.getRawResponse());
 }

 @Test
 public void testToString() {
     Token tokenObject = new Token(token, secret, null);
     String expectedToString = "Token[abcdefg1234567890 , hijklmnopqrstuvwxyz1234567890]";
     assertEquals(expectedToString, tokenObject.toString());
 }
}