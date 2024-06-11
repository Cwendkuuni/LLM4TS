package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {

 private String token = "testToken";
 private String secret = "testSecret";

 @Test
 public void testTokenConstructorWithTokenAndSecret() {
 Token tokenObject = new Token(token, secret);
 assertEquals(token, tokenObject.getToken());
 assertEquals(secret, tokenObject.getSecret());
 assertNull(tokenObject.getRawResponse());
 }

 @Test
 public void testTokenConstructorWithTokenSecretAndRawResponse() {
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
 public void testToStringMethod() {
 Token tokenObject = new Token(token, secret);
 assertEquals("Token[testToken , testSecret]", tokenObject.toString());
 }

 @Test
 public void testGetTokenWithNullToken() {
 assertNull(new Token(null, secret).getToken());
 }

 @Test
 public void testGetSecretWithNullSecret() {
 assertNull(new Token(token, null).getSecret());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTokenConstructorWithNullToken() {
 new Token(null, secret);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTokenConstructorWithNullSecret() {
 new Token(token, null);
 }

 @Test
 public void testGetTokenWithEmptyString() {
 Token tokenObject = new Token("", secret);
 assertEquals("", tokenObject.getToken());
 }

 @Test
 public void testGetSecretWithEmptyString() {
 Token tokenObject = new Token(token, "");
 assertEquals("", tokenObject.getSecret());
 }
}