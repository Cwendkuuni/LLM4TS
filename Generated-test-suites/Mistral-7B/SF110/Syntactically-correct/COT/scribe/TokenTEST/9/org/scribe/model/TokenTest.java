package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {

 private String token = "testToken";
 private String secret = "testSecret";

 @Test
 public void testTokenConstructionWithTokenAndSecret() {
 Token tokenObject = new Token(token, secret);
 assertEquals(token, tokenObject.getToken());
 assertEquals(secret, tokenObject.getSecret());
 assertNull(tokenObject.getRawResponse());
 }

 @Test
 public void testTokenConstructionWithTokenSecretAndRawResponse() {
 String rawResponse = "testRawResponse";
 Token tokenObject = new Token(token, secret, rawResponse);
 assertEquals(token, tokenObject.getToken());
 assertEquals(secret, tokenObject.getSecret());
 assertEquals(rawResponse, tokenObject.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRawResponseWhenNotConstructedWithRawResponse() {
 Token tokenObject = new Token(token, secret);
 tokenObject.getRawResponse();
 }

 @Test
 public void testToString() {
 Token tokenObject = new Token(token, secret);
 assertEquals("Token[testToken , testSecret]", tokenObject.toString());
 }

 @Test
 public void testGetTokenNull() {
 assertNull(new Token(null, secret).getToken());
 }

 @Test
 public void testGetSecretNull() {
 assertNull(new Token(token, null).getSecret());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetTokenEmptyString() {
 assertNull(new Token("", secret).getToken());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetSecretEmptyString() {
 assertNull(new Token(token, "", rawResponse).getSecret());
 }

}