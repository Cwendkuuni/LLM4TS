package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

 private String token = "TEST_TOKEN";
 private String secret = "TEST_SECRET";
 private String rawResponse = "TEST_RAW_RESPONSE";

 @Test
 public void testTokenConstructor() {
     Token tokenObj = new Token(token, secret);
     assertEquals(token, tokenObj.getToken());
     assertEquals(secret, tokenObj.getSecret());
     assertNull(tokenObj.getRawResponse());
 }

 @Test
 public void testTokenConstructorWithRawResponse() {
     Token tokenObj = new Token(token, secret, rawResponse);
     assertEquals(token, tokenObj.getToken());
     assertEquals(secret, tokenObj.getSecret());
     assertEquals(rawResponse, tokenObj.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRawResponseWhenNotConstructedByScribe() {
     Token tokenObj = new Token(token, secret);
     assertNull(tokenObj.getRawResponse());
     tokenObj.getRawResponse();
 }

 @Test
 public void testToString() {
     Token tokenObj = new Token(token, secret, rawResponse);
     String expected = String.format("Token[%s , %s]", token, secret);
     assertEquals(expected, tokenObj.toString());
 }
}