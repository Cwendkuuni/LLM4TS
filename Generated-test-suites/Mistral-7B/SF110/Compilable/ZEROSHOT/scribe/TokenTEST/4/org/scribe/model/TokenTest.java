package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

 private String token = "abcd1234";
 private String secret = "efgh5678";

 @Test
 public void testTokenConstructionWithTokenAndSecret() {
     Token tokenObject = new Token(token, secret);
     assertEquals(token, tokenObject.getToken());
     assertEquals(secret, tokenObject.getSecret());
 }

 @Test(expected = IllegalStateException.class)
 public void testTokenConstructionWithTokenAndSecretAndRawResponseNull() {
     Token tokenObject = new Token(token, secret);
     try {
         tokenObject.getRawResponse();
         fail("Expected IllegalStateException");
     } catch (IllegalStateException e) {
         // Expected
     }
 }

 @Test
 public void testTokenConstructionWithTokenAndSecretAndRawResponse() {
     String rawResponse = "{\"access_token\":\"" + token + "\",\"token_type\":\"Bearer\"}";
     Token tokenObject = new Token(token, secret, rawResponse);
     assertEquals(token, tokenObject.getToken());
     assertEquals(secret, tokenObject.getSecret());
     assertEquals(rawResponse, tokenObject.getRawResponse());
 }

 @Test
 public void testToStringMethod() {
     Token tokenObject = new Token(token, secret);
     String expected = String.format("Token[%s , %s]", token, secret);
     assertEquals(expected, tokenObject.toString());
 }

}