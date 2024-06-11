package org.scribe.model;

import org.junit.Test;
import org.junit.Assert;

public class OAuthConfigTest {

 private String apiKey = "testApiKey";
 private String apiSecret = "testApiSecret";
 private String callback = "testCallback";
 private SignatureType signatureType = SignatureType.Header;
 private String scope = "testScope";

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecret() {
 OAuthConfig config = new OAuthConfig(apiKey, apiSecret);

 Assert.assertEquals(apiKey, config.getApiKey());
 Assert.assertEquals(apiSecret, config.getApiSecret());
 Assert.assertEquals("oob", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndCallback() {
 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback);

 Assert.assertEquals(apiKey, config.getApiKey());
 Assert.assertEquals(apiSecret, config.getApiSecret());
 Assert.assertEquals(callback, config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndCallbackAndSignatureType() {
 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType);

 Assert.assertEquals(apiKey, config.getApiKey());
 Assert.assertEquals(apiSecret, config.getApiSecret());
 Assert.assertEquals(callback, config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndCallbackAndSignatureTypeAndScope() {
 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

 Assert.assertEquals(apiKey, config.getApiKey());
 Assert.assertEquals(apiSecret, config.getApiSecret());
 Assert.assertEquals(callback, config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertEquals(scope, config.getScope());
 }

 @Test
 public void testOAuthConfigWithNullApiKeyAndApiSecret() {
 try {
 OAuthConfig config = new OAuthConfig(null, null);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testOAuthConfigWithNullApiSecret() {
 try {
 OAuthConfig config = new OAuthConfig(apiKey, null);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testOAuthConfigWithNullCallback() {
 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null);

 Assert.assertEquals(apiKey, config.getApiKey());
 Assert.assertEquals(apiSecret, config.getApiSecret());
 Assert.assertEquals("oob", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithNullSignatureType() {
 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null);

 Assert.assertEquals(apiKey, config.getApiKey());
 Assert.assertEquals(apiSecret, config.getApiSecret());
 Assert.assertEquals(callback, config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithHasScope() {
 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

 Assert.assertEquals(apiKey, config.getApiKey());
 Assert.assertEquals(apiSecret, config.getApiSecret());
 Assert.assertEquals(callback, config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertEquals("testScope", config.getScope());
 Assert.assertTrue(config.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullScope() {
 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType);

 Assert.assertEquals(apiKey, config.getApiKey());
 Assert.assertEquals(apiSecret, config.getApiSecret());
 Assert.assertEquals(callback, config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 Assert.assertFalse(config.hasScope());
 }
}