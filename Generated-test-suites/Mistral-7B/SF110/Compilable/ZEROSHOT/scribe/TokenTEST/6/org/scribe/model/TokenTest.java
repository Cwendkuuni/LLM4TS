package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

 private String token = "myToken";
 private String secret = "mySecret";
 private String rawResponse = "myRawResponse";

 @Test
 public void testTokenConstructionWithTokenAndSecret() {
     Token tokenObj = new Token(token, secret);
     assertEquals("myToken", tokenObj.getToken());
     assertEquals("mySecret", tokenObj.getSecret());
     assertNull(tokenObj.getRawResponse());
 }

 @Test
 public void testTokenConstructionWithTokenSecretAndRawResponse() {
     Token tokenObj = new Token(token, secret, rawResponse);
     assertEquals("myToken", tokenObj.getToken());
     assertEquals("mySecret", tokenObj.getSecret());
     assertEquals("myRawResponse", tokenObj.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalStateExceptionWhenRawResponseIsNull() {
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