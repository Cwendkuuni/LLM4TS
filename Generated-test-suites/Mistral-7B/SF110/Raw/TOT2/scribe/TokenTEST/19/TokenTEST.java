package org.scribe.model;

import org.junit.Assert;
import org.junit.Test;

public class TokenTest {
   

   @Test
   public void testTokenConstructorWithTokenAndSecret() {
      Token token = new Token("testToken", "testSecret");
      assertNotNull(token);
      assertEquals("testToken", token.getToken());
      assertEquals("testSecret", token.getSecret());
   }

   @Test
   public void testTokenConstructorWithTokenSecretAndRawResponse() {
      Token token = new Token("testToken", "testSecret", "testRawResponse");
      assertNotNull(token);
      assertEquals("testToken", token.getToken());
      assertEquals("testSecret", token.getSecret());
      assertEquals("testRawResponse", token.getRawResponse());
   }

   @Test
   public void testGetTokenMethod() {
      Token token = new Token("testToken", "testSecret");
      assertEquals("testToken", token.getToken());
   }

   @Test
   public void testGetSecretMethod() {
      Token token = new Token("testToken", "testSecret");
      assertEquals("testSecret", token.getSecret());
   }

   @Test
   public void testGetRawResponseMethod() {
      Token token = new Token("testToken", "testSecret", "testRawResponse");
      assertEquals("testRawResponse", token.getRawResponse());
   }

   @Test
   public void testToStringMethod() {
      Token token = new Token("testToken", "testSecret");
      assertEquals("Token[testToken , testSecret]", token.toString());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testTokenConstructorWithNullToken() {
      new Token(null, "testSecret");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testTokenConstructorWithNullSecret() {
      new Token("testToken", null);
   }

   @Test
   public void testToStringMethodWithNullToken() {
      Token token = new Token(null, "testSecret");
      assertEquals("Token[null , testSecret]", token.toString());
   }

   @Test
   public void testToStringMethodWithNullSecret() {
      Token token = new Token("testToken", null);
      assertEquals("Token[testToken , null]", token.toString());
   }

   @Test(expected = IllegalStateException.class)
   public void testGetRawResponseMethodWithoutRawResponse() {
      Token token = new Token("testToken", "testSecret");
      token.getRawResponse();
   }

   
}