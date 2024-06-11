package org.scribe.model;
 import static org.junit.Assert.*;
 import org.junit.Test;

 public class TokenTest {

 @Test
 public void testTokenConstructorWithTokenAndSecret() {
 Token token = new Token("sampleToken", "sampleSecret");
 assertEquals("sampleToken", token.getToken());
 assertEquals("sampleSecret", token.getSecret());
 assertNull(token.getRawResponse());
 }

 @Test
 public void testTokenConstructorWithTokenSecretAndRawResponse() {
 Token token = new Token("sampleToken", "sampleSecret", "sampleRawResponse");
 assertEquals("sampleToken", token.getToken());
 assertEquals("sampleSecret", token.getSecret());
 assertEquals("sampleRawResponse", token.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRawResponseWhenNotConstructedByScribe() {
 Token token = new Token("sampleToken", "sampleSecret");
 token.getRawResponse();
 }

 @Test
 public void testToString() {
 Token token = new Token("sampleToken", "sampleSecret");
 String expected = String.format("Token[%s , %s]", "sampleToken", "sampleSecret");
 assertEquals(expected, token.toString());
 }
 }