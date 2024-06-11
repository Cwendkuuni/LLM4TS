package org.scribe.model;

import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OAuthConfigTest {

 private String apiKey = "testApiKey";
 private String apiSecret = "testApiSecret";
 private String callback = "testCallback";
 private SignatureType signatureType = SignatureType.Header;
 private String scope = "testScope";

 @Test
 public void testDefaultConstructor() {
     OAuthConfig config = new OAuthConfig();
     assertNull(config.apiKey);
     assertNull(config.apiSecret);
     assertNull(config.callback);
     assertNull(config.signatureType);
     assertNull(config.scope);
 }

 @Test
 public void testConstructorWithApiKeyAndApiSecret() {
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
     assertEquals(apiKey, config.apiKey);
     assertEquals(apiSecret, config.apiSecret);
     assertNull(config.callback);
     assertNull(config.signatureType);
     assertNull(config.scope);
 }

 @Test
 public void testConstructorWithAllParameters() {
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
     assertEquals(apiKey, config.apiKey);
     assertEquals(apiSecret, config.apiSecret);
     assertEquals(callback, config.callback);
     assertEquals(signatureType, config.signatureType);
     assertEquals(scope, config.scope);
 }

 @Test
 public void testGetters() {
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
     assertEquals(apiKey, config.getApiKey());
     assertEquals(apiSecret, config.getApiSecret());
     assertEquals(callback, config.getCallback());
     assertEquals(signatureType, config.getSignatureType());
     assertEquals(scope, config.getScope());
 }

 @Test
 public void testHasScope() {
     OAuthConfig configWithScope = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
     assertTrue(configWithScope.hasScope());

     OAuthConfig configWithoutScope = new OAuthConfig(apiKey, apiSecret, callback, signatureType, null);
     assertFalse(configWithoutScope.hasScope());
 }
}