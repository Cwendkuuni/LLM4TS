package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TokenTest {

 @Test
 public void testTokenConstructorWithTokenAndSecret() {
 Token token = new Token("tokenValue", "secretValue");
 assertNotNull(token);
 assertEquals("tokenValue", token.getToken());
 assertEquals("secretValue", token.getSecret());
 assertNull(token.getRawResponse());
 }

 @Test
 public void testTokenConstructorWithTokenSecretAndRawResponse() {
 Token token = new Token("tokenValue", "secretValue", "rawResponseValue");
 assertNotNull(token);
 assertEquals("tokenValue", token.getToken());
 assertEquals("secretValue", token.getSecret());
 assertEquals("rawResponseValue", token.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRawResponseWhenTokenIsNotConstructedWithRawResponse() {
 Token token = new Token("tokenValue", "secretValue");
 assertThrows(IllegalStateException.class, () -> token.getRawResponse());
 }

 @Test
 public void testToStringMethod() {
 Token token = new Token("tokenValue", "secretValue");
 assertEquals("Token[tokenValue , secretValue]", token.toString());
 }
}