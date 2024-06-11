package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithKeyAndSecret() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 assertEquals("testKey", config.getApiKey());
 assertEquals("testSecret", config.getApiSecret());
 assertNull(config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallback() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "http://localhost:8080/callback");
 assertEquals("testKey", config.getApiKey());
 assertEquals("testSecret", config.getApiSecret());
 assertEquals("http://localhost:8080/callback", config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallbackAndScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "http://localhost:8080/callback", SignatureType.QueryString, "scope1 scope2");
 assertEquals("testKey", config.getApiKey());
 assertEquals("testSecret", config.getApiSecret());
 assertEquals("http://localhost:8080/callback", config.getCallback());
 assertEquals(SignatureType.QueryString, config.getSignatureType());
 assertEquals("scope1 scope2", config.getScope());
 }

 @Test
 public void testOAuthConfigWithNullValues() {
 OAuthConfig config = new OAuthConfig(null, null, null, null, null);
 assertNull(config.getApiKey());
 assertNull(config.getApiSecret());
 assertNull(config.getCallback());
 assertNull(config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithEmptyStringValues() {
 OAuthConfig config = new OAuthConfig("", "", "", null, "");
 assertEquals("", config.getApiKey());
 assertEquals("", config.getApiSecret());
 assertNull(config.getCallback());
 assertNull(config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithNullSignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, null);
 assertEquals(SignatureType.Header, config.getSignatureType());
 }

 @Test
 public void testOAuthConfigWithNullScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, SignatureType.Header, null);
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigHasScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, SignatureType.Header, "scope1 scope2");
 assertTrue(config.hasScope());
 }

 @Test
 public void testOAuthConfigHasNoScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, SignatureType.Header, null);
 assertFalse(config.hasScope());
 }
}