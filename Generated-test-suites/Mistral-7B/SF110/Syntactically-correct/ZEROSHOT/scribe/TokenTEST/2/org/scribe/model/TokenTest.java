package org.scribe.model; // make sure to import necessary packages at the top of your test file

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

 private String token = "sampleToken";
 private String secret = "sampleSecret";
 private String rawResponse = "sampleRawResponse";

 @Test
 public void testTokenConstructionWithTokenAndSecret() {
     Token tokenObj = new Token(token, secret);
     assertEquals(token, tokenObj.getToken());
     assertEquals(secret, tokenObj.getSecret());
     assertNull(tokenObj.getRawResponse());
 }

 @Test
 public void testTokenConstructionWithTokenSecretAndRawResponse() {
     Token tokenObj = new Token(token, secret, rawResponse);
     assertEquals(token, tokenObj.getToken());
     assertEquals(secret, tokenObj.getSecret());
     assertEquals(rawResponse, tokenObj.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRawResponseWhenNotConstructedWithRawResponse() {
     Token tokenObj = new Token(token, secret);
     tokenObj.getRawResponse();
 }

 @Test
 public void testToStringMethod() {
     Token tokenObj = new Token(token, secret, rawResponse);
     String expected = String.format("Token[%s , %s]", token, secret);
     assertEquals(expected, tokenObj.toString());
 }
}