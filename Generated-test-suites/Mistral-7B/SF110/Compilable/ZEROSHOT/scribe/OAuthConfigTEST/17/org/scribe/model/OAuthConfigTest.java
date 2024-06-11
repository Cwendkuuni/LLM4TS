package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class OAuthConfigTest {

 private String apiKey = "testKey";
 private String apiSecret = "testSecret";
 private String callback = "testCallback";
 private SignatureType signatureType = SignatureType.Header;
 private String scope = "testScope";

 @Test
 public void testOAuthConfigWithNullKeyAndSecret() {
     OAuthConfig config = new OAuthConfig(null, null);
     assertNull(config.getApiKey());
     assertNull(config.getApiSecret());
     assertNull(config.getCallback());
     assertNull(config.getSignatureType());
     assertNull(config.getScope());
     assertFalse(config.hasScope());
 }

 @Test
 public void testOAuthConfigWithValidArguments() {
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
     assertEquals(apiKey, config.getApiKey());
     assertEquals(apiSecret, config.getApiSecret());
     assertEquals(callback, config.getCallback());
     assertEquals(signatureType, config.getSignatureType());
     assertEquals(scope, config.getScope());
     assertTrue(config.hasScope());
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullSignatureType() {
     new OAuthConfig(apiKey, apiSecret, callback, null, scope);
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullScope() {
     new OAuthConfig(apiKey, apiSecret, callback, signatureType, null);
 }
}