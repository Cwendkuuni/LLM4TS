package org.scribe.model;

import org.junit.Test;
import org.junit.Assert.*;

public class TokenTest {

 private String token = "abc123";
 private String secret = "def456";
 private String rawResponse = "response from server";

 @Test
 public void testTokenConstructionWithTokenAndSecret() {
     Token tokenObj = new Token(token, secret);
     Assert.assertEquals(token, tokenObj.getToken());
     Assert.assertEquals(secret, tokenObj.getSecret());
     Assert.assertNull(tokenObj.getRawResponse());
 }

 @Test
 public void testTokenConstructionWithTokenSecretAndRawResponse() {
     Token tokenObj = new Token(token, secret, rawResponse);
     Assert.assertEquals(token, tokenObj.getToken());
     Assert.assertEquals(secret, tokenObj.getSecret());
     Assert.assertEquals(rawResponse, tokenObj.getRawResponse());
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
     String actual = tokenObj.toString();
     Assert.assertEquals(expected, actual);
 }
}