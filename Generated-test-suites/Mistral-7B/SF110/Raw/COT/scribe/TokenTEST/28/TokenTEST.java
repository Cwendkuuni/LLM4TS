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
 public void testToString() {
 Token tokenObject = new Token(token, secret);
 String expectedToString = "Token[testToken , testSecret]";
 assertEquals(expectedToString, tokenObject.toString());
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
 public void testGetTokenNullAndSecretNull() {
 assertNull(new Token(null, null).getToken());
 }

 @Test
 public void testGetTokenEmptyString() {
 String emptyToken = "";
 Token tokenObject = new Token(emptyToken, secret);
 assertEquals(emptyToken, tokenObject.getToken());
 }

 @Test
 public void testGetSecretEmptyString() {
 String emptySecret = "";
 Token tokenObject = new Token(token, emptySecret);
 assertEquals(secret, tokenObject.getSecret());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetSecretEmptyStringAndTokenEmptyString() {
 String emptyToken = "";
 String emptySecret = "";
 Token tokenObject = new Token(emptyToken, emptySecret);
 }

 @Test
 public void testToStringWithNullToken() {
 Token tokenObject = new Token(null, secret);
 assertNull(tokenObject.toString());
 }

 @Test
 public void testToStringWithNullSecret() {
 Token tokenObject = new Token(token, null);
 String expectedToString = "Token[null , null]";
 assertEquals(expectedToString, tokenObject.toString());
 }

}