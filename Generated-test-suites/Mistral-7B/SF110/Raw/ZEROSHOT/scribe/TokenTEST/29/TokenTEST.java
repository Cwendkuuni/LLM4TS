package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

 @Test
 public void testTokenConstructorWithTokenAndSecret() {
 Token token = new Token("abc123", "def456");
 assertEquals("abc123", token.getToken());
 assertEquals("def456", token.getSecret());
 assertNull(token.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testTokenConstructorWithTokenAndSecretAndRawResponse() {
 Token token = new Token("abc123", "def456", "rawResponse");
 assertEquals("abc123", token.getToken());
 assertEquals("def456", token.getSecret());
 assertNotNull(token.getRawResponse());
 throw new IllegalStateException(); // this test should fail with an IllegalStateException
 }

 @Test
 public void testTokenGetters() {
 Token token = new Token("abc123", "def456");
 assertEquals("abc123", token.getToken());
 assertEquals("def456", token.getSecret());
 assertNull(token.getRawResponse());

 Token tokenWithRawResponse = new Token("abc123", "def456", "rawResponse");
 assertEquals("abc123", tokenWithRawResponse.getToken());
 assertEquals("def456", tokenWithRawResponse.getSecret());
 assertEquals("rawResponse", tokenWithRawResponse.getRawResponse());
 }

 @Test
 public void testTokenToString() {
 Token token = new Token("abc123", "def456");
 assertEquals("Token[abc123 , def456]", token.toString());
 }
}