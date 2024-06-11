package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecret() {
 OAuthConfig config = new OAuthConfig("key", "secret");
 assertEquals("key", config.getApiKey());
 assertEquals("secret", config.getApiSecret());
 assertNull(config.getCallback());
 assertNull(config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndCallback() {
 OAuthConfig config = new OAuthConfig("key", "secret", "callback");
 assertEquals("key", config.getApiKey());
 assertEquals("secret", config.getApiSecret());
 assertEquals("callback", config.getCallback());
 assertNull(config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndCallbackAndSignatureType() {
 OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
 assertEquals("key", config.getApiKey());
 assertEquals("secret", config.getApiSecret());
 assertEquals("callback", config.getCallback());
 assertEquals(SignatureType.QueryString, config.getSignatureType());
 assertEquals("scope", config.getScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndSignatureType() {
 OAuthConfig config = new OAuthConfig("key", "secret", null, SignatureType.Header, null);
 assertEquals("key", config.getApiKey());
 assertEquals("secret", config.getApiSecret());
 assertNull(config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndScope() {
 OAuthConfig config = new OAuthConfig("key", "secret", null, null, "scope");
 assertEquals("key", config.getApiKey());
 assertEquals("secret", config.getApiSecret());
 assertNull(config.getCallback());
 assertNull(config.getSignatureType());
 assertEquals("scope", config.getScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndHasScope() {
 OAuthConfig config = new OAuthConfig("key", "secret", null, null, "scope");
 assertTrue(config.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullApiKey() {
 assertNull(new OAuthConfig(null, "secret"));
 }

 @Test
 public void testOAuthConfigWithNullApiSecret() {
 assertNull(new OAuthConfig("key", null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOAuthConfigWithNullApiKeyAndApiSecret() {
 new OAuthConfig(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOAuthConfigWithNullCallback() {
 new OAuthConfig("key", "secret", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOAuthConfigWithNullSignatureType() {
 new OAuthConfig("key", "secret", null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOAuthConfigWithNullScope() {
 new OAuthConfig("key", "secret", null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOAuthConfigWithEmptyApiKey() {
 new OAuthConfig("", "secret");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOAuthConfigWithEmptyApiSecret() {
 new OAuthConfig("key", "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOAuthConfigWithEmptyApiKeyAndApiSecret() {
 new OAuthConfig("", "");
 }
}