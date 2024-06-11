package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class OAuthConfigTest {

 private String apiKey;
 private String apiSecret;
 private String callback;
 private SignatureType signatureType;
 private String scope;

 @Test
 public void testOAuthConfigWithNullKeyAndSecret() {
  OAuthConfig config = new OAuthConfig(null, null);
  assertNull(config.getApiKey());
  assertNull(config.getApiSecret());
  assertNull(config.getCallback());
  assertEquals(SignatureType.Header, config.getSignatureType());
  assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecret() {
  apiKey = "testApiKey";
  apiSecret = "testApiSecret";
  OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
  assertEquals(apiKey, config.getApiKey());
  assertEquals(apiSecret, config.getApiSecret());
  assertNull(config.getCallback());
  assertEquals(SignatureType.Header, config.getSignatureType());
  assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecretAndCallback() {
  apiKey = "testApiKey";
  apiSecret = "testApiSecret";
  callback = "testCallback";
  OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback);
  assertEquals(apiKey, config.getApiKey());
  assertEquals(apiSecret, config.getApiSecret());
  assertEquals(callback, config.getCallback());
  assertEquals(SignatureType.Header, config.getSignatureType());
  assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecretAndSignatureType() {
  apiKey = "testApiKey";
  apiSecret = "testApiSecret";
  signatureType = SignatureType.QueryString;
  OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, signatureType, null);
  assertEquals(apiKey, config.getApiKey());
  assertEquals(apiSecret, config.getApiSecret());
  assertNull(config.getCallback());
  assertEquals(SignatureType.QueryString, config.getSignatureType());
  assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecretAndScope() {
  apiKey = "testApiKey";
  apiSecret = "testApiSecret";
  scope = "testScope";
  OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, null, scope);
  assertEquals(apiKey, config.getApiKey());
  assertEquals(apiSecret, config.getApiSecret());
  assertNull(config.getCallback());
  assertEquals(SignatureType.Header, config.getSignatureType());
  assertEquals(scope, config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecretAndCallbackAndSignatureType() {
  apiKey = "testApiKey";
  apiSecret = "testApiSecret";
  callback = "testCallback";
  signatureType = SignatureType.QueryString;
  OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, null);
  assertEquals(apiKey, config.getApiKey());
  assertEquals(apiSecret, config.getApiSecret());
  assertEquals(callback, config.getCallback());
  assertEquals(SignatureType.QueryString, config.getSignatureType());
  assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecretAndCallbackAndScope() {
  apiKey = "testApiKey";
  apiSecret = "testApiSecret";
  callback = "testCallback";
  scope = "testScope";
  OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, scope);
  assertEquals(apiKey, config.getApiKey());
  assertEquals(apiSecret, config.getApiSecret());
  assertEquals(callback, config.getCallback());
  assertEquals(SignatureType.Header, config.getSignatureType());
  assertEquals(scope, config.getScope());
 }

 @Test
 public void testOAuthConfigWithNullKeyAndSecretButValidCallback() {
  callback = "testCallback";
  OAuthConfig config = new OAuthConfig(null, null, callback);
  assertNull(config.getApiKey());
  assertNull(config.getApiSecret());
  assertEquals(callback, config.getCallback());
  assertEquals(SignatureType.Header, config.getSignatureType());
  assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithNullKeyAndSecretButInvalidCallback() {
  OAuthConfig config = new OAuthConfig(null, null);
  assertNull(config.getApiKey());
  assertNull(config.getApiSecret());
  assertNull(config.getCallback());
  assertEquals(SignatureType.Header, config.getSignatureType());
  assertNull(config.getScope());
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullSignatureType() {
  apiKey = "testApiKey";
  apiSecret = "testApiSecret";
  signatureType = null;
  OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, signatureType, null);
 }

 @Test
 public void testHasScope() {
  OAuthConfig config1 = new OAuthConfig("testApiKey1", "testApiSecret1");
  assertFalse(config1.hasScope());
  OAuthConfig config2 = new OAuthConfig("testApiKey2", "testApiSecret2", null, null, "testScope");
  assertTrue(config2.hasScope());
 }
}