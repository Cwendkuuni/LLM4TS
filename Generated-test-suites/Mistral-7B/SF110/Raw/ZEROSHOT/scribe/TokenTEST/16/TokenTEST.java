package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

 private String token = "abcde12345";
 private String secret = "12345abcde";
 private String rawResponse = "{\"access_token\":\"abcde12345\",\"token_type\":\"Bearer\",\"expires_in\":3600}";

 @Test
 public void testTokenConstructionWithTokenAndSecret() {
     Token tokenObj = new Token(token, secret);
     assertEquals(token, tokenObj.getToken());
     assertEquals(secret, tokenObj.getSecret());
     assertNull(tokenObj.getRawResponse());
 }

 @Test
 public void testTokenConstructionWithTokenAndSecretAndRawResponse() {
     Token tokenObj = new Token(token, secret, rawResponse);
     assertEquals(token, tokenObj.getToken());
     assertEquals(secret, tokenObj.getSecret());
     assertEquals(rawResponse, tokenObj.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRawResponseWhenTokenIsNotConstructedWithRawResponse() {
     Token tokenObj = new Token(token, secret);
     tokenObj.getRawResponse();
 }

 @Test
 public void testToStringMethod() {
     Token tokenObj = new Token(token, secret);
     String expected = String.format("Token[%s , %s]", token, secret);
     assertEquals(expected, tokenObj.toString());
 }
}