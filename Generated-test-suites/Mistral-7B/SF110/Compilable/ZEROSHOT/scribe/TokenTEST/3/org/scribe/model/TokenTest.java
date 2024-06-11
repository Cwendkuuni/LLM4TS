package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

 @Test
 public void testTokenConstructionWithTokenAndSecret() {
 Token token = new Token("testToken", "testSecret");
 assertEquals("testToken", token.getToken());
 assertEquals("testSecret", token.getSecret());
 assertNull(token.getRawResponse());
 }

 @Test
 public void testTokenConstructionWithTokenSecretAndRawResponse() {
 Token token = new Token("testToken", "testSecret", "testRawResponse");
 assertEquals("testToken", token.getToken());
 assertEquals("testSecret", token.getSecret());
 assertEquals("testRawResponse", token.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRawResponseWhenTokenIsNotConstructedWithRawResponse() {
 Token token = new Token("testToken", "testSecret");
 token.getRawResponse();
 }

 @Test
 public void testToStringMethod() {
 Token token = new Token("testToken", "testSecret");
 String expected = "Token[testToken , testSecret]";
 assertEquals(expected, token.toString());
 }
}