package org.scribe.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {

 // Test constructor with token and secret
 @Test
 public void testTokenConstructionWithTokenAndSecret() {
     String token = "abcdef123456";
     String secret = "123456abcdef";
     Token tokenObj = new Token(token, secret);
     assertEquals(token, tokenObj.getToken());
     assertEquals(secret, tokenObj.getSecret());
     assertNull(tokenObj.getRawResponse());
 }

 // Test constructor with token, secret, and rawResponse
 @Test
 public void testTokenConstructionWithTokenAndSecretAndRawResponse() {
     String token = "abcdef123456";
     String secret = "123456abcdef";
     String rawResponse = "{\"access_token\":\"abcdef123456\", \"token_type\":\"bearer\"}";
     Token tokenObj = new Token(token, secret, rawResponse);
     assertEquals(token, tokenObj.getToken());
     assertEquals(secret, tokenObj.getSecret());
     assertEquals(rawResponse, tokenObj.getRawResponse());
 }

 // Test getToken method
 @Test
 public void testGetTokenMethod() {
     String token = "abcdef123456";
     String secret = "123456abcdef";
     Token tokenObj = new Token(token, secret);
     String returnedToken = tokenObj.getToken();
     assertEquals(token, returnedToken);
 }

 // Test getSecret method
 @Test
 public void testGetSecretMethod() {
     String token = "abcdef123456";
     String secret = "123456abcdef";
     Token tokenObj = new Token(token, secret);
     String returnedSecret = tokenObj.getSecret();
     assertEquals(secret, returnedSecret);
 }

 // Test getRawResponse method with null token object
 @Test(expected = IllegalStateException.class)
 public void testGetRawResponseMethodWithNullTokenObject() {
     Token tokenObj = null;
     assertThrows(IllegalStateException.class, () -> tokenObj.getRawResponse());
 }

 // Test getRawResponse method with token object without rawResponse
 @Test
 public void testGetRawResponseMethodWithTokenObjectWithoutRawResponse() {
     String token = "abcdef123456";
     String secret = "123456abcdef";
     Token tokenObj = new Token(token, secret);
     assertNull(tokenObj.getRawResponse());
 }

 // Test getRawResponse method with token object with rawResponse
 @Test
 public void testGetRawResponseMethodWithTokenObjectWithRawResponse() {
     String token = "abcdef123456";
     String secret = "123456abcdef";
     String rawResponse = "{\"access_token\":\"abcdef123456\", \"token_type\":\"bearer\"}";
     Token tokenObj = new Token(token, secret, rawResponse);
     String returnedRawResponse = tokenObj.getRawResponse();
     assertEquals(rawResponse, returnedRawResponse);
 }

 // Test toString method
 @Test
 public void testToStringMethod() {
     String token = "abcdef123456";
     String secret = "123456abcdef";
     Token tokenObj = new Token(token, secret);
     String expectedToString = "Token[abcdef123456 , 123456abcdef]";
     String returnedToString = tokenObj.toString();
     assertEquals(expectedToString, returnedToString);
 }

}