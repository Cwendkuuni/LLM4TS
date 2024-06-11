package org.scribe.model.test;

import static org.junit.Assert.*;

import org.scribe.model.Token;
import org.junit.Test;

public class TokenTest {

 private String token = "testToken";
 private String secret = "testSecret";

 @Test
 public void testTokenConstructorWithTokenAndSecret() {
 Token tokenObj = new Token(token, secret);
 assertEquals(token, tokenObj.getToken());
 assertEquals(secret, tokenObj.getSecret());
 }

 @Test
 public void testTokenConstructorWithTokenSecretAndRawResponse() {
 String rawResponse = "testRawResponse";
 Token tokenObj = new Token(token, secret, rawResponse);
 assertEquals(token, tokenObj.getToken());
 assertEquals(secret, tokenObj.getSecret());
 assertEquals(rawResponse, tokenObj.getRawResponse());
 }

 @Test
 public void testGetToken() {
 Token tokenObj = new Token(token, secret);
 Token tokenObj2 = new Token(token, secret, null);
 assertEquals(token, tokenObj.getToken());
 assertEquals(token, tokenObj2.getToken());
 }

 @Test
 public void testGetSecret() {
 Token tokenObj = new Token(token, secret);
 Token tokenObj2 = new Token(token, secret, null);
 assertEquals(secret, tokenObj.getSecret());
 assertEquals(secret, tokenObj2.getSecret());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRawResponseWhenObjectIsNotConstructedByScribe() {
 Token tokenObj = new Token(token, secret);
 tokenObj.getRawResponse();
 }

 @Test
 public void testToString() {
 Token tokenObj = new Token(token, secret);
 String expected = "Token[testToken , testSecret]";
 assertEquals(expected, tokenObj.toString());
 }

 @Test
 public void testEmptyConstructor() {
 Token tokenObj = new Token();
 assertNull(tokenObj.getToken());
 assertNull(tokenObj.getSecret());
 assertNull(tokenObj.getRawResponse());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTokenConstructorWithNullToken() {
 String nullToken = null;
 String secret = "testSecret";
 Token tokenObj = new Token(nullToken, secret);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTokenConstructorWithNullSecret() {
 String token = "testToken";
 String nullSecret = null;
 Token tokenObj = new Token(token, nullSecret);
 }

}