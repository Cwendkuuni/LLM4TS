package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

 private Token validToken;
 private Token invalidToken;

 @Test
 public void testTokenConstructorWithTokenAndSecret() {
     validToken = new Token("1234567890", "abcd1234efgh");
     assertNotNull(validToken);
     assertEquals("1234567890", validToken.getToken());
     assertEquals("abcd1234efgh", validToken.getSecret());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTokenConstructorWithNullToken() {
     try {
         new Token(null, "secret");
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         // Expected exception
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTokenConstructorWithNullSecret() {
     try {
         new Token("token", null);
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         // Expected exception
     }
 }

 @Test
 public void testTokenConstructorWithTokenSecretAndRawResponse() {
     String rawResponse = "{\"access_token\":\"1234567890\", \"expires_in\":3600, \"refresh_token\":\"abcd1234efgh\"}";
     validToken = new Token("1234567890", "abcd1234efgh", rawResponse);
     assertNotNull(validToken);
     assertEquals("1234567890", validToken.getToken());
     assertEquals("abcd1234efgh", validToken.getSecret());
     assertEquals(rawResponse, validToken.getRawResponse());
 }

 @Test
 public void testTokenGetters() {
     assertNotNull(validToken);
     assertEquals("1234567890", validToken.getToken());
     assertEquals("abcd1234efgh", validToken.getSecret());
     assertNotNull(validToken.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testTokenGetRawResponseWhenNull() {
     invalidToken = new Token("1234567890", "abcd1234efgh");
     try {
         invalidToken.getRawResponse();
         fail("Expected IllegalStateException");
     } catch (IllegalStateException e) {
         // Expected exception
     }
 }

 @Test
 public void testTokenToString() {
     assertNotNull(validToken);
     String expected = String.format("Token[%s , %s]", validToken.getToken(), validToken.getSecret());
     assertEquals(expected, validToken.toString());
 }
}