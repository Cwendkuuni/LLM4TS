package org.scribe.model.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.Token;

public class TokenTest {
  @Test
  public void testTokenConstructorWithTokenAndSecret() {
    String token = "abc123";
    String secret = "def456";
    Token tokenObject = new Token(token, secret);
    String expectedToken = token;
    String expectedSecret = secret;
    String actualToken = tokenObject.getToken();
    String actualSecret = tokenObject.getSecret();
    assertEquals("Unexpected token value", expectedToken, actualToken);
    assertEquals("Unexpected secret value", expectedSecret, actualSecret);
  }
  
  @Test
  public void testTokenConstructorWithTokenSecretAndRawResponse() {
    String token = "abc123";
    String secret = "def456";
    String rawResponse = "this is a raw response";
    Token tokenObject = new Token(token, secret, rawResponse);
    String expectedToken = token;
    String expectedSecret = secret;
    String expectedRawResponse = rawResponse;
    String actualToken = tokenObject.getToken();
    String actualSecret = tokenObject.getSecret();
    String actualRawResponse = tokenObject.getRawResponse();
    assertEquals("Unexpected token value", expectedToken, actualToken);
    assertEquals("Unexpected secret value", expectedSecret, actualSecret);
    assertEquals("Unexpected raw response value", expectedRawResponse, actualRawResponse);
  }
  
  @Test
  public void testGetters() {
    String token = "abc123";
    String secret = "def456";
    Token tokenObject = new Token(token, secret, null);
    String expectedToken = token;
    String expectedSecret = secret;
    String actualToken = tokenObject.getToken();
    String actualSecret = tokenObject.getSecret();
    assertEquals("Unexpected token value", expectedToken, actualToken);
    assertEquals("Unexpected secret value", expectedSecret, actualSecret);
    
    String illegalStateExceptionMessage = "Unexpected exception message";
    try {
      tokenObject.getRawResponse();
      fail("Expected IllegalStateException to be thrown");
    } catch (IllegalStateException e) {
      assertEquals("Unexpected exception message", illegalStateExceptionMessage, e.getMessage());
    }
  }
}