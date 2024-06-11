package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TokenTest {

 private String token = "testToken";
 private String secret = "testSecret";
 private String rawResponse = "testRawResponse";

 @Test
 public void testTokenConstructionWithTokenAndSecret() {
     Token tokenObj = new Token(token, secret);
     assertEquals(token, tokenObj.getToken());
     assertEquals(secret, tokenObj.getSecret());
     assertNull(tokenObj.getRawResponse());
 }

 @Test
 public void testTokenConstructionWithTokenSecretAndRawResponse() {
     Token tokenObj = new Token(token, secret, rawResponse);
     assertEquals(token, tokenObj.getToken());
     assertEquals(secret, tokenObj.getSecret());
     assertEquals(rawResponse, tokenObj.getRawResponse());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRawResponseWithoutRawResponse() {
     Token tokenObj = new Token(token, secret);
     assertThrows(IllegalStateException.class, () -> tokenObj.getRawResponse());
 }

 @Test
 public void testGetToken() {
     Token tokenObj = new Token(token, secret);
     assertEquals(token, tokenObj.getToken());
 }

 @Test
 public void testGetSecret() {
     Token tokenObj = new Token(token, secret);
     assertEquals(secret, tokenObj.getSecret());
 }

 @Test
 public void testToString() {
     Token tokenObj = new Token(token, secret);
     assertEquals("Token[testToken , testSecret]", tokenObj.toString());
 }

 @Test
 public void testTokenEquality() {
     Token tokenObj1 = new Token(token, secret);
     Token tokenObj2 = new Token(token, secret);
     Token tokenObj3 = new Token("differentToken", secret);
     assertEquals(tokenObj1, tokenObj2);
     assertNotEquals(tokenObj1, tokenObj3);
 }

 @Test
 public void testTokenHashCode() {
     Token tokenObj1 = new Token(token, secret);
     Token tokenObj2 = new Token(token, secret);
     Token tokenObj3 = new Token("differentToken", secret);
     assertEquals(tokenObj1.hashCode(), tokenObj2.hashCode());
     assertNotEquals(tokenObj1.hashCode(), tokenObj3.hashCode());
 }
}