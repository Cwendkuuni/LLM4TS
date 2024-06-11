package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenTest {

 @Test
 public void testTokenWithTokenAndSecret() {
 Token token = new Token("tokenValue", "secretValue");
 assertEquals("tokenValue", token.getToken());
 assertEquals("secretValue", token.getSecret());
 assertNull(token.getRawResponse());
 }

 @Test
 public void testTokenWithTokenAndSecretAndRawResponse() {
 Token token = new Token("tokenValue", "secretValue", "rawResponseValue");
 assertEquals("tokenValue", token.getToken());
 assertEquals("secretValue", token.getSecret());
 assertEquals("rawResponseValue", token.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRawResponseWithoutIt() {
 Token token = new Token("tokenValue", "secretValue");
 token.getRawResponse();
 }

 @Test
 public void testToString() {
 Token token = new Token("tokenValue", "secretValue");
 assertEquals("Token[tokenValue , secretValue]", token.toString());
 }
}