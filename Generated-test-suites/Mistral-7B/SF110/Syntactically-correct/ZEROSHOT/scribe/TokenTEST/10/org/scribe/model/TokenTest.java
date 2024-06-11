package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

 private String token = "testToken";
 private String secret = "testSecret";

 @Test
 public void testTokenConstructionWithTokenAndSecret() {
     Token tokenObj = new Token(token, secret);
     assertEquals(token, tokenObj.getToken());
     assertEquals(secret, tokenObj.getSecret());
     assertNull(tokenObj.getRawResponse());
 }

 @Test
 public void testTokenConstructionWithTokenSecretAndRawResponse() {
     String rawResponse = "testRawResponse";
     Token tokenObj = new Token(token, secret, rawResponse);
     assertEquals(token, tokenObj.getToken());
     assertEquals(secret, tokenObj.getSecret());
     assertEquals(rawResponse, tokenObj.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalStateExceptionWhenGettingRawResponseWithoutIt() {
     Token tokenObj = new Token(token, secret);
     tokenObj.getRawResponse();
 }

 @Test
 public void testToStringMethod() {
     String expected = String.format("Token[%s , %s]", token, secret);
     String actual = tokenObj.toString();
     assertEquals(expected, actual);
 }
}