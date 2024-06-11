package org.scribe.model;

import org.junit.Test;
import org.junit.Assert.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OAuthConfigTest {

 private OAuthConfig config;
 private String key = "testKey";
 private String secret = "testSecret";
 private String callback = "testCallback";
 private SignatureType signatureType = SignatureType.Header;
 private String scope = "testScope";

 @Test
 public void testOAuthConfigWithKeyAndSecret() {
  config = new OAuthConfig(key, secret);
  assertEquals(key, config.getApiKey());
  assertEquals(secret, config.getApiSecret());
  assertNull(config.getCallback());
  assertEquals(SignatureType.Header, config.getSignatureType());
  assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecretAndCallback() {
  config = new OAuthConfig(key, secret, callback);
  assertEquals(key, config.getApiKey());
  assertEquals(secret, config.getApiSecret());
  assertEquals(callback, config.getCallback());
  assertNull(config.getSignatureType());
  assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecretAndCallbackAndSignatureType() {
  SignatureType signatureTypeMock = mock(SignatureType.class);
  config = new OAuthConfig(key, secret, callback, signatureType, scope);
  assertEquals(key, config.getApiKey());
  assertEquals(secret, config.getApiSecret());
  assertEquals(callback, config.getCallback());
  assertEquals(signatureType, config.getSignatureType());
  assertEquals(scope, config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecretAndSignatureType() {
  SignatureType signatureTypeMock = mock(SignatureType.class);
  config = new OAuthConfig(key, secret, null, signatureType, scope);
  assertEquals(key, config.getApiKey());
  assertEquals(secret, config.getApiSecret());
  assertNull(config.getCallback());
  assertEquals(signatureType, config.getSignatureType());
  assertEquals(scope, config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecretAndScope() {
  config = new OAuthConfig(key, secret, null, null, scope);
  assertEquals(key, config.getApiKey());
  assertEquals(secret, config.getApiSecret());
  assertNull(config.getCallback());
  assertNull(config.getSignatureType());
  assertEquals(scope, config.getScope());
 }

 @Test
 public void testHasScope() {
  config = new OAuthConfig(key, secret, null, null, null);
  assertFalse(config.hasScope());
  config = new OAuthConfig(key, secret, null, null, "testScope");
  assertTrue(config.hasScope());
 }
}