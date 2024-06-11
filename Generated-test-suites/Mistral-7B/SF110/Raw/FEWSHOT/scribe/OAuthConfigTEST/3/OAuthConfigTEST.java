package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithKeyAndSecret() {
 OAuthConfig config = new OAuthConfig("key", "secret");
 assertEquals("key", config.getApiKey());
 assertEquals("secret", config.getApiSecret());
 assertNull(config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallback() {
 OAuthConfig config = new OAuthConfig("key", "secret", "http://localhost:8080/callback");
 assertEquals("key", config.getApiKey());
 assertEquals("secret", config.getApiSecret());
 assertEquals("http://localhost:8080/callback", config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallbackScope() {
 OAuthConfig config = new OAuthConfig("key", "secret", "http://localhost:8080/callback", SignatureType.QueryString, "scope1 scope2");
 assertEquals("key", config.getApiKey());
 assertEquals("secret", config.getApiSecret());
 assertEquals("http://localhost:8080/callback", config.getCallback());
 assertEquals(SignatureType.QueryString, config.getSignatureType());
 assertEquals("scope1 scope2", config.getScope());
 }

 @Test
 public void testOAuthConfigWithNullKey() {
 assertNull(new OAuthConfig(null, "secret"));
 }

 @Test
 public void testOAuthConfigWithNullSecret() {
 assertNull(new OAuthConfig("key", null));
 }

 @Test
 public void testOAuthConfigWithNullCallback() {
 OAuthConfig config = new OAuthConfig("key", "secret");
 assertEquals("oob", config.getCallback());
 }

 @Test
 public void testOAuthConfigWithNullSignatureType() {
 OAuthConfig config = new OAuthConfig("key", "secret");
 assertEquals(SignatureType.Header, config.getSignatureType());
 }

 @Test
 public void testOAuthConfigWithNullScope() {
 OAuthConfig config = new OAuthConfig("key", "secret");
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithEmptyScope() {
 OAuthConfig config = new OAuthConfig("key", "secret", null, null, "");
 assertNull(config.getScope());
 }
}