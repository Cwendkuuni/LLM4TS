
package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
   @Test
   public void testTokenConstructorWithTokenAndSecret() {
      String token = "abcdef12345";
      String secret = "secret123";
      Token tokenObject = new Token(token, secret);
      assertEquals(token, tokenObject.getToken());
      assertEquals(secret, tokenObject.getSecret());
   }

   @Test
   public void testTokenConstructorWithTokenSecretAndRawResponse() {
      String token = "tokenXyz";
      String secret = "secretXyz";
      String rawResponse = "rawResponseXyz";
      Token tokenObject = new Token(token, secret, rawResponse);
      assertEquals(token, tokenObject.getToken());
      assertEquals(secret, tokenObject.getSecret());
      assertEquals(rawResponse, tokenObject.getRawResponse());

      // Test with null rawResponse
      Token nullRawResponseTokenObject = new Token(token, secret);
      assertNull(tokenObject.getRawResponse());
   }

   @Test
   public void testGetters() {
      String token = "token123";
      String secret = "secret123";
      String rawResponse = "rawResponse123";
      Token tokenObject = new Token(token, secret, rawResponse);

      assertEquals(token, tokenObject.getToken());
      assertEquals(secret, tokenObject.getSecret());
      assertNotNull(tokenObject.getRawResponse());
      assertEquals("Token[token123 , secret123]", tokenObject.toString());
   }

   @Test(expected = IllegalStateException.class)
   public void testIllegalStateException() {
      String token = "token123";
      String secret = "secret123";
      Token tokenObject = new Token(token, secret);
      tokenObject.getRawResponse();
   }
}