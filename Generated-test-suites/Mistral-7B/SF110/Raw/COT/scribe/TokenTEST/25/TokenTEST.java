package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Token;

public class TokenTest {

 private String token = "testToken";
 private String secret = "testSecret";
 private String rawResponse = "testRawResponse";

 @Test
 public void testTokenConstructionWithTokenAndSecret() {
     Token tokenObject = new Token(token, secret);
     String actualToken = tokenObject.getToken();
     String actualSecret = tokenObject.getSecret();
     assertEquals(token, actualToken);
     assertEquals(secret, actualSecret);
     assertNull(tokenObject.getRawResponse());
 }

 @Test
 public void testTokenConstructionWithTokenSecretAndRawResponse() {
     Token tokenObject = new Token(token, secret, rawResponse);
     String actualToken = tokenObject.getToken();
     String actualSecret = tokenObject.getSecret();
     String actualRawResponse = tokenObject.getRawResponse();
     assertEquals(token, actualToken);
     assertEquals(secret, actualSecret);
     assertEquals(rawResponse, actualRawResponse);
 }

 @Test (expected = IllegalStateException.class)
 public void testGetRawResponseWhenRawResponseIsNull() {
     Token tokenObject = new Token(token, secret);
     tokenObject.getRawResponse();
 }

 @Test
 public void testToString() {
     Token tokenObject = new Token(token, secret, rawResponse);
     String expectedString = String.format("Token[%s , %s]", token, secret);
     String actualString = tokenObject.toString();
     assertEquals(expectedString, actualString);
 }

 @Test
 public void testGetTokenNull() {
     Token tokenObject = new Token(null, secret);
     assertNull(tokenObject.getToken());
 }

 @Test
 public void testGetSecretNull() {
     Token tokenObject = new Token(token, null);
     assertNull(tokenObject.getSecret());
 }
}