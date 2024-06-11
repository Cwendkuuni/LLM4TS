package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class OAuthConfigTest {

 private String apiKey = "testApiKey";
 private String apiSecret = "testApiSecret";
 private String callback = "testCallback";
 private SignatureType signatureType = SignatureType.Header;
 private String scope = "testScope";

 @Test
 public void testOAuthConfigWithKeyAndSecret() {
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
     assertEquals(apiKey, config.getApiKey());
     assertEquals(apiSecret, config.getApiSecret());
     assertNull(config.getCallback());
     assertEquals(SignatureType.Header, config.getSignatureType());
     assertNull(config.getScope());
     assertFalse(config.hasScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallbackAndSignatureType() {
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
     assertEquals(apiKey, config.getApiKey());
     assertEquals(apiSecret, config.getApiSecret());
     assertEquals(callback, config.getCallback());
     assertEquals(signatureType, config.getSignatureType());
     assertEquals(scope, config.getScope());
     assertTrue(config.hasScope());
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullApiKey() {
     try {
         new OAuthConfig(null, apiSecret);
         fail("Expected NullPointerException");
     } catch (NullPointerException e) {
         // expected
     }
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullApiSecret() {
     try {
         new OAuthConfig(apiKey, null);
         fail("Expected NullPointerException");
     } catch (NullPointerException e) {
         // expected
     }
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullApiKeyAndApiSecret() {
     try {
         new OAuthConfig(null, null);
         fail("Expected NullPointerException");
     } catch (NullPointerException e) {
         // expected
     }
 }

 @Test
 public void testOAuthConfigWithEmptyCallback() {
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret, "", signatureType, scope);
     assertEquals(apiKey, config.getApiKey());
     assertEquals(apiSecret, config.getApiSecret());
     assertEquals("", config.getCallback());
     assertEquals(SignatureType.Header, config.getSignatureType());
     assertNull(config.getScope());
     assertFalse(config.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullSignatureType() {
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, scope);
     assertEquals(apiKey, config.getApiKey());
     assertEquals(apiSecret, config.getApiSecret());
     assertEquals(callback, config.getCallback());
     assertNull(config.getSignatureType());
     assertEquals(scope, config.getScope());
     assertFalse(config.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullScope() {
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, null);
     assertEquals(apiKey, config.getApiKey());
     assertEquals(apiSecret, config.getApiSecret());
     assertEquals(callback, config.getCallback());
     assertEquals(signatureType, config.getSignatureType());
     assertNull(config.getScope());
     assertFalse(config.hasScope());
 }

}