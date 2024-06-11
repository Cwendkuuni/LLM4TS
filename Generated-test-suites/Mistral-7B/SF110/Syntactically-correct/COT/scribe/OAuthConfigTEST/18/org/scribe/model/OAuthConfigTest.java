package org.scribe.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignaturaType;

public class OAuthConfigTest {

 // Test constructor with only apiKey and apiSecret
 @Test
 public void testOAuthConfigWithApiKeyAndApiSecret() {
     String apiKey = "testKey";
     String apiSecret = "testSecret";
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
     assertNotNull(config);
     assertEquals(apiKey, config.getApiKey());
     assertEquals(apiSecret, config.getApiSecret());
     assertNull(config.getCallback());
     assertEquals(SignaturaType.Header, config.getSignatureType());
     assertNull(config.getScope());
 }

 // Test constructor with all parameters
 @Test
 public void testOAuthConfigWithAllParameters() {
     String apiKey = "testKey";
     String apiSecret = "testSecret";
     String callback = "http://example.com/callback";
     SignaturaType signatureType = SignaturaType.QueryString;
     String scope = "testScope";
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
     assertNotNull(config);
     assertEquals(apiKey, config.getApiKey());
     assertEquals(apiSecret, config.getApiSecret());
     assertEquals(callback, config.getCallback());
     assertEquals(signatureType, config.getSignatureType());
     assertEquals(scope, config.getScope());
 }

 // Test getters for non-nullable fields
 @Test
 public void testGettersForNonNullableFields() {
     String apiKey = "testKey";
     String apiSecret = "testSecret";
     String callback = "http://example.com/callback";
     SignaturaType signatureType = SignaturaType.Header;
     String scope = "testScope";
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
     assertEquals(apiKey, config.getApiKey());
     assertEquals(apiSecret, config.getApiSecret());
     assertEquals(callback, config.getCallback());
     assertEquals(signatureType, config.getSignatureType());
     assertEquals(scope, config.getScope());
 }

 // Test getters for nullable fields
 @Test
 public void testGettersForNullableFields() {
     String apiKey = "testKey";
     String apiSecret = "testSecret";
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
     assertNull(config.getCallback());
     assertNull(config.getScope());
 }

 // Test hasScope method
 @Test
 public void testHasScope() {
     String apiKey = "testKey";
     String apiSecret = "testSecret";
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
     assertFalse(config.hasScope());
     
     String apiKey2 = "testKey";
     String apiSecret2 = "testSecret";
     String scope = "testScope";
     OAuthConfig config2 = new OAuthConfig(apiKey2, apiSecret2, null, null, scope);
     assertTrue(config2.hasScope());
 }

 // Test exception handling when apiKey or apiSecret are null
 @Test(expected = NullPointerException.class)
 public void testExceptionWithNullApiKey() {
     String apiSecret = "testSecret";
     String apiKey = null;
     new OAuthConfig(apiKey, apiSecret);
 }

 @Test(expected = NullPointerException.class)
 public void testExceptionWithNullApiSecret() {
     String apiKey = "testKey";
     String apiSecret = null;
     new OAuthConfig(apiKey, apiSecret);
 }

}